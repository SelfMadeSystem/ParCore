package uwu.smsgamer.parcore.managers;

import com.sk89q.worldedit.Vector;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import uwu.smsgamer.parcore.*;
import uwu.smsgamer.parcore.utils.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The manager of players. Does stuff
 * like teleport them around, prevent
 * block breaking/placing, prevent
 * death, etc.
 */
public class PlayerManager implements Listener {
    /**
     * ParCore instance
     */
    static ParCore pl;
    /**
     * An instance of this because this implements Listener and it needs to be in object form to listen to events
     */
    @Getter
    static PlayerManager instance;
    /**
     *
     */
    public static String playerPath;
    /**
     * A list of players, each with an "id" determined by their join order
     */
    public static ArrayList<String> playerList = new ArrayList<>();
    /**
     * Used to store information about the player's current map situation and map limits.
     */
    // Type: 0 making, 1 playing, 2 in spawn
    //               name              min     max     type  respawn/czechpoint
    public static SortedMap<String, FourEntry<Vector, Vector, Byte, Location>> players = new TreeMap<>();

    /**
     * Used to set up the manager. Basically just registers events to this class object.
     *
     * @param parCore Main plugin class.
     */
    public static void setup(ParCore parCore) {
        pl = parCore;
        instance = new PlayerManager();
        playerPath = pl.getDataFolder().getAbsolutePath() + "/" + Vars.playerPath;
        Bukkit.getPluginManager().registerEvents(instance, pl);
        try {
            for (Path path : Files.walk(Paths.get(playerPath)).filter(Files::isRegularFile).collect(Collectors.toSet())) {
                PPlayer.pPlayers.add(PPlayer.loadPlayer(path.getFileName().toString().split("\\.")[0]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void done() {
        for (PPlayer pPlayer : PPlayer.pPlayers) {
            pPlayer.saveToFile();
        }
    }

    /**
     * Used when the player joins a map. It clears the area, then pastes in the map,
     * then
     *
     * @param player The player that joined the map.
     * @param min The minimum boundary.
     * @param max The maximum boundary.
     * @param playerName The name of the creator of the map.
     * @param mapName The name of the map itself.
     */
    public static void playerJoinedMap(Player player, Vector min, Vector max, String playerName, String mapName) {
        Location respLoc = VectorUtils.toLocation(WorldManager.getWorld(),
          FileManager.getRespawnLocation(playerName, mapName).add(min));
        //Gets the initial spawn location to teleport the player and to set as the respawn location for when the player dies.
        players.put(player.getName(), new FourEntry<>(null, null, (byte) 1, respLoc));
        //Adds the player's name as key, and a new FourEntry with null as the boundaries,
        // 1 (playing) as the type, and respLoc as the respawn location for when the player dies.
        BuildUtils.setupArena(FileManager.getMapFile(playerName, mapName).getWallMaterial(), WorldManager.getWorld(), min, max);
        //Sets up the arena by clearing everything between min & max vectors.
        pasteInMap(player, min, playerName, mapName); //Pastes in the map :)
        player.setGameMode(GameMode.ADVENTURE); //Sets the player in adventure so that he can't place or break any blox.
        player.getInventory().clear(); //Clears his inventory.
        respawn(player); //Finally, teleports the player to the desired location.
    }

    /**
     * Pastes in a map for the player, whether it's for him
     * to play on it, or for him to continue editing it.
     *
     * @param player The player
     * @param place Place to paste it.
     * @param playerName Name of creator.
     * @param mapName Name of map.
     */
    public static void pasteInMap(Player player, Vector place, String playerName, String mapName) {
        try {
            SchemUtils.loadSchematic(new Location(WorldManager.getWorld(), place.getBlockX() + 1, place.getBlockY(), place.getBlockZ() + 1), playerName, mapName);
            //Pastes in the map.
        } catch (IOException e) { //Uh oh, an error has occurred!
            e.printStackTrace(); //Prints the error to console.
            player.sendMessage(ChatColor.DARK_RED + "An unknown error occurred when loading schematic: " + playerName + ":" + mapName +
              ". If you are an admin, please check console for any errors.");
            //Tells the player that an error occurred when pasting in the schematic.
            backToSpawn(player); //Sends the player back to spawn.
        }
    }

    /**
     * Sets up the build arena for the player to build his map in.
     *
     * @param player The player that will build the map.
     * @param min The minimum boundary.
     * @param max The maximum boundary.
     */
    public static void playerMakeMap(Player player, Vector min, Vector max) {
        players.put(player.getName(), new FourEntry<>(min, max, (byte) 0, null));
        //Adds the player's name as key, and a new FourEntry with the min and max as boundaries,
        // 0 (making) as the type, and nothing as the respawn location.
        player.setGameMode(GameMode.CREATIVE); //Sets the player's gamemode to gmc so he can start building.
    }

    /**
     * Sends the player back to spawn.
     *
     * @param player The player to be sent back to spawn.
     */
    public static void backToSpawn(Player player) {
        players.put(player.getName(), new FourEntry<>(null, null, (byte) 2, Vars.spawnLocation));
        //Adds the player's name as key, and a new FourEntry with null boundaries,
        // 2 (at spawn) as the type, and the spawn location as the respawn location.
        player.setGameMode(GameMode.ADVENTURE); //Sets the player in adventure so that he can't place or break any blox.
        player.getInventory().clear(); //Clears his inventory.
        player.setHealth(20); //Sets player's health to 20
        player.setFoodLevel(20); //Sets player's food level to 20
        player.teleport(Vars.spawnLocation); //Finally, teleports the player to the desired location.
    }

    public static void respawn(Player player) {
        if (players.containsKey(player.getName())) {
            Location loc = players.get(player.getName()).getX();
            player.teleport(new Location(loc.getWorld(), loc.getX() + 0.5, loc.getY(), loc.getZ() + 0.5));
            player.setHealth(20);
            player.setFoodLevel(20);
        }
    }

    /**
     * Adds the player to the playerList if not already there and sends the player to spawn.
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!playerList.contains(event.getPlayer().getName()))
            playerList.add(event.getPlayer().getName());
        if (!PPlayer.contains(event.getPlayer().getName()))
            PPlayer.pPlayers.add(PPlayer.loadPlayer(event.getPlayer().getName()));
        backToSpawn(event.getPlayer());
    }

    /**
     * Happens when the player takes damage. Cancel it if the player's at spawn
     * or if health is less than 0 and he's playing a map. If health's less than
     * 0 and player's playing a map, also set health to 20 and teleport him to
     * his designated respawn location essentially doing an instant respawn.
     */
    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (players.containsKey(event.getEntity().getName())) {
            FourEntry<Vector, Vector, Byte, Location> entry = players.get(event.getEntity().getName());
            if (entry.getW() == 2) {
                event.setCancelled(true);
            }
            if (entry.getW() == 1) {
                if ((((Player) event.getEntity()).getHealth() - event.getDamage()) <= 0) {
                    Player p = ((Player) event.getEntity());
                    event.setCancelled(true);
                    p.setHealth(20);
                    respawn(p);
                }
            }
        }
    }

    /**
     * Makes sure the player doesn't exit his designated area
     * or teleport him to spawn if below 0 or above 258 when at spawn.
     */
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (players.containsKey(event.getPlayer().getName())) {
            FourEntry<Vector, Vector, Byte, Location> entry = players.get(event.getPlayer().getName());
            if (entry.getW() == 0) {
                if (entry.getK() == null || entry.getV() == null)
                    return;
                Vector min = entry.getK();
                Vector max = entry.getV();
                if (event.getTo().getX() <= min.getBlockX() ||
                  event.getTo().getZ() <= min.getBlockZ() ||
                  event.getTo().getX() >= max.getBlockX() ||
                  event.getTo().getZ() >= max.getBlockZ() ||
                  event.getTo().getY() < -4 ||
                  event.getTo().getY() > 260) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage("You are not to leave the designated arena!");
                }
            } else if (entry.getW() == 1) {
                if (event.getTo().getY() < -4) {
                    respawn(event.getPlayer());
                }
            } else if (entry.getW() == 2) {
                event.getPlayer().setFoodLevel(20);
                if (event.getTo().getY() < 0 || event.getTo().getY() > 258) {
                    backToSpawn(event.getPlayer());
                }
            }
        }
    }

    /**
     * Prevents block breaking when we don't want the player to break blocks.
     */
    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (players.containsKey(event.getPlayer().getName())) {
            FourEntry<Vector, Vector, Byte, Location> entry = players.get(event.getPlayer().getName());
            if (entry.getW() > 0) {
                event.getPlayer().sendMessage("You are not allowed to break blocks!");
                event.setCancelled(true);
                return;
            }
            if (entry.getK() == null || entry.getV() == null)
                return;
            Vector min = entry.getK();
            Vector max = entry.getV();
            if (event.getBlock().getLocation().getX() <= min.getBlockX() ||
              event.getBlock().getLocation().getZ() <= min.getBlockZ() ||
              event.getBlock().getLocation().getX() >= max.getBlockX() ||
              event.getBlock().getLocation().getZ() >= max.getBlockZ()) {
                event.setCancelled(true);
                event.getPlayer().sendMessage("You are not allowed to break blocks beyond designated arena!");
            }
        }
    }

    /**
     * Prevents block placing when we don't want the player to break blocks.
     */
    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (players.containsKey(event.getPlayer().getName())) {
            FourEntry<Vector, Vector, Byte, Location> entry = players.get(event.getPlayer().getName());
            if (entry.getW() > 0) {
                event.getPlayer().sendMessage("You are not allowed to place blocks!");
                event.setCancelled(true);
                return;
            }
            if (entry.getK() == null || entry.getV() == null)
                return;
            Vector min = entry.getK();
            Vector max = entry.getV();
            if (event.getBlock().getLocation().getX() <= min.getBlockX() ||
              event.getBlock().getLocation().getZ() <= min.getBlockZ() ||
              event.getBlock().getLocation().getX() >= max.getBlockX() ||
              event.getBlock().getLocation().getZ() >= max.getBlockZ()) {
                event.setCancelled(true);
                event.getPlayer().sendMessage("You are not allowed to place blocks beyond designated arena!");
            }
        }
    }
}
