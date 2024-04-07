package fr.flappy.randomtp.utils;

import fr.flappy.randomtp.SatisRandomTP;
import fr.flappy.randomtp.configurations.Config;
import fr.flappy.randomtp.manager.PlayerManager;
import org.bukkit.plugin.java.JavaPlugin;

public enum Permissions {
    PREFIX("messages.prefix"),
    USE("permissions.use"),
    RELOAD("permissions.reload"),
    DISABLE("permissions.disable"),
    ENABLE("permissions.enable"),
    LVL("permissions.level-{level}"),
    INSTANT("permissions.instant-tp");

    private final String key;
    private String permission;

    Permissions(String key) {this.key = key;}

    public static void load(){
        final Config config = JavaPlugin.getPlugin(SatisRandomTP.class).getRtpConfig();
        for (Permissions lang : Permissions.values()){
            lang.permission = config.options().getString(lang.key);
        }
    }

    public boolean checkPerLevel(PlayerManager playerManager, int lvl)  {
        return playerManager.hasPermission(LVL.key.replace("{level}", lvl + ""));
    }
    public boolean check(PlayerManager playerManager){
        return playerManager.hasPermission(permission);
    }
}
