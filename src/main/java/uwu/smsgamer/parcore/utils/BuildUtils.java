package uwu.smsgamer.parcore.utils;

import com.sk89q.worldedit.Vector;
import org.bukkit.*;
import uwu.smsgamer.parcore.Vars;

public class BuildUtils {
    public static void setupArena(World world, Vector min, Vector max) {
        final int maxX = Math.abs(min.getBlockX() - max.getBlockX());
        final int maxZ = Math.abs(min.getBlockZ() - max.getBlockZ());
        for (int y = 0; y < 256; y++) {
            for (int x = 0; x <= maxX; x++) {
                for (int z = 0; z <= maxZ; z++) {
                    int fx = min.getBlockX() + x;
                    int fy = y;
                    int fz = min.getBlockZ() + z;
                    //ThreadUtils.syncExec(() ->
                    world.getBlockAt(fx, fy, fz).setType((x == 0 || z == 0 || x == maxX || z == maxZ) ? Vars.wallMaterial : Material.AIR);
                    //);
                }
            }
        }
    }
}
