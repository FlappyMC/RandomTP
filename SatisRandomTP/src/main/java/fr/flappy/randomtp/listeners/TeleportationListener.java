package fr.flappy.randomtp.listeners;

import fr.flappy.randomtp.SatisRandomTP;
import fr.flappy.randomtp.manager.PlayerManager;
import fr.flappy.randomtp.manager.TeleportationManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class TeleportationListener implements Listener {
    private final TeleportationManager teleportationManager = JavaPlugin.getPlugin(SatisRandomTP.class).getTeleportationManager();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        for(PlayerManager playerManager : teleportationManager.getTeleportingPlayers()) {
            if(playerManager.getPlayer().equals(event.getPlayer())) {
                if(event.getFrom().distance(event.getTo()) > 0.2) {
                    teleportationManager.cancelTeleportation(playerManager);
                    break;
                }
            }
        }
    }
}
