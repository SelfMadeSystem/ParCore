package uwu.smsgamer.parcore.utils;

import org.bukkit.*;
import org.bukkit.util.Vector;

/**
 * Mainly just transformers of different types of location holders.
 * Method names are self explanatory and don't need javadoc.
 */
public class VectorUtils {
    public static Vector toBukkitVector(com.sk89q.worldedit.Vector vector) {
        return new Vector(vector.getX(), vector.getY(), vector.getZ());
    }

    public static com.sk89q.worldedit.Vector toWEVector(Vector vector) {
        return new com.sk89q.worldedit.Vector(vector.getX(), vector.getY(), vector.getZ());
    }

    public static com.sk89q.worldedit.Vector toWEVector(Location location) {
        return new com.sk89q.worldedit.Vector(location.getX(), location.getY(), location.getZ());
    }

    public static Location toLocation(World world, com.sk89q.worldedit.Vector vector) {
        return new Location(world, vector.getX(), vector.getY(), vector.getZ());
    }

    public static Location toLocation(World world, Vector vector) {
        return new Location(world, vector.getX(), vector.getY(), vector.getZ());
    }
}
