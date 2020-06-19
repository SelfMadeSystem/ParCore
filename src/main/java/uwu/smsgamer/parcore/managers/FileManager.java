package uwu.smsgamer.parcore.managers;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.stream.*;

public class FileManager {
    private static JavaPlugin pl;

    public static void setup(JavaPlugin plugin) {
        pl = plugin;
    }

    public static File getSchemaFile(String player, String map) {
        return new File(pl.getDataFolder(), player + "-" + map + ".schematic");
    }

    public static String getSchemaName(String player, String map) {
        return player + "-" + map + ".schematic";
    }

    public static List<String> getAllSchemaNames() throws IOException {
        Stream<Path> walk = Files.walk(Paths.get(pl.getDataFolder().getAbsolutePath()));
        return walk.filter(Files::isRegularFile)
          .map(Path::toString).collect(Collectors.toList());
    }

    public static List<File> getAllSchemaFiles() throws IOException {
        Stream<Path> walk = Files.walk(Paths.get(pl.getDataFolder().getAbsolutePath()));
        return walk.filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList());
    }
}
