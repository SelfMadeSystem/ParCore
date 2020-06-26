package uwu.smsgamer.parcore.utils;

import org.bukkit.Bukkit;
import uwu.smsgamer.parcore.ParCore;

/**
 * Utils for Threads.
 */
public class ThreadUtils {
    static ParCore pl;

    public static void setup(ParCore parCore) {
        pl = parCore;
    }

    /**
     * Executes a <code>Runnable</code> synchronously.
     *
     * @param runnable The runnable to execute.
     */
    public static void syncExec(Runnable runnable) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(pl, runnable);
    }

    /**
     * Executes a <code>Runnable</code> synchronously with a delay in ticks.
     *
     * @param runnable The runnable to execute.
     * @param delayTicks Delay in ticks.
     */
    public static void syncExDe(Runnable runnable, long delayTicks) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(pl, runnable, delayTicks);
    }

    //sync repeating
    public static void syncRep(Runnable runnable, long spacing) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(pl, runnable, 0, spacing);
    }

    public static void syncRep(Runnable runnable, long spacing, long delayTicks) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(pl, runnable, delayTicks, spacing);
    }
}
