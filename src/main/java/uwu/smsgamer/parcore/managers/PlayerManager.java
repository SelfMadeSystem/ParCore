package uwu.smsgamer.parcore.managers;

import com.sk89q.worldedit.Vector;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerJoinEvent;
import uwu.smsgamer.parcore.*;
import uwu.smsgamer.parcore.utils.ThreeEntry;

import java.util.*;

public class PlayerManager implements Listener {
    static ParCore pl;
    @Getter
    static PlayerManager instance;
    public static ArrayList<String> playerList = new ArrayList<>();
    static SortedMap<String, ThreeEntry<Vector, Vector, Boolean>> players = new TreeMap<>();

    public static void setup(ParCore parCore) {
        pl = parCore;
        instance = new PlayerManager();
        Bukkit.getPluginManager().registerEvents(instance, pl);
    }

    public static void playerJoinedMap(Player player) {
        players.put(player.getName(), new ThreeEntry<>(null, null, true));
    }

    public static void playerChanged(Player player, Vector min, Vector max) {
        players.put(player.getName(), new ThreeEntry<>(min, max, false));
    }

    public static void backToSpawn(Player player) {
        players.put(player.getName(), new ThreeEntry<>(null, null, true));
        player.teleport(Vars.respawnLocation);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!playerList.contains(event.getPlayer().getName()))
            playerList.add(event.getPlayer().getName());
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (players.containsKey(event.getPlayer().getName())) {
            ThreeEntry<Vector, Vector, Boolean> entry = players.get(event.getPlayer().getName());
            if (entry.getW()) {
                event.getPlayer().sendMessage("You are not allowed to break blocks!");
                event.setCancelled(true);
                return;
            }
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
            ThreeEntry<Vector, Vector, Boolean> entry = players.get(event.getPlayer().getName());
            if (entry.getW()) {
                event.getPlayer().sendMessage("You are not allowed to place blocks!");
                event.setCancelled(true);
                return;
            }
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
