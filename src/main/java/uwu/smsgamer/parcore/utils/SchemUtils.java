package uwu.smsgamer.parcore.utils;

import com.boydti.fawe.object.schematic.Schematic;
import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.World;
import org.bukkit.Location;
import uwu.smsgamer.parcore.managers.FileManager;

import java.io.*;
import java.util.Objects;

public class SchemUtils { // TODO: 2020-06-18 PlayerName'n'MapName'n'shit
    public static void saveSchematic(Location loc) throws IOException {
        File file = new File("mySchem.schematic");
        loc.setY(0);
        Vector bot = new Vector(loc.getBlockX(), 0, loc.getBlockZ());
        Vector top = new Vector(bot.getBlockX(), 255, bot.getBlockZ()); //MUST be a whole number eg integer
        CuboidRegion region = new CuboidRegion((World) new BukkitWorld(loc.getWorld()), bot, top);
        Schematic schem = new Schematic(region);
        schem.save(file, ClipboardFormat.SCHEMATIC);
    }

    public static void loadSchematic(Location loc, String playerName, String mapName) throws IOException {
        File file = FileManager.getSchemaFile(playerName, mapName);
        EditSession editSession = Objects.requireNonNull(ClipboardFormats.findByFile(file)).load(file)
          .paste(new BukkitWorld(loc.getWorld()), new Vector(loc.getX(), 0, loc.getZ()), false, true, null);
    }
}
