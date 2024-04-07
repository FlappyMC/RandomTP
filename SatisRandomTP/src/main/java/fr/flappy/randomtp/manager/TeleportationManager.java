package fr.flappy.randomtp.manager;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import fr.flappy.randomtp.SatisRandomTP;
import fr.flappy.randomtp.utils.Lang;
import fr.flappy.randomtp.utils.TeleportationUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class TeleportationManager {
    private final Set<PlayerManager> teleportingPlayers = new HashSet<>();
    private final Random random = new Random();
    private final TeleportationUtils teleportationUtils = new TeleportationUtils();
    private static final int max_tries = 10;
    FLocation fLocation = new FLocation();
    private int lvl;

    public Location newRandomLocation() { // from x=0,Z=0
        Location currentLocation = new Location(teleportationUtils.getWorldPerLvl(lvl), 0.5, 0, 0.5);

        double angleX = random.nextDouble() * 2 * Math.PI;
        double angleZ = random.nextDouble() * 2 * Math.PI;
        double distanceX = teleportationUtils.getMinDistancePerLevel(lvl) + random.nextDouble() * (teleportationUtils.getMaxDistancePerLevel(lvl) - teleportationUtils.getMinDistancePerLevel(lvl));
        double distanceZ = teleportationUtils.getMinDistancePerLevel(lvl) + random.nextDouble() * (teleportationUtils.getMaxDistancePerLevel(lvl) - teleportationUtils.getMinDistancePerLevel(lvl));

        int x = currentLocation.getBlockX() + (int) (distanceX * Math.cos(angleX));
        int z = currentLocation.getBlockZ() + (int) (distanceZ * Math.cos(angleZ));
        int y = teleportationUtils.getWorldPerLvl(lvl).getHighestBlockYAt(x, z);

        return new Location(teleportationUtils.getWorldPerLvl(lvl), x + 0.5, y, z + 0.5);
    }

    public void instantTeleportation(PlayerManager playerManager){
        Location location = newRandomLocation();
        if (findingSafeLocation(location)) {
            playerManager.teleport(location);
            String teleport = Lang.TELEPORT.getMessage().replace("{x}", String.valueOf(location.getBlockX())).replace("{y}", String.valueOf(location.getBlockY())).replace("{Z}", String.valueOf(location.getBlockZ()));
            playerManager.sendMessage(teleport);
        }

    }

    public void startTeleportationTask(PlayerManager playerManager, int lvl){
        String willTeleport = Lang.WILL.getMessage().replace("{delay}", String.valueOf(teleportationUtils.getDelayPerLevel(lvl)));
        if(!teleportationUtils.isLevelsEnabled()){
            Lang.DISABLED.send(playerManager);
            return;
        }
        if(lvl == 1){
            this.lvl = 1;
            if(!playerManager.hasPermission("randomtp.lvl1")){
                Lang.PERMISSIONS.send(playerManager);
                return;
            }
            if (!teleportationUtils.isLevelsEnabled()) {
                Lang.USE.send(playerManager);
                return;
            }
        }
        if(lvl == 2){
            this.lvl = 2;
            if(!playerManager.hasPermission("randomtp.lvl2")){
                Lang.PERMISSIONS.send(playerManager);
                return;
            }
            if (!teleportationUtils.isLevelsEnabled()) {
                Lang.USE.send(playerManager);
                return;
            }
        }

        if(lvl == 3){
            this.lvl = 3;
            if(!playerManager.hasPermission("randomtp.lvl3")){
                Lang.PERMISSIONS.send(playerManager);
                return;
            }
            if (!teleportationUtils.isLevelsEnabled()) {
                Lang.USE.send(playerManager);
                return;
            }
        }

        if(lvl > 3){
            Lang.USE.send(playerManager);
            return;
        }

        playerManager.sendMessage(Lang.PREFIX.getMessage() + willTeleport);

        if(teleportingPlayers.contains(playerManager)){
            Lang.ALREADY_TELEPORTING.send(playerManager);
            return;
        }
        teleportingPlayers.add(playerManager);

        new BukkitRunnable() {
            int tries = 0;

            @Override
            public void run() {
                Location location = newRandomLocation();
                if (findingSafeLocation(location)) {
                    location.getChunk().load(true);
                    playerManager.teleport(location);
                    String teleport = Lang.TELEPORT.getMessage().replace("{location}", "X: " + location.getBlockX() + " Y: " + location.getBlockY() + " Z: " + location.getBlockZ());
                    playerManager.sendMessage(Lang.PREFIX.getMessage() + teleport);
                    teleportingPlayers.remove(playerManager);
                    cancel();
                } else {
                    tries++;
                    if (tries >= max_tries) {
                        Lang.CANCELED.send(playerManager);
                        cancel();
                        teleportingPlayers.remove(playerManager);
                    } else {
                        Lang.FINDING.send(playerManager);
                    }
                }
            }
        }.runTaskTimer(JavaPlugin.getPlugin(SatisRandomTP.class), 20L * teleportationUtils.getDelayPerLevel(lvl), 20L * teleportationUtils.getFindingSafeLocationDelayPerLevel(lvl));
    }

    public boolean findingSafeLocation(Location location){
        Block block = location.getBlock();
        Block above = block.getRelative(BlockFace.UP);
        Block below = block.getRelative(BlockFace.DOWN);
        fLocation = new FLocation(location);
        Faction faction = Board.getInstance().getFactionAt(fLocation);

        if (!below.getType().isSolid() || below.getType().isTransparent() || above.getType().isSolid()) {
            return false;
        }

        if(faction.isWilderness()){
            return block.getType() == Material.AIR && above.getType() == Material.AIR;
        }
        return false;
    }

    public void disable(){
        teleportationUtils.disableTeleportation();
    }

    public void enable(){
        teleportationUtils.enableTeleportation();
    }
}
