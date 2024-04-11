package fr.flappy.randomtp.teleportation;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import fr.flappy.randomtp.SatisRandomTP;
import fr.flappy.randomtp.manager.PlayerManager;
import fr.flappy.randomtp.utils.TeleportationUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.Random;

public class SafeLocation {
    private final TeleportationUtils teleportationUtils = JavaPlugin.getPlugin(SatisRandomTP.class).getTeleportationUtils();
    private Location safeLocation;

    public SafeLocation(PlayerManager playerManager, int lvl) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(safeLocation != null){
                    cancel();
                }
                searchSafeLocation(lvl);
            }
        }.runTaskTimer(JavaPlugin.getPlugin(SatisRandomTP.class), 0, 5L);
    }

    public void searchSafeLocation(int lvl){
        Location currentLocation = new Location(teleportationUtils.getWorldPerLvl(lvl), 0.5, 0, 0.5);

        Random random = new Random();
        double angleX = random.nextDouble() * 2 * Math.PI;
        double angleZ = random.nextDouble() * 2 * Math.PI;
        double distanceX = teleportationUtils.getMinDistancePerLevel(lvl) + random.nextDouble() * (teleportationUtils.getMaxDistancePerLevel(lvl) - teleportationUtils.getMinDistancePerLevel(lvl));
        double distanceZ = teleportationUtils.getMinDistancePerLevel(lvl) + random.nextDouble() * (teleportationUtils.getMaxDistancePerLevel(lvl) - teleportationUtils.getMinDistancePerLevel(lvl));

        int x = currentLocation.getBlockX() + (int) (distanceX * Math.cos(angleX));
        int z = currentLocation.getBlockZ() + (int) (distanceZ * Math.cos(angleZ));
        int y = teleportationUtils.getWorldPerLvl(lvl).getHighestBlockYAt(x, z);

        currentLocation = new Location(teleportationUtils.getWorldPerLvl(lvl), x, y, z);

        Block block = currentLocation.getBlock();
        Block above = block.getRelative(BlockFace.UP);
        Block below = block.getRelative(BlockFace.DOWN);
        FLocation fLocation = new FLocation(currentLocation);
        Faction faction = Board.getInstance().getFactionAt(fLocation);

        if (!below.getType().isSolid() || below.getType().isTransparent() || above.getType().isSolid()) {
            System.out.println("Condition 1 not met: The block below is not solid, is transparent, or the block above is solid.");
            return;
        }
        if (!below.getType().isSolid() || below.getType().isTransparent() || above.getType().isSolid()) {
            System.out.println("Condition 2 not met: The block below is not solid, is transparent, or the block above is solid.");
            return;
        }
        if(faction.isWilderness()){
            safeLocation = new Location(teleportationUtils.getWorldPerLvl(lvl), x + 0.5, y, z + 0.5);
        }else{
            System.out.println("Condition 3 not met: The block below is not solid, is transparent, or the block above is solid.");
        }
    }

    public Location getSafeLocation() {
        return safeLocation;
    }
}
