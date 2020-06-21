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
import java.util.*;

public class PlayerManager implements Listener {
    static ParCore pl;
    @Getter
    static PlayerManager instance;
    public static ArrayList<String> playerList = new ArrayList<>();
    // Type: 0 making, 1 playing, 2 in spawn
    //               name              min     max     type  respawn
    static SortedMap<String, FourEntry<Vector, Vector, Byte, Location>> players = new TreeMap<>();

    public static void setup(ParCore parCore) {
        pl = parCore;
        instance = new PlayerManager();
        Bukkit.getPluginManager().registerEvents(instance, pl);
    }

    public static void playerJoinedMap(Player player, Vector min, Vector max, String mapName) {
        String playerName = player.getName();
        Location respLoc = VectorUtils.toLocation(WorldManager.getWorld(), FileManager.getRespawnLocation(playerName, mapName));
        players.put(player.getName(), new FourEntry<>(null, null, (byte) 1, respLoc));
        BuildUtils.setupArena(Material.AIR, WorldManager.getWorld(), min, max);
        try {
            SchemUtils.loadSchematic(new Location(WorldManager.getWorld(), min.getBlockX(), min.getBlockY(), min.getBlockZ()), playerName, mapName);
        } catch (IOException e) {
            e.printStackTrace();
            player.sendMessage(ChatColor.DARK_RED + "An unknown error occurred when loading schematic: " + playerName + ":" + mapName +
              ". If you are an admin, please check console for any errors.");
            return;
        }
        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().clear();
        player.teleport(respLoc);
    }

    public static void playerMakeMap(Player player, Vector min, Vector max) {
        players.put(player.getName(), new FourEntry<>(min, max, (byte) 0, null));
        player.setGameMode(GameMode.CREATIVE);
    }

    public static void backToSpawn(Player player) {
        players.put(player.getName(), new FourEntry<>(null, null, (byte) 2, Vars.respawnLocation));
        player.setGameMode(GameMode.ADVENTURE);
        player.teleport(Vars.respawnLocation);
        player.setHealth(20);
        player.setFoodLevel(20);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!playerList.contains(event.getPlayer().getName())) {
            playerList.add(event.getPlayer().getName());
            backToSpawn(event.getPlayer());
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (players.containsKey(event.getEntity().getName())) {
            FourEntry<Vector, Vector, Byte, Location> entry = players.get(event.getEntity().getName());
            if (entry.getW() == 2) {
                event.setCancelled(true);
            }
            if (entry.getW() == 1) {
                if ((((Player) event.getEntity()).getHealth() - event.getDamage()) <= 0) {
                    event.setCancelled(true);
                    ((Player) event.getEntity()).setHealth(20);
                    event.getEntity().teleport(players.get(event.getEntity().getName()).getX());
                }
            }
        }
    }

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
                    event.getPlayer().sendMessage("You are not leave the designated arena!");
                }
            } else if (entry.getW() == 2) {
                event.getPlayer().setFoodLevel(20);
                if (event.getTo().getY() < 0 || event.getTo().getY() > 258) {
                    backToSpawn(event.getPlayer());
                }
            }
        }
    }

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
