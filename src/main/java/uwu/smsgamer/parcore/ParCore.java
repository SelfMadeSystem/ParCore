package uwu.smsgamer.parcore;

import org.bukkit.*;
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
            return;
        }
        String[] args = event.getMessage().split(" ");
        if (args[0].equalsIgnoreCase("/pkr")) {
            WorldManager.newBuildArena(event.getPlayer(), args[1]);
            event.setCancelled(true);
        } else if (args[0].equalsIgnoreCase("/play")) {
            WorldManager.newMapArena(event.getPlayer(), args[1], args[2]);
            event.setCancelled(true);
        } else if (args[0].equalsIgnoreCase("/delete")) {
            SchemUtils.deleteSchematic(event.getPlayer().getName(), args[1]);
            event.setCancelled(true);
        } else if (args[0].equalsIgnoreCase("/save")) {
            WorldManager.saveBuildArena(event.getPlayer(), args[1], args.length == 2 ? Material.AIR : Material.getMaterial(args[2]));
            event.setCancelled(true);
        } else if (args[0].equalsIgnoreCase("/b2s")) {
            PlayerManager.backToSpawn(event.getPlayer());
            event.setCancelled(true);
        } else if (args[0].equalsIgnoreCase("/pkrd")) {
            event.setCancelled(true);
            if (args.length == 1) {
                event.getPlayer().sendMessage(PlayerManager.playerList.toString());
            } else {
                if (args[1].equalsIgnoreCase("playerlist")) {
                    event.getPlayer().sendMessage(PlayerManager.playerList.toString());
                } else if (args[1].equalsIgnoreCase("players")) {
                    event.getPlayer().sendMessage(PlayerManager.players.toString());
                } else if (args[1].equalsIgnoreCase("pPlayers")) {
                    event.getPlayer().sendMessage(PPlayer.pPlayers.toString());
                }
            }
        }
    }
}
