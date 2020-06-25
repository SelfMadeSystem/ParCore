package uwu.smsgamer.parcore.utils;

import lombok.*;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;
import uwu.smsgamer.parcore.managers.FileManager;

import java.io.*;
import java.util.*;

/**
 * An object containing information about a map.
 * Currently more or less unused, but will be used
 * in the version of this amazing plugin.
 */
@Getter
@Setter
public class MapFile {
    /**
     * Name of creator of the map
     */
    private String player;
    /**
     * Name of the map itself
     */
    private String name;
    /**
     * Description of the map
     */
    private String description;
    /**
     * Whether or not it's published
     */
    private boolean published;
    /**
     * Whether or not it's completed by the creator
     */
    private boolean verified;
    /**
     * The likes this map got
     */
    private Set<Boolean> likes;
    /**
     * The spawn location of this map
     */
    private Vector spawnLocation;
    /**
     * The material to make the wall. Default's air.
     */
    private Material wallMaterial;
    /**
     * The schematic file of this map
     */
    private File schem;

    /**
     * @param schemFile The file of the schematic of the map.
     * @param config The configuration to load info from.
     */
    public MapFile(File schemFile, YamlConfiguration config) {
        schem = schemFile;
        player = config.getString("player");
        name = config.getString("name");
        description = config.getString("description");
        published = config.getBoolean("published");
        likes = new HashSet<>(config.getBooleanList("likes"));
        wallMaterial = Material.getMaterial(config.getString("wallMaterial"));
        verified = config.getBoolean("verified");
        spawnLocation = new Vector();
    }

    /**
     * Resets information of this map.
     *
     * @param config The configuration to load info from.
     */
    public void resetInfo(YamlConfiguration config) {
        player = config.getString("player");
        name = config.getString("name");
        description = config.getString("description");
        published = config.getBoolean("published");
        likes = new HashSet<>(config.getBooleanList("likes"));
        spawnLocation = new Vector();
    }

    @Override
    public String toString() {
        return "MapFile{" +
          "player='" + player + '\'' +
          ", name='" + name + '\'' +
          ", description='" + description + '\'' +
          ", published=" + published +
          ", verified=" + verified +
          ", likes=" + likes +
          ", spawnLocation=" + spawnLocation +
          ", wallMaterial=" + wallMaterial +
          ", schem=" + schem +
          '}';
    }

    public void saveToFile() {
        YamlConfiguration config = new YamlConfiguration();
        config.set("player", player); //Sets "player" to player
        config.set("name", name); //Sets "name" to the name
        config.set("description", description); //Sets "description" to nothing since it's unused, but still needed to be loaded in the FileManager.
        config.set("verified", verified); //Sets "verified" to false since it's unused, but still needed to be loaded in the FileManager.
        config.set("published", published); //Sets "published" to false since it's unused, but still needed to be loaded in the FileManager.
        config.set("likes", new ArrayList<Boolean>()); //Sets "likes" to an empty array since it's unused, but still needed to be loaded in the FileManager.
        config.set("x", spawnLocation.getBlockX() + 0.5); //Sets "x" to the block position + 0.5 of spawnLocation.
        config.set("y", spawnLocation.getBlockY()); //Sets "y" to the block position of spawnLocation.
        config.set("z", spawnLocation.getBlockZ() + 0.5); //Sets "z" to the block position + 0.5 of spawnLocation.
        config.set("wallMaterial", wallMaterial.name()); //Sets "wallMaterial" to the wall material. (:
        try {
            config.save(FileManager.getYamlMapName(player, name));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
