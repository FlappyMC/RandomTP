package fr.flappy.randomtp;

import fr.flappy.randomtp.commands.RandomTP;
import fr.flappy.randomtp.configurations.Config;
import fr.flappy.randomtp.listeners.TeleportationListener;
import fr.flappy.randomtp.manager.PlayerManager;
import fr.flappy.randomtp.teleportation.TeleportPlayer;
import fr.flappy.randomtp.utils.Lang;
import fr.flappy.randomtp.utils.Permissions;
import fr.flappy.randomtp.teleportation.TeleportationUtils;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class SatisRandomTP extends JavaPlugin {
    private TeleportationUtils teleportationUtils;
    private Config config;
    private RandomTP randomTP;
    private TeleportPlayer teleportPlayer;

    @Override
    public void onEnable() {
        getLogger().info("SatisRandomTP loading...");
        config = new Config("config");
        Lang.load(this);
        Permissions.load(this);
        teleportationUtils = new TeleportationUtils();
        teleportPlayer = new TeleportPlayer(this);

        randomTP = new RandomTP();
        PluginCommand command = getCommand("randomteleport");
        if (command != null) {
            command.setExecutor(randomTP);
        }

        getServer().getPluginManager().registerEvents(new TeleportationListener(), this);
        getLogger().info("SatisRandomTP loaded.");
    }

    @Override
    public void onDisable() {
        getLogger().info("SatisRandomTP unloading...");
        teleportationUtils.clearAllTasks();
        teleportationUtils.clearAllPlayerManagersData();
        PlayerManager.getPlayerManagers().clear();
        config.save();
        getLogger().info("SatisRandomTP unloaded.");
    }

    public TeleportPlayer getTeleportPlayer() {
        return teleportPlayer;
    }

    public Config getRtpConfig() {
        return config;
    }

    public TeleportationUtils getTeleportationUtils() {
        return teleportationUtils;
    }
}
