package uwu.smsgamer.parcore.utils;

import com.sk89q.worldedit.Vector;
import lombok.*;
import org.bukkit.*;

import java.util.*;

/**
 * An entry with four (yes 4) variable types kuz 2 isn't enough.
 */
@Getter
@Setter
public class PlayerInfo {
    Vector min;
    Vector max;
    Mode mode;
    Location respLoc;
    String map;
    public final static Material redOn = Material.REDSTONE_BLOCK;
    public final static Material redOff = Material.RED_MUSHROOM;
    public final static Material greenOn = Material.EMERALD_BLOCK;
    public final static Material greenOff = Material.BROWN_MUSHROOM;
    boolean switchBlocks;
    List<Vector>[] switchBlockList = null;

    public PlayerInfo(Vector min, Vector max, Mode mode, Location respLoc, String map) {
        this.min = min;
        this.max = max;
        this.mode = mode;
        this.respLoc = respLoc;
        this.map = map == null ? "" : map;
        this.switchBlocks = false;
    }

    public void changeBlocks() {
        if (this.min == null || this.max == null || this.respLoc == null)
            return;
        this.switchBlocks = !this.switchBlocks;
        if (switchBlockList == null) {
            switchBlockList = BuildUtils.getAllMaterialLocations(Arrays.asList(redOn, greenOn),
              //new Entry[]{new Entry(this.switchBlocks ? redOn : greenOn,
              //this.switchBlocks ? redOff : greenOff),
              //new Entry(this.switchBlocks ? greenOff : redOff, this.switchBlocks ? greenOn : redOn)},
              respLoc.getWorld(),
              min, max);
        }
        World world = respLoc.getWorld();
        for (int i = 0; i < switchBlockList.length; i++) {
            List<Vector> list = switchBlockList[i];
            for (Vector vec : list) {
                if (i == 0) {
                    world.getBlockAt(vec.getBlockX(), vec.getBlockY(), vec.getBlockZ()).setType(this.switchBlocks ? redOn : redOff);
                } else {
                    world.getBlockAt(vec.getBlockX(), vec.getBlockY(), vec.getBlockZ()).setType(this.switchBlocks ? greenOff : greenOn);
                }
            }
        }
    }


    @AllArgsConstructor
    private static class Entry implements Map.Entry<Material, Material> {
        private final Material key;
        private Material value;

        @Override
        public Material getKey() {
            return key;
        }

        @Override
        public Material getValue() {
            return value;
        }

        @Override
        public Material setValue(Material v) {
            Material val = value;
            value = v;
            return val;
        }
    }

    @Override
    public String toString() {
        return "<" + min + ", " + max + ", " + mode + ", " + respLoc + ">";
    }

    public enum Mode {
        SPAWN(true, false, false, true),
        MAKE(false, true, false, true),
        PLAYING(true, false, true, false),
        TESTING(true, false, true, false),
        VERIFY(true, false, true, false);
        public boolean noBlock; //No Block
        public boolean limitH; //limit horizontal. Vertical is always limited
        public boolean playing; //Respawn on death
        public boolean noDamage; //No damage

        Mode(boolean noBlock, boolean limitH, boolean playing, boolean noDamage) {
            this.noBlock = noBlock;
            this.limitH = limitH;
            this.playing = playing;
            this.noDamage = noDamage;
        }
    }
}
