package fr.flappy.randomtp.utils;

import fr.flappy.randomtp.SatisRandomTP;
import fr.flappy.randomtp.configurations.Config;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class TeleportationUtils {
    private final Config config;

    public TeleportationUtils() {
        config = JavaPlugin.getPlugin(SatisRandomTP.class).getRtpConfig();
    }

    public void setMinDistance(int minDistance) {
        getConfig().options().set("teleportation.min_distance", minDistance);
    }

    public void setMaxDistance(int maxDistance) {
        getConfig().options().set("teleportation.max_distance", maxDistance);
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

    public World getWorldPerLvl(int lvl) {
        return Bukkit.getWorlds().get(getConfig().options().getInt("levels.level-" + lvl + ".world"));
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

    public int getFindingSafeLocationDelayPerLevel(int lvl){
        return getConfig().options().getInt("levels.level-" + lvl + ".finding-safe-location-delay");
    }

    public int getTeleportationCooldownPerLevel(int lvl) {
        return getConfig().options().getInt("teleportation.level-" + lvl + ".cooldown");
    }

    private Config getConfig() {
        return config;
    }
}
