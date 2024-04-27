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
    COOLDOWN("permissions.cooldown-{level}"),
    INSTANT("permissions.instant-tp");

    private final String key;
    private String permission;

    Permissions(String key) {this.key = key;}

    private static SatisRandomTP instance;
    public static void load(SatisRandomTP instance){
        Permissions.instance = instance;
        reload();
    }

    public static void reload(){
        final Config config = JavaPlugin.getPlugin(SatisRandomTP.class).getRtpConfig();
        for (Permissions permissions : Permissions.values()){
            permissions.permission = config.options().getString(permissions.key);
        }
    }

    public boolean checkLvlPerLevel(PlayerManager playerManager, int lvl)  {
        final String replace = LVL.key.replace("{level}", String.valueOf(lvl));
        LVL.permission = replace;
        return playerManager.hasPermission(replace);
    }

    public boolean check(PlayerManager playerManager){
        return playerManager.hasPermission(permission);
    }
}
