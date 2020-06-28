package uwu.smsgamer.parcore.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import uwu.smsgamer.parcore.Vars;

public class Chat {
    public static void send(CommandSender player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Vars.prefix + message));
    }
}
