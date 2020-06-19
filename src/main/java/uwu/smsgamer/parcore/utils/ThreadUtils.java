package uwu.smsgamer.parcore.utils;

import org.bukkit.Bukkit;
import uwu.smsgamer.parcore.ParCore;

public class ThreadUtils {
    static ParCore pl;

    public static void setup(ParCore parCore) {
        pl = parCore;
    }

    public static void syncExec(Runnable runnable) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(pl, runnable);
    }

    public static void syncExDe(Runnable runnable, long delayTicks) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(pl, runnable, delayTicks);
    }
}
