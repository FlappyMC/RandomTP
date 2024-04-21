package fr.flappy.randomtp;

import fr.flappy.randomtp.commands.RandomTP;
import fr.flappy.randomtp.configurations.Config;
import fr.flappy.randomtp.listeners.TeleportationListener;
import fr.flappy.randomtp.utils.Lang;
import fr.flappy.randomtp.utils.Permissions;
import fr.flappy.randomtp.teleportation.TeleportationUtils;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class SatisRandomTP extends JavaPlugin {
    private TeleportationUtils teleportationUtils;
    private Config config;
    private RandomTP randomTP;

    @Override
    public void onEnable() {
        config = new Config("config");
        Lang.load(this);
        Permissions.load();
        teleportationUtils = new TeleportationUtils();

        randomTP = new RandomTP();
        PluginCommand command = getCommand("randomteleport");
        if (command != null) {
            command.setExecutor(randomTP);
        }

        getServer().getPluginManager().registerEvents(new TeleportationListener(), this);
        getLogger().info("Plugin enabled");
    }

    @Override
    public void onDisable() {
        config.save();
        getLogger().info("Plugin disabled");
    }

    public RandomTP getRandomTP() {
        return randomTP;
    }

    public Config getRtpConfig() {
        return config;
    }

    public TeleportationUtils getTeleportationUtils() {
        return teleportationUtils;
    }
}
