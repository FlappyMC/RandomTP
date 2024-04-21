package fr.flappy.randomtp.teleportation;

import fr.flappy.randomtp.SatisRandomTP;
import fr.flappy.randomtp.manager.PlayerManager;
import fr.flappy.randomtp.utils.Lang;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeleportPlayer {
    TeleportationUtils teleportationUtils = JavaPlugin.getPlugin(SatisRandomTP.class).getTeleportationUtils();
    public static Map<PlayerManager, BukkitTask> teleportingPlayers = new HashMap<>();
    private static long COOLDOWN_PERIOD = 5 * 60 * 1000;
    private final Map<UUID, Long> lastTeleportTimes = new HashMap<>();

    public TeleportPlayer(PlayerManager playerManager, int lvl){
        if(checkTeleportationConditions(playerManager, lvl)){
            teleportationUtils.getTeleportingPlayers().add(playerManager);
            SafeLocation safeLocation = new SafeLocation(lvl);
            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    System.out.println("Teleporting " + playerManager.getPlayer().getName() + " (" + playerManager.getUuid() + ")");
                    if(safeLocation.getSafeLocation() != null){
                        teleportationUtils.getTeleportingPlayers().remove(playerManager);
                        playerManager.teleport(safeLocation.getSafeLocation());
                        String message = ChatColor.translateAlternateColorCodes('&', Lang.TELEPORT.getMessage().replaceAll("%location%", "x: " + safeLocation.getSafeLocation().getBlockX() + " y: " + safeLocation.getSafeLocation().getBlockY() + " z: " + safeLocation.getSafeLocation().getBlockZ()));
                        playerManager.sendMessage(message);

                        if(!lastTeleportTimes.containsKey(playerManager.getUuid()))
                            System.out.println("No last teleport time for " + playerManager.getPlayer().getName() + " (" + playerManager.getUuid() + ")");
                            lastTeleportTimes.put(playerManager.getUuid(), System.currentTimeMillis());
                    }
                }
            }.runTaskLater(JavaPlugin.getPlugin(SatisRandomTP.class), teleportationUtils.getDelayPerLevel(lvl) * 20L);

            teleportingPlayers.put(playerManager, task);
        }
    }

    private boolean checkTeleportationConditions(PlayerManager playerManager, int lvl){
        if(lastTeleportTimes.containsKey(playerManager.getUuid())){
            System.out.println("Last teleport time for " + playerManager.getPlayer().getName() + " (" + playerManager.getUuid() + ") : " + lastTeleportTimes.get(playerManager.getUuid()));
            long lastTeleportTime = lastTeleportTimes.get(playerManager.getUuid());
            if(System.currentTimeMillis() - lastTeleportTime < COOLDOWN_PERIOD){
                Lang.COOLDOWN.send(playerManager);
                return false;
            }
        }
        if(teleportationUtils.getTeleportingPlayers().contains(playerManager)){
            Lang.ALREADY_TELEPORTING.send(playerManager);
            return false;
        }

        if(!teleportationUtils.isLevelsEnabled()){
            Lang.DISABLED.send(playerManager);
            return false;
        }

        if(!playerManager.hasPermission("randomtp.lvl" + lvl)){
            Lang.PERMISSIONS.send(playerManager);
            return false;
        }
        teleportationUtils.getTeleportingPlayers().add(playerManager);
        String willTeleport = Lang.WILL.getMessage().replace("%delay%", String.valueOf(teleportationUtils.getDelayPerLevel(lvl)));
        playerManager.sendMessage(Lang.PREFIX.getMessage() + willTeleport);
        return true;
    }

    public static Map<PlayerManager, BukkitTask> getTeleportingPlayers() {
        return teleportingPlayers;
    }
}
