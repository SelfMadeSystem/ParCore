package uwu.smsgamer.parcore;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.dependency.Dependency;
import org.bukkit.plugin.java.annotation.plugin.*;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import uwu.smsgamer.parcore.managers.*;

@Plugin(name = "ParCore", version = "1.0")
@Description("Parkour core for MatrixPvP.")
@Author("Sms_Gamer_3808")
@Dependency("LuckPerms")
@Dependency("WorldEdit")
public final class ParCore extends JavaPlugin {

    @Override
    public void onEnable() {
        ConfigManager.setup(this, "config");
        FileManager.setup(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
