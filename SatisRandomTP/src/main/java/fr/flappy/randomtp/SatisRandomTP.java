package fr.flappy.randomtp;

import fr.flappy.randomtp.commands.RandomTP;
import fr.flappy.randomtp.configurations.Config;
import fr.flappy.randomtp.listeners.TeleportationListener;
import fr.flappy.randomtp.manager.TeleportationManager;
import fr.flappy.randomtp.utils.Lang;
import fr.flappy.randomtp.utils.Permissions;
import fr.flappy.randomtp.utils.TeleportationUtils;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class SatisRandomTP extends JavaPlugin {
    private TeleportationManager teleportationManager;
    private TeleportationUtils teleportationUtils;
    private Config config;

    @Override
    public void onEnable() {
        config = new Config("config");
        Lang.load(this);
        Permissions.load();
        teleportationUtils = new TeleportationUtils();
        teleportationManager = new TeleportationManager();

        PluginCommand randomTeleportCommand = getCommand("randomteleport");
        if (randomTeleportCommand != null) {
            randomTeleportCommand.setExecutor(new RandomTP());
        }

        getServer().getPluginManager().registerEvents(new TeleportationListener(), this);
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

    public TeleportationUtils getTeleportationUtils() {
        return teleportationUtils;
    }

    public TeleportationManager getTeleportationManager() {
        return teleportationManager;
    }
}
