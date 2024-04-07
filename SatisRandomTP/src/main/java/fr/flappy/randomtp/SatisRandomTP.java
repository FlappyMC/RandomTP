package fr.flappy.randomtp;

import fr.flappy.randomtp.commands.RandomTP;
import fr.flappy.randomtp.configurations.Config;
import fr.flappy.randomtp.manager.TeleportationManager;
import fr.flappy.randomtp.utils.Lang;
import fr.flappy.randomtp.utils.Permissions;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class SatisRandomTP extends JavaPlugin {
    private TeleportationManager teleportationManager;
    private Config config;

    @Override
    public void onEnable() {
        config = new Config("config");
        Lang.load(this);
        Permissions.load();
        teleportationManager = new TeleportationManager();

        PluginCommand randomTeleportCommand = getCommand("randomteleport");
        if (randomTeleportCommand != null) {
            randomTeleportCommand.setExecutor(new RandomTP());
        }

        getLogger().info("Plugin enabled");
    }

    @Override
    public void onDisable() {
        config.save();
        getLogger().info("Plugin disabled");
    }

    public Config getRtpConfig() {
        return config;
    }

    public TeleportationManager getTeleportationManager() {
        return teleportationManager;
    }










//    private static SatisRandomTP instance;
//    private TeleportationManager teleportationManager;
//    private Config config;
//
//    @Override
//    public void onEnable() {
//        instance = this;
//        config = new Config("config");
//        Lang.load(this);
//        Permissions.load();
//        teleportationManager = new TeleportationManager();
//
//        getCommand("randomteleport").setExecutor(new RandomTP());
//        getLogger().info("Plugin enabled");
//    }
//
//    @Override
//    public void onDisable() {
//        config.save();
//        getLogger().info("Plugin disabled");
//    }
//
//    public Config getRtpConfig() {
//        return config;
//    }
//
//    public TeleportationManager getTeleportationManager() {
//        return teleportationManager;
//    }
//
//    public static SatisRandomTP getInstance() {
//        return instance;
//    }
}
