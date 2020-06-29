package uwu.smsgamer.parcore.commands;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import uwu.smsgamer.parcore.managers.GuiManager;
import uwu.smsgamer.parcore.utils.Chat;

import java.util.List;
import java.util.stream.Collectors;

public class MapsCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            Chat.send(sender, ChatColor.RED + "Players only!");
            return true;
        }
        if (args.length == 0) {
            GuiManager.getManager((Player) sender).openMapsGui("");
        } else {
            GuiManager.getManager((Player) sender).openMapsGui(args[0]);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> l = Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        l.add("mine");
        return l;
    }
}
