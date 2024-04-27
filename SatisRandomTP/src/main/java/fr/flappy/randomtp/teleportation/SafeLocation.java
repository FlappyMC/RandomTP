package fr.flappy.randomtp.teleportation;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import fr.flappy.randomtp.SatisRandomTP;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;

public class SafeLocation {
    private final TeleportationUtils teleportationUtils = JavaPlugin.getPlugin(SatisRandomTP.class).getTeleportationUtils();
    private final BukkitTask task;
    private Location safeLocation = null;
    private final Random random = new Random();

    public SafeLocation(int lvl) {
        task = new BukkitRunnable() {
            @Override
            public void run() {
                if(safeLocation != null){
                    safeLocation.getChunk().load(true);
                    teleportationUtils.getTasks().remove(task);
                    cancel();
                    return;
                }
                searchSafeLocation(lvl);
            }
        }.runTaskTimer(JavaPlugin.getPlugin(SatisRandomTP.class), 0, 5L);
        teleportationUtils.getTasks().put(null, task);
    }

    public void searchSafeLocation(int lvl){
        if (safeLocation != null) return;

        World world = teleportationUtils.getWorldPerLvl(lvl);
        double minDistance = teleportationUtils.getMinDistancePerLevel(lvl);
        double maxDistance = teleportationUtils.getMaxDistancePerLevel(lvl);

        safeLocation = new Location(world, 0.5, 0, 0.5);

        double angleX = random.nextDouble() * 2 * Math.PI;
        double angleZ = random.nextDouble() * 2 * Math.PI;
        double distanceX = minDistance + random.nextDouble() * (maxDistance - minDistance);
        double distanceZ = minDistance + random.nextDouble() * (maxDistance - minDistance);

        int x = safeLocation.getBlockX() + (int) (distanceX * Math.cos(angleX));
        int z = safeLocation.getBlockZ() + (int) (distanceZ * Math.cos(angleZ));
        int y = world.getHighestBlockYAt(x, z);

        safeLocation.setX(x);
        safeLocation.setY(y);
        safeLocation.setZ(z);

        Block block = safeLocation.getBlock();
        Block above = block.getRelative(BlockFace.UP);
        Block below = block.getRelative(BlockFace.DOWN);
        FLocation fLocation = new FLocation(safeLocation);
        Faction faction = Board.getInstance().getFactionAt(fLocation);

        if (!below.getType().isSolid() || below.getType().isTransparent() || above.getType().isSolid()) {
            safeLocation = null;
            return;
        }
        if(faction.isWilderness()){
            safeLocation.setX(x + 0.5);
            safeLocation.setY(y);
            safeLocation.setZ(z + 0.5);
        } else {
            safeLocation = null;
        }
    }

    public Location getSafeLocation() {
        return safeLocation;
    }
}
