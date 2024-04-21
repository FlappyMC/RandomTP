package fr.flappy.randomtp.listeners;

import fr.flappy.randomtp.SatisRandomTP;
import fr.flappy.randomtp.manager.PlayerManager;
import fr.flappy.randomtp.teleportation.TeleportationUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class TeleportationListener implements Listener {
    private final TeleportationUtils teleportationUtils = JavaPlugin.getPlugin(SatisRandomTP.class).getTeleportationUtils();
    private final Map<Player, PlayerManager> playerManagerMap = new HashMap<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        PlayerManager playerManager = playerManagerMap.get(event.getPlayer());
        if (playerManager != null && event.getFrom().distanceSquared(event.getTo()) > 0.04) {
            teleportationUtils.getTeleportingPlayers().remove(playerManager);
            teleportationUtils.cancelTeleportation(playerManager);
            playerManagerMap.remove(event.getPlayer());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        PlayerManager playerManager = playerManagerMap.get(event.getPlayer());
        if (playerManager != null) {
            teleportationUtils.getTeleportingPlayers().remove(playerManager);
            playerManagerMap.remove(event.getPlayer());
        }
    }
}
