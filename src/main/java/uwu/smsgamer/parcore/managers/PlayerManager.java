package uwu.smsgamer.parcore.managers;

import com.sk89q.worldedit.Vector;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import uwu.smsgamer.parcore.ParCore;
import uwu.smsgamer.parcore.utils.ThreeEntry;

import java.util.*;

public class PlayerManager implements Listener {
    static ParCore pl;
    @Getter
    static PlayerManager instance;
    static Map<String, ThreeEntry<Vector, Vector, Boolean>> players = new HashMap<>();

    public static void setup(ParCore parCore) {
        pl = parCore;
        instance = new PlayerManager();
        Bukkit.getPluginManager().registerEvents(instance, pl);
    }

    public static void playerChanged(Player player, Vector min, Vector max) {
        players.put(player.getName(), new ThreeEntry<>(min, max, false));
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
