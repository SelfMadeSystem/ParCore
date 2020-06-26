package uwu.smsgamer.parcore.utils;

import com.sk89q.worldedit.Vector;
import org.bukkit.*;

import java.util.*;

/**
 * Really just sets up an arena.
 */
public class BuildUtils {
    /**
     * All it really does is setup walls & makes air inside.
     *
     * @param walls Material walls are made of.
     * @param world The world to do this in.
     * @param min The minimum.
     * @param max The maximum.
     */
    public static void setupArena(Material walls, World world, Vector min, Vector max, boolean... noAir) {
        final int maxX = Math.abs(min.getBlockX() - max.getBlockX()); //Gets the maxX value from subtracting the minBlockX & maxBlockX
        final int maxZ = Math.abs(min.getBlockZ() - max.getBlockZ()); //Gets the maxZ value from subtracting the minBlockZ & maxBlockZ
        final boolean airnt = noAir.length != 0 && noAir[0];
        for (int y = 0; y < 256; y++) { //For every block from 0-256
            for (int x = 0; x <= maxX; x++) { //For every block from 0-maxX
                for (int z = 0; z <= maxZ; z++) { //For every block from 0-maxZ
                    int fx = min.getBlockX() + x; //Gets x location in world that this block will be.
                    int fz = min.getBlockZ() + z; //Gets z location in world that this block will be.
                    //ThreadUtils.syncExec(() ->
                    if ((x == 0 || z == 0 || x == maxX || z == maxZ))
                        world.getBlockAt(fx, y, fz).setType(walls);
                    else if (!airnt)
                        world.getBlockAt(fx, y, fz).setType(Material.AIR);
                    //Places the wall material if the x or z value are 0 or equal to their max. Places air otherwise.
                    //);
                }
            }
        }
    }

    public static List<Vector>[] getAllMaterialLocations(List<Material> mats, World world, Vector min, Vector max) {
        final int maxX = Math.abs(min.getBlockX() - max.getBlockX()); //Gets the maxX value from subtracting the minBlockX & maxBlockX
        final int maxZ = Math.abs(min.getBlockZ() - max.getBlockZ()); //Gets the maxZ value from subtracting the minBlockZ & maxBlockZ
        ArrayList<Vector>[] replacements = new ArrayList[]{new ArrayList<>(), new ArrayList<>()};
        for (int y = 0; y < 256; y++) { //For every block from 0-256
            for (int x = 0; x <= maxX; x++) { //For every block from 0-maxX
                for (int z = 0; z <= maxZ; z++) { //For every block from 0-maxZ
                    int fx = min.getBlockX() + x; //Gets x location in world that this block will be.
                    int fz = min.getBlockZ() + z; //Gets z location in world that this block will be.
                    Material type = world.getBlockAt(fx, y, fz).getType();
                    if (mats.contains(type)) {
                        replacements[mats.indexOf(type)].add(new Vector(fx, y, fz));
                    }
                }
            }
        }
        return replacements;
    }

    /**
     * Replaces the key with the value
     * of each entr in "repl"
     *
     * @param repl Replacements
     * @param world The world to replace in
     * @param min Min vector
     * @param max Max vector
     * @return Every replaced block.
     */
    public static List<Vector>[] replaceMaterials(Map.Entry<Material, Material>[] repl, World world, Vector min, Vector max) {
        final int maxX = Math.abs(min.getBlockX() - max.getBlockX()); //Gets the maxX value from subtracting the minBlockX & maxBlockX
        final int maxZ = Math.abs(min.getBlockZ() - max.getBlockZ()); //Gets the maxZ value from subtracting the minBlockZ & maxBlockZ
        ArrayList<Vector>[] replacements = new ArrayList[repl.length];
        for (int y = 0; y < 256; y++) { //For every block from 0-256
            for (int x = 0; x <= maxX; x++) { //For every block from 0-maxX
                for (int z = 0; z <= maxZ; z++) { //For every block from 0-maxZ
                    int fx = min.getBlockX() + x; //Gets x location in world that this block will be.
                    int fz = min.getBlockZ() + z; //Gets z location in world that this block will be.
                    for (int i = 0; i < repl.length; i++) {
                        Map.Entry<Material, Material> materialMaterialEntry = repl[i];
                        Material f = materialMaterialEntry.getKey();
                        if (world.getBlockAt(fx, y, fz).getType().equals(f)) {
                            replacements[i].add(new Vector(fx, y, fz));
                            world.getBlockAt(fx, y, fz).setType(materialMaterialEntry.getValue(), false);
                        }
                    }
                }
            }
        }
        return replacements;
    }
}
