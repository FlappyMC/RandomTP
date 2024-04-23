package fr.flappy.randomtp.commands;

import fr.flappy.randomtp.SatisRandomTP;
import fr.flappy.randomtp.manager.PlayerManager;
import fr.flappy.randomtp.teleportation.TeleportPlayer;
import fr.flappy.randomtp.utils.Lang;
import fr.flappy.randomtp.utils.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class RandomTP implements CommandExecutor {
    private final SatisRandomTP plugin = JavaPlugin.getPlugin(SatisRandomTP.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("randomteleport")) {
            if (!(sender instanceof Player)) {
                Lang.PERMISSIONS.send(sender);
                return true;
            }

            Player player = (Player) sender;
            UUID playerUUID = player.getUniqueId();
            PlayerManager playerManager = PlayerManager.getPlayerManager(playerUUID);

            if (playerManager == null) {
                playerManager = new PlayerManager(player);
            }

            if (args.length == 0) {
                new TeleportPlayer(playerManager, 1);
                return true;
            } else if (args.length < 2) {
                if (args[0].equalsIgnoreCase("help")) {
                    Lang.USE.sendAll(playerManager);
                    return true;
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    if (Permissions.RELOAD.check(playerManager)) {
                        plugin.getRtpConfig().save();
                        plugin.getRtpConfig().reload();
                        Lang.reload();
                        Permissions.load();
                        Lang.RELOAD.send(playerManager);
                        return true;
                    }

                    Lang.PERMISSIONS.send(playerManager);
                    return true;
                }
                if (args[0].equalsIgnoreCase("disable")) {
                    if (Permissions.DISABLE.check(playerManager)) {
                        plugin.getTeleportationUtils().disableTeleportation();
                        Lang.DISABLED.send(playerManager);
                        return true;
                    }
                    Lang.PERMISSIONS.send(playerManager);
                    return true;
                }
                if (args[0].equalsIgnoreCase("enable")) {
                    if (Permissions.ENABLE.check(playerManager)) {
                        plugin.getTeleportationUtils().enableTeleportation();
                        Lang.ENABLED.send(playerManager);
                        return true;
                    }
                    Lang.PERMISSIONS.send(playerManager);
                    return true;
                }
                if (args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("4") || args[0].equalsIgnoreCase("5")) {
                    if (Permissions.LVL.checkPerLevel(playerManager, Integer.parseInt(args[0]))) {
                        new TeleportPlayer(playerManager, Integer.parseInt(args[0]));
                        return true;
                    }
                    Lang.PERMISSIONS.send(playerManager);
                    return true;
                }

                Lang.USE.send(playerManager);
                return false;
            }
            Lang.USE.send(playerManager);
            return false;
        }
        return false;
    }
}
