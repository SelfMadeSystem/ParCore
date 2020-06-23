package uwu.smsgamer.parcore;

import com.sk89q.worldedit.Vector;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import uwu.smsgamer.parcore.managers.ConfigManager;

/**
 * The class where it stores variables from the config.yml
 */
public class Vars {
    /** Size of the map/build arena*/
    public static Vector size = new Vector(64, 0, 80);
    /**
     * Spacing between each map
     */
    public static int spacing = 1024;
    /**
     * Material for the wall in build arena
     */
    public static Material wallMaterial = Material.BEDROCK;
    /**
     * Respawn location (to hub/spawn), not respawn in game (playing map)
     */
    public static Location spawnLocation = new Location(Bukkit.getWorld("world"), 0, 129, 0);
    /**
     * Directory where maps are held
     */
    public static String mapsPath = "maps/";
    /**
     * Directory where player data are held
     */
    public static String playerPath = "players/";

    /**
     * Sets up all the variables by first getting the config,
     * then getting each variable in config and setting it to
     * appropriate variable in this class.
     */
    public static void setup() {
        YamlConfiguration config = ConfigManager.getConfig("config");
        size = new Vector(config.getInt("size.x"), 0, config.getInt("size.z"));
        spacing = config.getInt("spacing");
        wallMaterial = Material.matchMaterial(config.getString("wallMaterial"));
        spawnLocation = new Location(Bukkit.getWorld(config.getString("spawnLocation.world")),
          config.getInt("spawnLocation.x"), config.getInt("spawnLocation.y"), config.getInt("spawnLocation.z"));
        mapsPath = config.getString("mapsPath");
        playerPath = config.getString("playerPath");
    }
}
