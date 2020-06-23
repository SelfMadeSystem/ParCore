package uwu.smsgamer.parcore;

import org.bukkit.Bukkit;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.dependency.Dependency;
import org.bukkit.plugin.java.annotation.plugin.*;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import uwu.smsgamer.parcore.managers.*;
import uwu.smsgamer.parcore.utils.*;

/**
 * Main class for this plugin.
 */
@Plugin(name = "ParCore", version = "1.0")
@Description("Parkour core for MatrixPvP.")
@Author("Sms_Gamer_3808")
@Dependency("LuckPerms")
@Dependency("WorldEdit")
public final class ParCore extends JavaPlugin implements Listener {

    /**
     * Sets up Vars, ThreadUtils, every Manager, then registers
     * events to this class (TEMPORARY WILL REMOVE IN FINAL RELEASE)
     */
    @Override
    public void onEnable() {
        ConfigManager.setup(this, "config");
        Vars.setup();
        ThreadUtils.setup(this);
        FileManager.setup(this);
        WorldManager.setup(this);
        PlayerManager.setup(this);
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    /**Tells the WorldManager that we're disabling. Doesn't do anything atm*/
    @Override
    public void onDisable() {
        PlayerManager.done();
        WorldManager.done();
    }

    /**TEMPORARY housing of commands to test shit.*/
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().equalsIgnoreCase("/pkr")) {
            WorldManager.newBuildArena(event.getPlayer());
            event.setCancelled(true);
        } else if (event.getMessage().startsWith("/pkr")) {
            String[] args = event.getMessage().split(" ");
            WorldManager.newBuildArena(event.getPlayer(), args[1]);
            event.setCancelled(true);
        } else if (event.getMessage().startsWith("/play")) {
            String[] args = event.getMessage().split(" ");
            WorldManager.newMapArena(event.getPlayer(), args[1], args[2]);
            event.setCancelled(true);
        } else if (event.getMessage().startsWith("/delete")) {
            String[] args = event.getMessage().split(" ");
            SchemUtils.deleteSchematic(event.getPlayer().getName(), args[1]);
            event.setCancelled(true);
        } else if (event.getMessage().startsWith("/save")) {
            String[] args = event.getMessage().split(" ");
            WorldManager.saveBuildArena(event.getPlayer(), args[1]);
            event.setCancelled(true);
        } else if (event.getMessage().startsWith("/b2s")) {
            PlayerManager.backToSpawn(event.getPlayer());
            event.setCancelled(true);
        }
    }
}
