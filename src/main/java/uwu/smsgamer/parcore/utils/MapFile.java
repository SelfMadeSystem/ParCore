package uwu.smsgamer.parcore.utils;

import lombok.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;

import java.io.File;
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
     * The likes this map got
     */
    private Set<Boolean> likes;
    /**
     * The spawn location of this map
     */
    private Vector spawnLocation;
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
}
