package uwu.smsgamer.parcore.utils;

import com.boydti.fawe.object.schematic.Schematic;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.World;
import org.bukkit.*;
import uwu.smsgamer.parcore.managers.FileManager;

import java.io.*;
import java.util.*;

/**
 * Utils for loading & saving schematics.
 */
public class SchemUtils {
    /**
     * Saves a schematic & a yml config to go along with it
     * containing the name of the creator, the name of the map,
     * a description (unused atm), whether or not it's published
     * (also unused), the likes it got (you guessed it, unused),
     * then the x, y, & z coords of the initial spawn location
     * (this time actually used though).
     *
     * @param loc "Bottom" location to save.
     * @param x X forwards
     * @param z Z forwards
     * @param playerName Name of the player to save to.
     * @param mapName Name of the map to save to,
     * @param spawnLocation Location of the initial spawn location of this map.
     * @param wallMaterial The material of the wall. Default's air.
     * @throws IOException if can't save file.
     */
    public static void saveSchematic(Location loc, int x, int z, String playerName, String mapName, String description, Vector spawnLocation, Material wallMaterial, MapFile.MapMode mode) throws IOException {
        File file = new File(FileManager.getSchemaName(playerName, mapName)); //Gets file that the schematic is going to be saved to.
        loc.setY(0); //Sets location to 0 since we're copying from 0-255
        Vector bot = VectorUtils.toWEVector(loc); //Sets the bottom vector as WorldEdit vector.
        Vector top = new Vector(bot.getBlockX() + x, 255, bot.getBlockZ() + z); //Adds the "x" & "z" & makes "y" 255 as a new vector.
        CuboidRegion region = new CuboidRegion((World) new BukkitWorld(loc.getWorld()), bot, top); //Makes a worldedit region from those vectors & world of loc.
        Schematic schem = new Schematic(region); //Makes a schematic out of that region.
        schem.save(file, ClipboardFormat.SCHEMATIC); //Saves the schematic to that file we initialized at the beginning.
        /*YamlConfiguration config = new YamlConfiguration(); //Makes a new yaml config.
        config.set("player", playerName); //Sets "player" to the playerName
        config.set("name", mapName); //Sets "name" to the mapName
        config.set("description", ""); //Sets "description" to nothing since it's unused, but still needed to be loaded in the FileManager.
        config.set("published", false); //Sets "published" to false since it's unused, but still needed to be loaded in the FileManager.
        config.set("likes", new ArrayList<Boolean>()); //Sets "likes" to an empty array since it's unused, but still needed to be loaded in the FileManager.
        config.set("x", spawnLocation.getBlockX() + 0.5); //Sets "x" to the block position + 0.5 of spawnLocation.
        config.set("y", spawnLocation.getBlockY()); //Sets "y" to the block position of spawnLocation.
        config.set("z", spawnLocation.getBlockZ() + 0.5); //Sets "z" to the block position + 0.5 of spawnLocation.
        config.set("wallMaterial", wallMaterial.name()); //Sets "wallMaterial" to the wall material. (:
        config.save(new File(FileManager.getYamlMapName(playerName, mapName))); //And finally, we save the config file.*/
        MapFile mf = FileManager.getMapFile(playerName, mapName); //mf doesn't stand for mother fucker
        mf.setPlayer(playerName);
        mf.setName(mapName);
        mf.setDescription(description);
        mf.setMode(mode);
        mf.setLikes(new HashSet<>());
        mf.setSpawnLocation(new org.bukkit.util.Vector(spawnLocation.getBlockX() + 0.5, spawnLocation.getBlockY(), spawnLocation.getBlockZ() + 0.5));
        mf.setWallMaterial(wallMaterial);
        mf.setPublished(false);
        mf.setVerified(false);
        mf.saveToFile();
        FileManager.mapFiles.put(playerName + ":" + mapName, mf);
    }

    public static void saveSchematic(Location loc, int x, int z, String playerName, String mapName, String description, Vector spawnLocation) throws IOException {
        saveSchematic(loc, x, z, playerName, mapName, description, spawnLocation, Material.AIR, MapFile.MapMode.NORMAL);
    }

    /**
     * Deletes a schematic.
     *
     * @param playerName Name of the creator of the map.
     * @param mapName Name of the map to remove.
     */
    public static void deleteSchematic(String playerName, String mapName) {
        File file = new File(FileManager.getSchemaName(playerName, mapName));
        if (file.exists()) {
            PPlayer p = PPlayer.get(playerName);
            if (p != null)
                p.mapCount--;
            file.delete();
        }
        file = new File(FileManager.getYamlMapName(playerName, mapName));
        if (file.exists())
            file.delete();
        FileManager.mapFiles.remove(playerName + ":" + mapName);
    }

    /**
     * Loads a map based on a location, name of the creator of the map, and the map name.
     *
     * @param loc Location where to place this map.
     * @param playerName The name of the player who made this map.
     * @param mapName Name of the map itself.
     * @throws IOException if we can't get the file or if it doesn't exist.
     */
    public static void loadSchematic(Location loc, String playerName, String mapName) throws IOException {
        File file = FileManager.getSchemaFile(playerName, mapName); //Gets the map.
        Objects.requireNonNull(ClipboardFormats.findByFile(file)).load(file)
          .paste(new BukkitWorld(loc.getWorld()), new Vector(loc.getX(), 0, loc.getZ()), false, true, null); //Pastes the map.
    }
}
