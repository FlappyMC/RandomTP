package fr.flappy.randomtp.teleportation;

import fr.flappy.randomtp.SatisRandomTP;
import fr.flappy.randomtp.manager.PlayerManager;
import fr.flappy.randomtp.utils.Lang;
import fr.flappy.randomtp.utils.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TeleportPlayer {
    private static long COOLDOWN_PERIOD = 0;
    private static final Map<UUID, Long> lastTeleportTimes = new HashMap<>();
    TeleportationUtils teleportationUtils = JavaPlugin.getPlugin(SatisRandomTP.class).getTeleportationUtils();
    private final Set<PlayerManager> teleportingPlayers = JavaPlugin.getPlugin(SatisRandomTP.class).getTeleportationUtils().getTeleportingPlayers();
    private static BukkitTask task;

    public TeleportPlayer(PlayerManager playerManager, int lvl){
        COOLDOWN_PERIOD = TimeUnit.MINUTES.toMillis(JavaPlugin.getPlugin(SatisRandomTP.class).getTeleportationUtils().getCooldownPerLvl(lvl));
        if(checkTeleportationConditions(playerManager, lvl)){
            teleportingPlayers.add(playerManager);
            SafeLocation safeLocation = new SafeLocation(lvl);
            task = new BukkitRunnable() {
                @Override
                public void run() {
                    if(safeLocation.getSafeLocation() != null){
                        teleportationUtils.getTeleportingPlayers().remove(playerManager);
                        playerManager.teleport(safeLocation.getSafeLocation());
                        String message = ChatColor.translateAlternateColorCodes('&', Lang.TELEPORT.getMessage().replaceAll("%location%", "x: " + safeLocation.getSafeLocation().getBlockX() + " y: " + safeLocation.getSafeLocation().getBlockY() + " z: " + safeLocation.getSafeLocation().getBlockZ()));
                        playerManager.sendMessage(message);
                    }
                }
            }.runTaskLater(JavaPlugin.getPlugin(SatisRandomTP.class), teleportationUtils.getDelayPerLevel(lvl) * 20L);

            teleportingPlayers.add(playerManager);
        }
    }

    public static BukkitTask task(){
        return task;
    }

    private boolean checkTeleportationConditions(PlayerManager playerManager, int lvl){
        UUID playerUUID = playerManager.getUuid();
        long lastTeleportTime = lastTeleportTimes.getOrDefault(playerUUID, 0L);
        long elapsedSinceLastTeleport = System.currentTimeMillis() - lastTeleportTime;

        if(elapsedSinceLastTeleport < COOLDOWN_PERIOD){
            long remainingCooldown = COOLDOWN_PERIOD - elapsedSinceLastTeleport;
            long remainingMinutes = TimeUnit.MILLISECONDS.toMinutes(remainingCooldown);
            String message = String.format(Lang.COOLDOWN.getMessage(), remainingMinutes);
            playerManager.sendMessage(message);
            return false;
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
        String permission = Permissions.COOLDOWN.toString();
        String finalPermission = permission.replace("{lvl}", String.valueOf(lvl));
        if(!playerManager.hasPermission(finalPermission)){
            lastTeleportTimes.put(playerUUID, System.currentTimeMillis());
        }
        return true;
    }
}
