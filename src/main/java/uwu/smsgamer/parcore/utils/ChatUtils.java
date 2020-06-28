package uwu.smsgamer.parcore.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import uwu.smsgamer.parcore.Vars;

public class ChatUtils {
    public static void sendMessage(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Vars.prefix + message));
    }
}
