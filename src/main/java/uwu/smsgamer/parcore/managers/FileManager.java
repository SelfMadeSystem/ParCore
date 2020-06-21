package uwu.smsgamer.parcore.managers;

import com.sk89q.worldedit.Vector;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import uwu.smsgamer.parcore.Vars;
import uwu.smsgamer.parcore.utils.MapFile;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class FileManager {
    private static JavaPlugin pl;
    private static final Map<String, MapFile> mapFiles = new HashMap<>();
    private static final String sp = ":";
    private static String mapPath;

    public static void setup(JavaPlugin plugin) {
        pl = plugin;
        mapPath = pl.getDataFolder().getAbsolutePath() + "/" + Vars.mapsPath;
        try {
            for (String st : getAllSchemaNames()) {
                String[] strings = st.split(sp);
                mapFiles.put(st, getMapFile(strings[0], strings[1].split("\\.")[0]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File getSchemaFile(String player, String map) {
        MapFile mF = mapFiles.get(player + sp + map);
        return mF != null ? mF.getSchem() : new File(mapPath, player + sp + map + ".schematic");
    }

    public static MapFile getMapFile(String player, String map) {
        return new MapFile(new File(pl.getDataFolder(), player + sp + map + ".schematic"),
          YamlConfiguration.loadConfiguration(new File(mapPath, player + sp + map + ".yml")));
    }

    public static String getSchemaName(String player, String map) {
        return mapPath + player + sp + map + ".schematic";
    }

    public static String getYamlName(String player, String map) {
        return mapPath + player + sp + map + ".yml";
    }

    public static Vector getRespawnLocation(String player, String map) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(getYamlName(player, map)));
        return new Vector(config.getInt("x"), config.getInt("y"), config.getInt("z"));
    }

    public static List<String> getAllSchemaNames() throws IOException {
        Stream<Path> walk = Files.walk(Paths.get(mapPath));
        return walk.filter(Files::isRegularFile)
          .map(Path::toString).collect(Collectors.toList());
    }
}
