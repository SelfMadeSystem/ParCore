package uwu.smsgamer.parcore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import uwu.smsgamer.parcore.managers.*;
import uwu.smsgamer.parcore.utils.*;

import java.util.List;

public class EditCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            Chat.send(sender, "&cPlayers only.");
            return true;
        }
        PlayerInfo info = PlayerManager.players.get(sender.getName());
        String[] split = info.getMap().split(":");
        if (split.length == 1 || split[1] == null || split[1].isEmpty()) {
            if (args.length == 0)
                GuiManager.getManager((Player) sender).openSelector((click, gui, elm) -> {
                    if (click.getType().isLeftClick()) {
                        GuiManager.getManager((Player) sender).openMapSettings(elm.name);
                    } else {
                        WorldManager.newBuildArena((Player) sender, elm.name);
                        Chat.send(sender, "&aYou are now editing: &6" + elm.name);
                    }
                    return true;
                }, ChatColor.RESET + "Left click to edit map properties.", ChatColor.RESET + "Right click to edit map.");
            else
                GuiManager.getManager((Player) sender).openMapSettings(args[1]);
        } else {
            if (args.length == 0) {
                GuiManager.getManager((Player) sender).openMapSettings(info.getMap());
            } else {
                GuiManager.getManager((Player) sender).openMapSettings(args[0]);
            }
        }

        // if (WorldManager.saveBuildArena((Player) sender, args[1], args.length == 2 ? Material.AIR : Material.getMaterial(args[2]),
        //  args.length > 3 ? MapFile.MapMode.getMode(args[3]) : MapFile.MapMode.NORMAL))
        //    Chat.send(sender, "&aSuccessfully saved!");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
