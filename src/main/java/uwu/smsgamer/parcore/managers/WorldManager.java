package uwu.smsgamer.parcore.managers;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.*;
import org.bukkit.*;
import org.bukkit.entity.Player;
import uwu.smsgamer.parcore.*;
import uwu.smsgamer.parcore.utils.*;

import java.io.IOException;

public class WorldManager {
    static final String worldName = "ParCoreWorld";
    static ParCore pl;
    static World world;
    static BaseBlock wallBlock;

    public static void setup(ParCore plugin) {
        pl = plugin;
        WorldCreator wc = new WorldCreator(worldName);
        wc.type(WorldType.FLAT);
        wc.generatorSettings("3;minecraft:air;127;");
        Bukkit.createWorld(wc);
        world = Bukkit.getWorld(worldName);
        world.setAutoSave(false);
        wallBlock = new ImmutableDatalessBlock(Vars.wallMaterial.getId());
    }

    public static void done() {
        //Bukkit.getWorldContainer(). TODO: Remove it <3
    }

    public static void newBuildArena(Player player) {
        int last = PlayerManager.playerList.indexOf(player.getName());
        Vector min = new Vector((last * Vars.spacing), 0, (last * Vars.spacing));
        Vector max = new Vector(Vars.size.getBlockX() + (last * Vars.spacing), 255, Vars.size.getBlockZ() + (last * Vars.spacing));
        PlayerManager.playerChanged(player, min, max);
        BuildUtils.setupArena(Vars.wallMaterial, world, min, max);
        player.teleport(new Location(world, min.getX() + 4, 128, min.getZ() + 4));
    }

    public static void newMapArena(Player player, String playerName, String mapName) {
        int last = PlayerManager.playerList.indexOf(player.getName());
        Vector min = new Vector((last * Vars.spacing), 0, (last * Vars.spacing));
        Vector max = new Vector(Vars.size.getBlockX() + (last * Vars.spacing), 255, Vars.size.getBlockZ() + (last * Vars.spacing));
        PlayerManager.playerJoinedMap(player);
        BuildUtils.setupArena(Material.AIR, world, min, max);
        try {
            SchemUtils.loadSchematic(new Location(player.getWorld(), min.getBlockX(), min.getBlockY(), min.getBlockZ()), playerName, mapName);
        } catch (IOException e) {
            e.printStackTrace();
            player.sendMessage(ChatColor.DARK_RED + "An unknown error occurred when loading schematic: " + playerName + ":" + mapName +
              ". If you are an admin, please check console for any errors.");
            return;
        }
        player.teleport(new Location(world, min.getX() + 4, 128, min.getZ() + 4));
    }

    public static void saveBuildArena(Player player, String mapName) {
        int last = PlayerManager.playerList.indexOf(player.getName());
        try {
            SchemUtils.saveSchematic(new Location(world, (last * Vars.spacing) + 1, 0, (last * Vars.spacing) + 1),
              Vars.size.getBlockX() - 2, Vars.size.getBlockZ() - 2, player.getName(), mapName);
        } catch (IOException e) {
            e.printStackTrace();
            player.sendMessage(ChatColor.DARK_RED + "An unknown error occurred when saving schematic: " + player.getName() + ":" + mapName +
              ". If you are an admin, please check console for any errors.");
        }
    }
}
