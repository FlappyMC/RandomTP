package fr.flappy.randomtp.teleportation;

import fr.flappy.randomtp.SatisRandomTP;
import fr.flappy.randomtp.configurations.Config;
import fr.flappy.randomtp.manager.PlayerManager;
import fr.flappy.randomtp.utils.Lang;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public class TeleportationUtils {
    private final Config config;
    private final Set<PlayerManager> teleportingPlayers = new HashSet<>();

    public void cancelTeleportation(PlayerManager playerManager){
        teleportingPlayers.remove(playerManager);
        TeleportPlayer.getTeleportingPlayers().get(playerManager).cancel();
        Lang.MOVED.send(playerManager);
    }

    public Set<PlayerManager> getTeleportingPlayers() {
        return teleportingPlayers;
    }

    public TeleportationUtils() {
        config = JavaPlugin.getPlugin(SatisRandomTP.class).getRtpConfig();
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
