package example.myplugin.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TweakCraftCommandColor implements CommandExecutor {
    private String message_color = "&0 : §0text§r \n"+
    "&1 : §1text§r \n"+
    "&2 : §2text§r \n"+
    "&3 : §3text§r \n"+
    "&4 : §4text§r \n"+
    "&5 : §5text§r \n"+
    "&6 : §6text§r \n"+
    "&7 : §7text§r \n"+
    "&8 : §8text§r \n"+
    "&9 : §9text§r \n"+
    "&a : §atext§r \n"+
    "&b : §btext§r \n"+
    "&c : §ctext§r \n"+
    "&d : §dtext§r \n"+
    "&e : §etext§r \n"+
    "&f : §ftext§r \n"+
    "&k : §ktext§r \n"+
    "&l : §ltext§r \n"+
    "&m : §mtext§r \n"+
    "&n : §ntext§r \n"+
    "&o : §otext§r \n"+
    "&r : reset \n";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;
        player.sendMessage(message_color);
        return true;
    }
}
