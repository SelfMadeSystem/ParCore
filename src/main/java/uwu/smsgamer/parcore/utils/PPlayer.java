package uwu.smsgamer.parcore.utils;

import org.bukkit.configuration.file.YamlConfiguration;
import uwu.smsgamer.parcore.managers.*;

import java.io.*;
import java.util.*;

/**
 * Parkour player. Every player has this object associated with them.
 * It tracks stuff like amount of levels, their total rating, the
 * levels that they beat, etc.
 */
public class PPlayer {
    private static final HashMap<String, PPlayer> players = new HashMap<>();
    public static Set<PPlayer> pPlayers = new HashSet<>();
    /**
     * Name of player
     */
    public String name;
    /**
     * Maps the player has made
     */
    public int mapCount;
    /**
     * Maximum amount of maps the player is allowed to have
     */
    public int maxMapCount;
    /**
     * The maps that have been completed
     */
    //      Creator MapName
    public HashMap<String, String> completedMaps;

    public PPlayer(String name, int mapCount, int maxMapCount, HashMap<String, String> completedMaps) {
        this.name = name;
        this.mapCount = mapCount;
        this.maxMapCount = maxMapCount;
        this.completedMaps = completedMaps;
    }

    public static boolean contains(String s) {
        if (players.containsKey(s))
            return true;
        for (PPlayer p : pPlayers) {
            if (p.name.equals(s)) {
                players.put(s, p);
                return true;
            }
        }
        return false;
    }

    public static PPlayer get(String s) {
        if (players.containsKey(s))
            return players.get(s);
        for (PPlayer p : pPlayers)
            if (p.name.equals(s)) {
                players.put(s, p);
                return p;
            }
        return null;
    }

    public static PPlayer loadPlayer(String playerName) {
        playerName = playerName.split("\\.")[0];
        File file = new File(PlayerManager.playerPath, playerName + ".yml");
        if (file.exists()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            HashMap<String, String> completedMaps = new HashMap<>();
            for (String st : config.getStringList("completedMaps")) {
                String[] split = st.split(":");
                completedMaps.put(split[0], split[1]);
            }
            return new PPlayer(playerName, config.getInt("mapCount"), config.getInt("maxMapCount"), completedMaps);
        } else {
            return new PPlayer(playerName, 0, 2, new HashMap<>());
        }
    }

    public void saveToFile() {
        YamlConfiguration config = new YamlConfiguration();
        config.set("name", name);
        config.set("mapCount", mapCount);
        config.set("maxMapCount", maxMapCount);
        List<String> maps = new ArrayList<>();
        for (Map.Entry<String, String> entry : completedMaps.entrySet()) {
            maps.add(entry.getKey() + ":" + entry.getValue());
        }
        config.set("completedMaps", maps);
        try {
            config.save(FileManager.getYamlPlayerName(name));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
