package fr.flappy.randomtp.manager;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerManager {
    private final Player player;
    private final UUID uuid;
    private final String username;

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof PlayerManager){
            PlayerManager playerManager = (PlayerManager) obj;
            return playerManager.getUuid().equals(uuid);
        }
        return false;
    }

    @Override
    public int hashCode(){
        return Objects.hash(uuid);
    }

    public PlayerManager(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.username = player.getName();
    }

    public Player getPlayer() {
        return player;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public void sendMessage(String message){
        player.sendMessage(message);
    }

    public boolean hasPermission(String permission){
        return player.hasPermission(permission);
    }

    public Location getLocation(){
        return player.getLocation();
    }

    public void teleport(Location location){
        player.teleport(location);
    }
}
