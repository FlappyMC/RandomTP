package fr.flappy.randomtp.utils;

import fr.flappy.randomtp.SatisRandomTP;
import fr.flappy.randomtp.configurations.Config;
import fr.flappy.randomtp.manager.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public enum Lang {
    PREFIX("messages.prefix"),
    USE("messages.use"),
    DISABLED("messages.disabled"),
    ENABLED("messages.enabled"),
    ERROR("messages.error"),
    CANCELED("messages.canceled"),
    TELEPORT("messages.teleport"),
    IN_PROGRESS("messages.in-progress"),
    RELOAD("messages.reload"),
    WILL("messages.will-teleport"),
    PERMISSIONS("messages.permissions"),
    ALREADY_TELEPORTING("messages.already-teleporting"),
    MOVED("messages.moved"),
    FINDING("messages.finding-safe-location"),
    COOLDOWN("messages.cooldown");

    private final String key;
    private String message;

    Lang(String key) {
        this.key = key;
    }

    private static SatisRandomTP instance;

    public static void load(SatisRandomTP instance){
        Lang.instance = instance;
        reload();
    }

    public static void reload(){
        final Config config = JavaPlugin.getPlugin(SatisRandomTP.class).getRtpConfig();
        for (Lang lang : Lang.values()){
            lang.message = config.options().getString(lang.key);
        }
    }

    public String getMessage(){
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public void sendAll(PlayerManager player){
        List<String> list = JavaPlugin.getPlugin(SatisRandomTP.class).getRtpConfig().options().getStringList("messages.help");
        for(String string : list){
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', string));
        }
    }

    public void send(PlayerManager player){
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', PREFIX.getMessage()) + ChatColor.translateAlternateColorCodes('&', message));
    }

    public void send(CommandSender sender){
        sender.sendMessage(PREFIX.getMessage() + message);
    }
}
