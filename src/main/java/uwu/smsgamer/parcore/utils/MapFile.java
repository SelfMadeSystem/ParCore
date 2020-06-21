package uwu.smsgamer.parcore.utils;

import lombok.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.*;

@Getter
@Setter
public class MapFile {
    private final String player;
    private final String name;
    private final String description;
    private final boolean published;
    private final Set<Boolean> likes;
    private final Vector spawnLocation;
    private File schem;

    public MapFile(File schemFile, YamlConfiguration config) {
        schem = schemFile;
        player = config.getString("player");
        name = config.getString("name");
        description = config.getString("description");
        published = config.getBoolean("published");
        likes = new HashSet<>(config.getBooleanList("likes"));
        spawnLocation = new Vector();
    }
}
