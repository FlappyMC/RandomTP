package fr.flappy.randomtp.listeners;

import fr.flappy.randomtp.SatisRandomTP;
import fr.flappy.randomtp.manager.PlayerManager;
import fr.flappy.randomtp.teleportation.TeleportationUtils;
import fr.flappy.randomtp.utils.Lang;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Map;

public class TeleportationListener implements Listener {
    private final TeleportationUtils teleportationUtils = JavaPlugin.getPlugin(SatisRandomTP.class).getTeleportationUtils();
    private final Map<PlayerManager, Location> teleportingPlayerManagers = teleportationUtils.getTeleportingPlayerManagers();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if(!teleportingPlayerManagers.isEmpty()){
            for(PlayerManager playerManager : teleportingPlayerManagers.keySet()){
                if(playerManager.getUuid() == event.getPlayer().getUniqueId()) {
                    if (event.getFrom().distanceSquared(event.getTo()) > 0.04) {
                        teleportationUtils.clearPlayerManagersData(playerManager);
                        Lang.MOVED.send(playerManager);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        teleportationUtils.clearPlayerManagersData(PlayerManager.getPlayerManager(event.getPlayer().getUniqueId()));
    }
}
