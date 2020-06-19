package uwu.smsgamer.parcore;

import com.sk89q.worldedit.Vector;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import uwu.smsgamer.parcore.managers.ConfigManager;

public class Vars {
    public static Vector size = new Vector(64, 0, 80);
    public static int spacing = 1024;
    public static Material wallMaterial = Material.BEDROCK;

    public static void setup() {
        YamlConfiguration config = ConfigManager.getConfig("config");
        size = new Vector(config.getInt("size.x"), 0, config.getInt("size.z"));
        spacing = config.getInt("spacing");
        wallMaterial = Material.matchMaterial(config.getString("wallMaterial"));
    }
}
