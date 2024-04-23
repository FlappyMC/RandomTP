package fr.flappy.randomtp.listeners;

import fr.flappy.randomtp.SatisRandomTP;
import fr.flappy.randomtp.manager.PlayerManager;
import fr.flappy.randomtp.teleportation.TeleportationUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Set;

public class TeleportationListener implements Listener {
    private final TeleportationUtils teleportationUtils = JavaPlugin.getPlugin(SatisRandomTP.class).getTeleportationUtils();
    private final Set<PlayerManager> teleportingPlayers = teleportationUtils.getTeleportingPlayers();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        for(PlayerManager playerManager : teleportingPlayers){
            if (playerManager != null) {
                if (event.getFrom().distanceSquared(event.getTo()) > 0.04) {
                    teleportationUtils.getTeleportingPlayers().remove(playerManager);
                    teleportationUtils.cancelTeleportation(playerManager);
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        for(PlayerManager playerManager : teleportingPlayers){
            if(playerManager == event.getPlayer()){
                teleportingPlayers.remove(playerManager);
                break;
            }
        }
    }
}
