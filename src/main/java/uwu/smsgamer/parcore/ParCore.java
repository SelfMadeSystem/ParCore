package uwu.smsgamer.parcore;

import org.bukkit.Bukkit;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.dependency.Dependency;
import org.bukkit.plugin.java.annotation.plugin.*;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import uwu.smsgamer.parcore.managers.*;
import uwu.smsgamer.parcore.utils.ThreadUtils;

@Plugin(name = "ParCore", version = "1.0")
@Description("Parkour core for MatrixPvP.")
@Author("Sms_Gamer_3808")
@Dependency("LuckPerms")
@Dependency("WorldEdit")
public final class ParCore extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        ConfigManager.setup(this, "config");
        ThreadUtils.setup(this);
        Vars.setup();
        FileManager.setup(this);
        WorldManager.setup(this);
        PlayerManager.setup(this);
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        WorldManager.done();
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().equalsIgnoreCase("/pkr")) {
            WorldManager.newBuildArena(event.getPlayer());
            event.setCancelled(true);
        }
    }
}
