package uwu.smsgamer.parcore.managers;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.*;

/**
 * Config manager for this plugin loads configs and gets configs and saves configs. Nothing to really document here if you can read Java.
 */
public class ConfigManager {
    public static Map<String, YamlConfiguration> configs = new HashMap<>();
    private static JavaPlugin pl;

    public static void setup(JavaPlugin plugin, String... configs) {
        pl = plugin;
        for (String config : configs) {
            System.out.println("ParCore: Loading config: " + config);
            try {
                loadConfig(config);
                System.out.println("ParCore: Loaded config: " + config);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("ParCore: Error while loading config: " + config);
            }
        }
    }

    public static YamlConfiguration getConfig(String name) {
        return configs.get(name);
    }

    public static YamlConfiguration loadConfig(String name) {
        configs.remove(name);
        File configFile = new File(pl.getDataFolder(), name + ".yml");
        if (!configFile.exists())
            pl.saveResource(name + ".yml", false);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        configs.put(name, config);
        return config;
    }

    public static void saveConfig(String name) {
        try {
            configs.get(name).save(pl.getDataFolder().getAbsolutePath() + File.separator + name + ".yml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
