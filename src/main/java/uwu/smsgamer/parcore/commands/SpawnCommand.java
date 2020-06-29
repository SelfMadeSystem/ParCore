package uwu.smsgamer.parcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import uwu.smsgamer.parcore.managers.PlayerManager;
import uwu.smsgamer.parcore.utils.Chat;

import java.util.*;

public class SpawnCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            if (args.length == 0)
                Chat.send(sender, "&4Players only or specify player!");
            else {
                Player p = Bukkit.getPlayer(args[0]);
                if (p == null) {
                    Chat.send(sender, "&4Player: &7" + args[0] + "&4 is not online or does not exist!");
                } else {
                    PlayerManager.backToSpawn(p);
                    Chat.send(sender, "&aSent &7" + p.getName() + " &aback to spawn.");
                }
            }
            return true;
        }
        PlayerManager.backToSpawn((Player) sender);
        Chat.send(sender, "&aSent back to spawn.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }
}
