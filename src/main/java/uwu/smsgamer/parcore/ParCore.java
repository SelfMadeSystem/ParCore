package uwu.smsgamer.parcore;

import org.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.dependency.Dependency;
import org.bukkit.plugin.java.annotation.plugin.*;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import uwu.smsgamer.parcore.commands.*;
import uwu.smsgamer.parcore.managers.*;
import uwu.smsgamer.parcore.utils.ThreadUtils;

/**
 * Main class for this plugin.
 */
@Plugin(name = "ParCore", version = "1.0")
@Description("Parkour core for MatrixPvP.")
@Author("Sms_Gamer_3808")
@Dependency("LuckPerms")
@Dependency("WorldEdit")
@Command(name = "maps", desc = "Lists your maps and all public maps.", usage = "/maps [mine, player name]")
@Command(name = "play", desc = "Plays somebody's map if it's published. Plays your own whether or not it's published.",
  usage = "/play [player name] [map name]")
@Command(name = "publish", desc = "Publishes the map you're working on or another map. " +
  "If you haven't verified it yet, you will play the map and once you verified it, then it will be published.",
  aliases = "upload", usage = "/publish [map name]")
@Command(name = "save", desc = "Saves the map you're working on.", usage = "/save [map name]")
@Command(name = "test", desc = "Tests the map you're working on or another one of your maps." +
  "If you complete it, it will be verified.", usage = "/test [map name]")
public final class ParCore extends JavaPlugin /*implements Listener*/ {

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
        setExec("maps", new MapsCommand());
        setExec("play", new PlayCommand());
        setExec("publish", new PublishCommand());
        setExec("save", new SaveCommand());
        setExec("test", new TestCommand());
        //Bukkit.getPluginManager().registerEvents(this, this);
    }

    private void setExec(String cmd, TabExecutor executor) {
        PluginCommand c = getCommand(cmd);
        c.setExecutor(executor);
        c.setTabCompleter(executor);
    }

    /**
     * Tells the WorldManager that we're disabling. Doesn't do anything atm
     */
    @Override
    public void onDisable() {
        PlayerManager.done();
        WorldManager.done();
        FileManager.done();
    }

    /*TEMPORARY housing of commands to test shit.
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
            MapFile file = FileManager.getMapFile(args[1], args[2]);
            if (file.isPublished()) {
                event.getPlayer().sendMessage("Have fun!");
                WorldManager.newMapArena(event.getPlayer(), args[1], args[2]);
            } else {
                event.getPlayer().sendMessage("Map not published!");
            }
            event.setCancelled(true);
        } else if (args[0].equalsIgnoreCase("/test")) {
            WorldManager.newMapArena(event.getPlayer(), event.getPlayer().getName(), args[1], false);
            event.getPlayer().sendMessage("Testing map: " + args[1]);
            event.setCancelled(true);
        } else if (args[0].equalsIgnoreCase("/upload")) {
            MapFile file = FileManager.getMapFile(event.getPlayer().getName(), args[1]);
            if (file.isVerified()) {
                file.setPublished(true);
                event.getPlayer().sendMessage("Oki Doki!");
            } else {
                WorldManager.newMapArena(event.getPlayer(), event.getPlayer().getName(), args[1], true);
                event.getPlayer().sendMessage("Verify first!");
            }
            event.setCancelled(true);
        } else if (args[0].equalsIgnoreCase("/delete")) {
            SchemUtils.deleteSchematic(event.getPlayer().getName(), args[1]);
            event.setCancelled(true);
        } else if (args[0].equalsIgnoreCase("/save")) {
            if (WorldManager.saveBuildArena(event.getPlayer(), args[1], args.length == 2 ? Material.AIR : Material.getMaterial(args[2]),
              args.length > 3 ? MapFile.MapMode.getMode(args[3]) : MapFile.MapMode.NORMAL))
                event.getPlayer().sendMessage("Saved!");
            event.setCancelled(true);
        } else if (args[0].equalsIgnoreCase("/b2s")) {
            PlayerManager.backToSpawn(event.getPlayer());
            event.getPlayer().sendMessage("Sent back to spawn.");
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
                } else if (args[1].equalsIgnoreCase("maps")) {
                    if (args.length == 2) {
                        event.getPlayer().sendMessage(FileManager.mapFiles.toString());
                    } else if (args[2].equalsIgnoreCase("all")) {
                        event.getPlayer().sendMessage(FileManager.mapFiles.toString());
                    } else if (args[2].equalsIgnoreCase("name")) {
                        event.getPlayer().sendMessage(FileManager.mapFiles.values().stream().map(mapFile -> mapFile.getPlayer() + ";" + mapFile.getName()).collect(Collectors.joining("\n")));
                    } else if (args[2].equalsIgnoreCase("vap")) { //verified and published
                        event.getPlayer().sendMessage(FileManager.mapFiles.values().stream().map(m -> m.getPlayer() + ";" + m.getName() + ":" + m.isVerified() + ":" + m.isPublished()).collect(Collectors.joining("\n")));
                    }
                }
            }
        }
    }*/
}

