package uwu.smsgamer.parcore.managers;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.*;
import org.bukkit.*;
import org.bukkit.entity.Player;
import uwu.smsgamer.parcore.*;
import uwu.smsgamer.parcore.utils.BuildUtils;

public class WorldManager {
    static final String worldName = "ParCoreWorld";
    static ParCore pl;
    static World world;
    static int last = 0;
    static BaseBlock wallBlock;

    public static void setup(ParCore plugin) {
        pl = plugin;
        WorldCreator wc = new WorldCreator(worldName);
        wc.type(WorldType.FLAT);
        wc.generatorSettings("3;minecraft:air;127;");
        Bukkit.createWorld(wc);
        world = Bukkit.getWorld(worldName);
        world.setAutoSave(false);
        wallBlock = new ImmutableDatalessBlock(Vars.wallMaterial.getId());
    }

    public static void done() {
        //Bukkit.getWorldContainer(). TODO: Remove it <3
    }

    public static void newBuildArena(Player player) {
        //EditSession session = new EditSessionBuilder(new BukkitWorld(world)).fastmode(true).build();
        //new Thread(() -> {
        Vector min = new Vector((last * Vars.spacing), 0, (last * Vars.spacing));
        Vector max = new Vector(Vars.size.getBlockX() + (last * Vars.spacing), 255, Vars.size.getBlockZ() + (last * Vars.spacing));
        PlayerManager.playerChanged(player, min, max);
        /*try {
            session.makeWalls(new CuboidRegion(min, max), (Pattern) position -> wallBlock);
            session.setBlock(new Vector(min.getX() + 1, 128, min.getZ() + 1), wallBlock);
        } catch (WorldEditException e) {
            e.printStackTrace();
        }*/
        BuildUtils.setupArena(world, min, max);
        player.teleport(new Location(world, min.getX() + 4, 128, min.getZ() + 4));
        //}).start();

        last++;
    }
}
