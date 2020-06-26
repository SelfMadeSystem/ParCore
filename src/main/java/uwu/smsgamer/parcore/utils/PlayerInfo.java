package uwu.smsgamer.parcore.utils;

import com.sk89q.worldedit.Vector;
import lombok.*;
import org.bukkit.*;

import java.util.Map;

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
    public final static Material redOff = Material.REDSTONE_WIRE;
    public final static Material greenOn = Material.EMERALD_BLOCK;
    public final static Material greenOff = Material.SAPLING;
    boolean switchBlocks;

    public PlayerInfo(Vector min, Vector max, Mode mode, Location respLoc, String map) {
        this.min = min;
        this.max = max;
        this.mode = mode;
        this.respLoc = respLoc;
        this.map = map;
        this.switchBlocks = false;
        this.changeBlocks();
    }

    public void changeBlocks() {
        if (this.min == null || this.max == null || this.respLoc == null)
            return;
        this.switchBlocks = !this.switchBlocks;
        BuildUtils.replaceMaterials(new Entry[]{new Entry(this.switchBlocks ? redOn : greenOn, this.switchBlocks ? redOff : greenOff),
            new Entry(this.switchBlocks ? greenOff : redOff, this.switchBlocks ? greenOn : redOn)},
          respLoc.getWorld(),
          min, max);
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
        SPAWN(true, false, false),
        MAKE(false, true, false),
        PLAYING(true, false, true),
        TESTING(true, false, true),
        VERIFY(true, false, true);
        public boolean noBOrD; //No Block Or Damage
        public boolean limitH; //limit horizontal. Vertical is always limited
        public boolean playing; //Respawn on death

        Mode(boolean noBlock, boolean limitH, boolean playing) {
            this.noBOrD = noBlock;
            this.limitH = limitH;
            this.playing = playing;
        }
    }
}
