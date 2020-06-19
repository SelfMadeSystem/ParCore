package uwu.smsgamer.parcore.utils;

import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public class MapFile {
    private final String player;
    private final String name;
    private final String description;
    private final boolean published;
    private final Set<Boolean> likes;
    @Getter
    private File schem;

    public MapFile(File schemFile, YamlConfiguration config) {
        player = config.getString("player");
        name = config.getString("name");
        description = config.getString("description");
        published = config.getBoolean("published");
        likes = new HashSet<>(config.getBooleanList("likes"));
    }
}
