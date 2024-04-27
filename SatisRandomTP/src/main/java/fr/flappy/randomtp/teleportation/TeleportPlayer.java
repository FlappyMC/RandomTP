package fr.flappy.randomtp.teleportation;

import fr.flappy.randomtp.SatisRandomTP;
import fr.flappy.randomtp.manager.PlayerManager;
import fr.flappy.randomtp.utils.Lang;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TeleportPlayer {
    private final SatisRandomTP plugin;
    private final TeleportationUtils teleportationUtils;
    private static long COOLDOWN_PERIOD = 0;
    private SafeLocation safeLocation;

    public TeleportPlayer(SatisRandomTP plugin){
        this.plugin = plugin;
        this.teleportationUtils = plugin.getTeleportationUtils();
    }

    public void teleportPlayer(PlayerManager playerManager, int lvl){
        COOLDOWN_PERIOD = TimeUnit.MINUTES.toMillis(teleportationUtils.getCooldownPerLvl(lvl));
        if(teleportationUtils.getSafeLocations().containsKey(playerManager)) {
            safeLocation = teleportationUtils.getSafeLocations().get(playerManager);
        }

        safeLocation = new SafeLocation(lvl);
        teleportationUtils.getSafeLocations().put(playerManager, safeLocation);

        if(checkTeleportationConditions(playerManager, lvl)){
            teleportationUtils.getTeleportingPlayerManagers().put(playerManager, safeLocation.getSafeLocation());
            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    if (safeLocation.getSafeLocation() != null) {
                        teleportationUtils.getTeleportingPlayerManagers().remove(playerManager);
                        playerManager.teleport(teleportationUtils.getSafeLocations().get(playerManager).getSafeLocation());
                        String message = ChatColor.translateAlternateColorCodes('&', Lang.TELEPORT.getMessage().replaceAll("%location%", "x: " + safeLocation.getSafeLocation().getBlockX() + " y: " + safeLocation.getSafeLocation().getBlockY() + " z: " + safeLocation.getSafeLocation().getBlockZ()));
                        playerManager.sendMessage(message);
                        teleportationUtils.getTasks().remove(playerManager);
                        cancel();
                    }
                }
            }.runTaskLater(plugin, teleportationUtils.getDelayPerLevel(lvl) * 20L);
            teleportationUtils.getTasks().put(playerManager, task);
        }
    }

    private boolean checkTeleportationConditions(PlayerManager playerManager, int lvl){
        UUID playerUUID = playerManager.getUuid();
        long lastTeleportTime = teleportationUtils.getLastTeleportTimes().getOrDefault(playerUUID, 0L);
        long elapsedSinceLastTeleport = System.currentTimeMillis() - lastTeleportTime;

        if(elapsedSinceLastTeleport < COOLDOWN_PERIOD){
            if(!playerManager.hasPermission("randomtp.cooldown." + lvl)){
                long remainingCooldown = COOLDOWN_PERIOD - elapsedSinceLastTeleport;
                long remainingMinutes = TimeUnit.MILLISECONDS.toMinutes(remainingCooldown);
                String message = String.format(Lang.COOLDOWN.getMessage(), remainingMinutes);
                playerManager.sendMessage(message);
                return false;
            }
        }

        if(teleportationUtils.getTeleportingPlayerManagers().containsKey(playerManager)){
            Lang.ALREADY_TELEPORTING.send(playerManager);
            return false;
        }

        if(!teleportationUtils.isLevelsEnabled()){
            Lang.DISABLED.send(playerManager);
            return false;
        }

        teleportationUtils.getTeleportingPlayerManagers().put(playerManager, safeLocation.getSafeLocation());
        String willTeleport = Lang.WILL.getMessage().replace("%delay%", String.valueOf(teleportationUtils.getDelayPerLevel(lvl)));
        playerManager.sendMessage(Lang.PREFIX.getMessage() + willTeleport);
        if(!teleportationUtils.getLastTeleportTimes().containsKey(playerUUID)){
            teleportationUtils.getLastTeleportTimes().put(playerUUID, System.currentTimeMillis());
        }
        return true;
    }
}
