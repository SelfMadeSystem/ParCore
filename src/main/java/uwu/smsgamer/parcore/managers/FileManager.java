package uwu.smsgamer.parcore.managers;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import uwu.smsgamer.parcore.utils.MapFile;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class FileManager {
    private static JavaPlugin pl;
    private static final Map<String, MapFile> mapFiles = new HashMap<>();
    private static final String sp = ":";

    public static void setup(JavaPlugin plugin) {
        pl = plugin;
        try {
            for (String st : getAllSchemaNames()) {
                String[] strings = st.split(sp);
                mapFiles.put(st, getMapFile(strings[0], strings[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File getSchemaFile(String player, String map) {
        return mapFiles.get(player + sp + map).getSchem();
    }

    public static MapFile getMapFile(String player, String map) {
        return new MapFile(new File(pl.getDataFolder(), player + sp + map + ".schematic"),
          YamlConfiguration.loadConfiguration(new File(pl.getDataFolder(), player + sp + map + ".schematic")));
    }

    public static String getSchemaName(String player, String map) {
        return player + sp + map + ".schematic";
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
