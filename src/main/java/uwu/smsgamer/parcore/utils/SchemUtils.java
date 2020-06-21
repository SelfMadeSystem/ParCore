package uwu.smsgamer.parcore.utils;

import com.boydti.fawe.object.schematic.Schematic;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.World;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import uwu.smsgamer.parcore.managers.FileManager;

import java.io.*;
import java.util.*;

public class SchemUtils { // TODO: 2020-06-18 PlayerName'n'MapName'n'shit
    public static void saveSchematic(Location loc, int x, int z, String playerName, String mapName) throws IOException {
        File file = new File(FileManager.getSchemaName(playerName, mapName));
        loc.setY(0);
        Vector bot = new Vector(loc.getBlockX(), 0, loc.getBlockZ());
        Vector top = new Vector(bot.getBlockX() + x, 255, bot.getBlockZ() + z);
        CuboidRegion region = new CuboidRegion((World) new BukkitWorld(loc.getWorld()), bot, top);
        Schematic schem = new Schematic(region);
        schem.save(file, ClipboardFormat.SCHEMATIC);
        YamlConfiguration config = new YamlConfiguration();
        config.set("player", playerName);
        config.set("name", mapName);
        config.set("description", "");
        config.set("published", false);
        config.set("likes", new ArrayList<Boolean>());
        config.save(new File(FileManager.getYamlName(playerName, mapName)));
    }

    public static void loadSchematic(Location loc, String playerName, String mapName) throws IOException {
        File file = FileManager.getSchemaFile(playerName, mapName);
        EditSession editSession = Objects.requireNonNull(ClipboardFormats.findByFile(file)).load(file)
          .paste(new BukkitWorld(loc.getWorld()), new Vector(loc.getX(), 0, loc.getZ()), false, true, null);
    }
}
