package fr.flappy.randomtp.manager;

import fr.flappy.randomtp.SatisRandomTP;
import fr.flappy.randomtp.teleportation.TeleportPlayer;
import fr.flappy.randomtp.utils.Lang;
import fr.flappy.randomtp.utils.TeleportationUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class TeleportationManager {
    private final Set<PlayerManager> teleportingPlayers = new HashSet<>();
    private final TeleportationUtils teleportationUtils = JavaPlugin.getPlugin(SatisRandomTP.class).getTeleportationUtils();

    public void cancelTeleportation(PlayerManager playerManager){
        teleportingPlayers.remove(playerManager);
        TeleportPlayer.getTeleportingPlayers().get(playerManager).cancel();
        Lang.MOVED.send(playerManager);
    }

    public void startTeleportationTask(PlayerManager playerManager, int lvl){
        TeleportPlayer teleportPlayer = new TeleportPlayer(playerManager, lvl);
    }

    public Set<PlayerManager> getTeleportingPlayers() {
        return teleportingPlayers;
    }

    public void disable(){
        teleportationUtils.disableTeleportation();
    }

    public void enable(){
        teleportationUtils.enableTeleportation();
    }
}
