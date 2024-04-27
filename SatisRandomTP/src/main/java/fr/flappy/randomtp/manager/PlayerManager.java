package fr.flappy.randomtp.manager;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerManager {
    private static final Map<UUID, PlayerManager> playerManagers = new HashMap<>();
    private final Player player;
    private final UUID uuid;

    public PlayerManager(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
    }

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

    public static Map<UUID, PlayerManager> getPlayerManagers() {
        return playerManagers;
    }

    public static PlayerManager getPlayerManager(UUID uuid){
        return playerManagers.get(uuid);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void sendMessage(String message){
        player.sendMessage(message);
    }

    public boolean hasPermission(String permission){
        return player.hasPermission(permission);
    }

    public void teleport(Location location){
        player.teleport(location);
    }
}
