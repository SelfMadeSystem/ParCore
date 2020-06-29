package uwu.smsgamer.parcore.managers;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.*;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.entity.Player;
import uwu.smsgamer.parcore.*;
import uwu.smsgamer.parcore.utils.*;

import java.io.*;

/**
 * Manages the parkour world, i.e. sets up an arena/map, clears arenas/maps, etc.
 */
public class WorldManager {
    /**
     * Name of the parkour world
     */
    static final String worldName = "ParCoreWorld";
    /**
     * Instance of the ParCore plugin
     */
    static ParCore pl;
    /**
     * Instance of the Parkour world
     */
    @Getter
    static World world;
    /**
     * A WorldEdit BaseBlock for the wall
     */
    static BaseBlock wallBlock;

    /**
     * Sets up the world and the wallBlock.
     *
     * @param plugin The ParCore instance.
     */
    public static void setup(ParCore plugin) {
        pl = plugin;
        WorldCreator wc = new WorldCreator(worldName); //Makes new WorldCreator to create the parkour world.
        wc.type(WorldType.FLAT); //Makes it flat
        wc.generatorSettings("3;minecraft:air;127;"); //Makes it a void (nothing)
        world = Bukkit.createWorld(wc); //Actually creates the world or loads it if already made and sets the world instance to the created or loaded world.
        world.setAutoSave(false); //Makes sure the world doesn't save.
        wallBlock = new ImmutableDatalessBlock(Vars.wallMaterial.getId()); //Makes the WorldEdit BaseBlock for future use.
    }

    /**
     * Currently unused but might delete the world... who knows
     */
    public static void done() {
        //Bukkit.getWorldContainer(). TODO: Remove it <3
    }

    /**
     * Sets up a new build arena for the player to build his parkour map.
     *
     * @param player The player that this build arena belongs to.
     */
    public static void newBuildArena(Player player) {
        int last = PlayerManager.playerList.indexOf(player.getName()); //Gets the index of the player so that arenas don't overlap.
        Vector min = new Vector((last * Vars.spacing), 0, (last * Vars.spacing)); //Gets the min vector by using the index of the player
        // and multiplying it by the spacing set in Vars.
        Vector max = new Vector(Vars.size.getBlockX() + (last * Vars.spacing), 255, Vars.size.getBlockZ() + (last * Vars.spacing));
        //Gets the max vector by getting the the size set in vars and adding, the index of the player multiplied by the spacing.
        PlayerManager.playerMakeMap(player, min, max, ""); //Tells PlayerManager that we're making a "MakeMap" (building arena)
        BuildUtils.setupArena(Vars.wallMaterial, world, min, max); //Sets up the arena with wallMaterial set in Vars as the wall material,
        // the "world" set up in the setup function, and the minimum and maximum vector positions.
        player.setAllowFlight(true); //Sets the player to be able to fly.
        player.setFlying(true); //Makes sure the player is flying so that he doesn't fall into the void.
        player.teleport(new Location(world, min.getX() + 4, 128, min.getZ() + 4)); //Finally, we teleport the player there.
    }

    /**
     * Sets up a build arena with a schematic already in place for the player to build his parkour map.
     *
     * @param player The player that this build arena belongs to.
     */
    public static void newBuildArena(Player player, String mapName) {
        int last = PlayerManager.playerList.indexOf(player.getName()); //Gets the index of the player so that arenas don't overlap.
        Vector min = new Vector((last * Vars.spacing), 0, (last * Vars.spacing)); //Gets the min vector by using the index of the player
        // and multiplying it by the spacing set in Vars.
        Vector max = new Vector(Vars.size.getBlockX() + (last * Vars.spacing), 255, Vars.size.getBlockZ() + (last * Vars.spacing));
        //Gets the max vector by getting the the size set in vars and adding, the index of the player multiplied by the spacing.
        PlayerManager.playerMakeMap(player, min, max, mapName); //Tells PlayerManager that we're making a "MakeMap" (building arena)
        BuildUtils.setupArena(Vars.wallMaterial, world, min, max); //Sets up the arena with wallMaterial set in Vars as the wall material,
        PlayerManager.pasteInMap(player, min, player.getName(), mapName); //Pastes in the map :)
        // the "world" set up in the setup function, and the minimum and maximum vector positions.
        player.setAllowFlight(true); //Sets the player to be able to fly.
        player.setFlying(true); //Makes sure the player is flying so that he doesn't fall into the void.
        player.teleport(new Location(world, min.getX() + 4, 128, min.getZ() + 4)); //Finally, we teleport the player there.
    }

    /**
     * Sets up a new map for the player to play.
     *
     * @param player The player.
     * @param playerName The name of the creator of the map.
     * @param mapName The map name.
     */
    public static void newMapArena(Player player, String playerName, String mapName, boolean... placeholder) {
        int last = PlayerManager.playerList.indexOf(player.getName()); //Gets the index of the player so that arenas don't overlap
        Vector min = new Vector((last * Vars.spacing), 0, (last * Vars.spacing));
        Vector max = new Vector(Vars.size.getBlockX() + (last * Vars.spacing), 255, Vars.size.getBlockZ() + (last * Vars.spacing));
        PlayerManager.playerJoinedMap(player, min, max, playerName, mapName, placeholder);
    }

    /**
     * Saves the map of a player to disk.
     *
     * @param player The player.
     * @param mapName The name of the map.
     * @param wallMaterial The material of the wall of the map.
     * @return Whether it was successful or not.
     */
    public static boolean saveBuildArena(Player player, String mapName, String description, Material wallMaterial, MapFile.MapMode mode) {
        PPlayer pPlayer = PPlayer.get(player.getName());
        if (!new File(FileManager.getSchemaName(player.getName(), mapName)).exists()) {
            if (pPlayer.mapCount + 1 > pPlayer.maxMapCount) {
                Chat.send(player, "&cYou have reached your maximum amount of maps of " + pPlayer.maxMapCount + "!");
                return false;
            }
            pPlayer.mapCount++;
        }
        if (!mapName.matches("[a-zA-Z0-9]+")) {
            Chat.send(player, "&cName of map must be alphanumeric.");
            return false;
        }
        int last = PlayerManager.playerList.indexOf(player.getName()); //Gets the index of the player so that arenas don't overlap.
        try {
            double s = (last * Vars.spacing);
            if (wallMaterial == null)
                SchemUtils.saveSchematic(new Location(world, s + 1, 0, s + 1),
                  Vars.size.getBlockX() - 2, Vars.size.getBlockZ() - 2, player.getName(), mapName, description,
                  new Vector(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()).subtract(s, 0, s));
            else
                SchemUtils.saveSchematic(new Location(world, s + 1, 0, s + 1),
                  Vars.size.getBlockX() - 2, Vars.size.getBlockZ() - 2, player.getName(), mapName, description,
                  new Vector(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()).subtract(s, 0, s), wallMaterial, mode);
            //Saves the schematic.
        } catch (IOException e) { //Uh oh, an error occurred!
            e.printStackTrace(); //Print the error.
            Chat.send(player, "&4An unknown error occurred when saving schematic: " + player.getName() + ":" + mapName +
              ". If you are an admin, please check console for any errors.");
            //Tell the player that an error occurred.
            return false;
        }
        return true;
    }
}
