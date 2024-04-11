package fr.flappy.randomtp.commands;

import fr.flappy.randomtp.SatisRandomTP;
import fr.flappy.randomtp.manager.PlayerManager;
import fr.flappy.randomtp.utils.Lang;
import fr.flappy.randomtp.utils.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class RandomTP implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("randomteleport")){
            if(!(sender instanceof Player)){
                Lang.PERMISSIONS.send(sender);
                return true;
            }
            PlayerManager playerManager = new PlayerManager((Player) sender);
            if(args.length == 0){
                JavaPlugin.getPlugin(SatisRandomTP.class).getTeleportationManager().startTeleportationTask(playerManager, 1);
                return true;
            }else if(args.length < 2){
                if(args[0].equalsIgnoreCase("help")){
                    Lang.USE.sendAll(playerManager);
                    return true;
                }
                if(args[0].equalsIgnoreCase("reload")){
                    if(Permissions.RELOAD.check(playerManager)){
                        JavaPlugin.getPlugin(SatisRandomTP.class).getRtpConfig().save();
                        JavaPlugin.getPlugin(SatisRandomTP.class).getRtpConfig().reload();
                        Lang.reload();
                        Permissions.load();
                        Lang.RELOAD.send(playerManager);
                        return true;
                    }

                    Lang.PERMISSIONS.send(playerManager);
                    return true;
                }
                if(args[0].equalsIgnoreCase("disable")){
                    if(Permissions.DISABLE.check(playerManager)){
                        JavaPlugin.getPlugin(SatisRandomTP.class).getTeleportationManager().disable();
                        Lang.DISABLED.send(playerManager);
                        return true;
                    }
                    Lang.PERMISSIONS.send(playerManager);
                    return true;
                }
                if(args[0].equalsIgnoreCase("enable")){
                    if(Permissions.ENABLE.check(playerManager)){
                        JavaPlugin.getPlugin(SatisRandomTP.class).getTeleportationManager().enable();
                        Lang.ENABLED.send(playerManager);
                        return true;
                    }
                    Lang.PERMISSIONS.send(playerManager);
                    return true;
                }
                if(args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("3")){
                    if(Permissions.LVL.checkPerLevel(playerManager,  Integer.parseInt(args[0]))){
                        JavaPlugin.getPlugin(SatisRandomTP.class).getTeleportationManager().startTeleportationTask(playerManager, Integer.parseInt(args[0]));
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
