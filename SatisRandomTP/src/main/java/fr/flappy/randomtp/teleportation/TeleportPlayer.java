package fr.flappy.randomtp.teleportation;

import fr.flappy.randomtp.SatisRandomTP;
import fr.flappy.randomtp.manager.PlayerManager;
import fr.flappy.randomtp.manager.TeleportationManager;
import fr.flappy.randomtp.utils.Lang;
import fr.flappy.randomtp.utils.TeleportationUtils;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class TeleportPlayer {
    SatisRandomTP plugin = JavaPlugin.getPlugin(SatisRandomTP.class);
    TeleportationUtils teleportationUtils = JavaPlugin.getPlugin(SatisRandomTP.class).getTeleportationUtils();
    public static Map<PlayerManager, BukkitTask> teleportingPlayers = new HashMap<>();

    public TeleportPlayer(PlayerManager playerManager, int lvl){
        if(checkTeleportationConditions(playerManager, lvl)){
            plugin.getTeleportationManager().getTeleportingPlayers().add(playerManager);
            SafeLocation safeLocation = new SafeLocation(lvl);
            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    if(safeLocation.getSafeLocation() != null){
                        plugin.getTeleportationManager().getTeleportingPlayers().remove(playerManager);
                        playerManager.teleport(safeLocation.getSafeLocation());
                        String message = ChatColor.translateAlternateColorCodes('&', Lang.TELEPORT.getMessage().replaceAll("%location%", "x: " + safeLocation.getSafeLocation().getBlockX() + " y: " + safeLocation.getSafeLocation().getBlockY() + " z: " + safeLocation.getSafeLocation().getBlockZ()));
                        playerManager.sendMessage(message);
                    }
                }
            }.runTaskLater(plugin, teleportationUtils.getDelayPerLevel(lvl) * 20L);

            teleportingPlayers.put(playerManager, task);
        }
    }

    private boolean checkTeleportationConditions(PlayerManager playerManager, int lvl){
        if(plugin.getTeleportationManager().getTeleportingPlayers().contains(playerManager)){
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
        plugin.getTeleportationManager().getTeleportingPlayers().add(playerManager);
        String willTeleport = Lang.WILL.getMessage().replace("{delay}", String.valueOf(teleportationUtils.getDelayPerLevel(lvl)));
        playerManager.sendMessage(Lang.PREFIX.getMessage() + willTeleport);
        return true;
    }

    public static Map<PlayerManager, BukkitTask> getTeleportingPlayers() {
        return teleportingPlayers;
    }
}
