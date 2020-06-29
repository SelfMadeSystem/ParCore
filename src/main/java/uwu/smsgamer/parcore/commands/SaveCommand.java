package uwu.smsgamer.parcore.commands;

import org.bukkit.command.*;
import org.bukkit.entity.Player;
import uwu.smsgamer.parcore.managers.*;
import uwu.smsgamer.parcore.utils.*;

import java.util.List;

public class SaveCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            Chat.send(sender, "&cPlayers only.");
            return true;
        }
        PlayerInfo info = PlayerManager.players.get(sender.getName());
        if (!info.getMode().equals(PlayerInfo.Mode.MAKE)) {
            Chat.send(sender, "&cYou are not in editing mode. If this in an error, please report to administration.");
            return true;
        }
        String[] split = info.getMap().split(":");
        if (split.length == 1 || split[1] == null || split[1].isEmpty()) {
            GuiManager.getManager((Player) sender).openMapSettings("");
        } else {
            if (args.length == 0) {
                MapFile mf = FileManager.getMapFile(sender.getName(), split[1]);
                if (WorldManager.saveBuildArena((Player) sender, mf.getName(), mf.getDescription(),
                  mf.getWallMaterial(), mf.getMode()))
                    Chat.send(sender, "&aSuccessfully saved!");
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
