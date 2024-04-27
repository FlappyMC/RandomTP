package fr.flappy.randomtp.teleportation;

import fr.flappy.randomtp.SatisRandomTP;
import fr.flappy.randomtp.configurations.Config;
import fr.flappy.randomtp.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class TeleportationUtils {
    private final Config config;
    private final Map<PlayerManager, BukkitTask> tasks = new HashMap<>();
    private final Map<PlayerManager, Location> teleportingPlayerManagers = new HashMap<>();
    private final Map<PlayerManager, SafeLocation> safeLocations = new HashMap<>();
    private static final Map<UUID, Long> lastTeleportTimes = new HashMap<>();

    public Map<PlayerManager, Location> getTeleportingPlayerManagers(){
        return teleportingPlayerManagers;
    }

    public void clearPlayerManagersData(PlayerManager playerManager){
        teleportingPlayerManagers.remove(playerManager);
        safeLocations.remove(playerManager);
        lastTeleportTimes.remove(playerManager.getUuid());
        if(tasks.containsKey(playerManager)){
            tasks.get(playerManager).cancel();
            tasks.remove(playerManager);
        }
    }

    public void clearAllPlayerManagersData(){
        teleportingPlayerManagers.clear();
        safeLocations.clear();
        lastTeleportTimes.clear();
        for(BukkitTask task : tasks.values()){
            task.cancel();
        }
        tasks.clear();
    }

    public Map<UUID, Long> getLastTeleportTimes() {
        return lastTeleportTimes;
    }

    public Map<PlayerManager, SafeLocation> getSafeLocations() {
        return safeLocations;
    }

    public void clearAllTasks(){
        if(tasks.isEmpty()){
            return;
        }
        tasks.forEach((playerManager, task) -> task.cancel());
        tasks.clear();
    }

    public TeleportationUtils() {
        config = JavaPlugin.getPlugin(SatisRandomTP.class).getRtpConfig();
    }

    public Map<PlayerManager, BukkitTask> getTasks() {
        return tasks;
    }

    public void enableTeleportation() {
        getConfig().options().set("teleportation.enabled", true);
        getConfig().options().set("levels.enabled", true);
    }

    public void disableTeleportation() {
        getConfig().options().set("teleportation.enabled", false);
        getConfig().options().set("levels.enabled", false);
    }

    public boolean isLevelsEnabled() {
        return getConfig().options().getBoolean("levels.enabled");
    }

    public int getCooldownPerLvl(int lvl){
        return getConfig().options().getInt("levels.level-" + lvl + ".cooldown");
    }

    public World getWorldPerLvl(int lvl) {
        return Bukkit.getWorld(getConfig().options().getString("levels.level-" + lvl + ".world"));
    }

    public double getMaxDistancePerLevel(int lvl) {
        return getConfig().options().getInt("levels.level-" + lvl + ".max-distance");
    }

    public double getMinDistancePerLevel(int lvl) {
        return getConfig().options().getInt("levels.level-" + lvl + ".min-distance");
    }

    public int getDelayPerLevel(int lvl){
        return getConfig().options().getInt("levels.level-" + lvl + ".delay");
    }

    private Config getConfig() {
        return config;
    }
}
