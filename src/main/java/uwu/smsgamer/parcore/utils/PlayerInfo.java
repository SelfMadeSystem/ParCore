package uwu.smsgamer.parcore.utils;

import com.sk89q.worldedit.Vector;
import lombok.*;
import org.bukkit.Location;

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

    public PlayerInfo(Vector min, Vector max, Mode mode, Location respLoc, String map) {
        this.min = min;
        this.max = max;
        this.mode = mode;
        this.respLoc = respLoc;
        this.map = map;
    }

    @Override
    public String toString() {
        return "<" + min + ", " + max + ", " + mode + ", " + respLoc + ">";
    }

    public enum Mode {
        SPAWN(true, false, false),
        MAKE(false, true, false),
        PLAYING(true, false, true),
        TESTING(true, false, true);
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
