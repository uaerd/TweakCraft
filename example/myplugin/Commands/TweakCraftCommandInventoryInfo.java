package example.myplugin.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.Material;

import example.myplugin.TweakCraft;

public class TweakCraftCommandInventoryInfo implements CommandExecutor {
    private TweakCraft plugin;
    
    public TweakCraftCommandInventoryInfo(TweakCraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;
        if (!plugin.isrun_command(player)) {
            sender.sendMessage("You do not have permission to use this command.");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage("Usage: /InventoryInfo [player_name]");
            return true;
        }

        String playerName = args[0];
        Player targetPlayer = sender.getServer().getPlayer(playerName);

        if (targetPlayer == null) {
            sender.sendMessage("Player " + playerName + " is not online or does not exist.");
            return true;
        }

        // เรียกเมธอดเพื่อแสดงข้อมูล Inventory ของผู้เล่นใน GUI
        displayInventoryInfo(player, targetPlayer);
        return true;
    }

    // เมธอดสำหรับแสดงข้อมูล Inventory ของผู้เล่นใน GUI
    private void displayInventoryInfo(Player sender, Player targetPlayer) {
        if (sender.getUniqueId() == targetPlayer.getUniqueId()) {
            return;
        }
        
        PlayerInventory inventory = targetPlayer.getInventory();
        ItemStack[] contents = inventory.getContents();

        // สร้างอินเวนทอรี่ใหม่สำหรับ GUI
        Inventory gui = Bukkit.createInventory(null, 54, "Inventory of " + targetPlayer.getName());

        for (ItemStack item : contents) {
            if (item != null && item.getType() != Material.AIR) {
                gui.addItem(item);
            }
        }

        // เปิดอินเวนทอรี่ GUI ให้กับผู้เรียกใช้คำสั่ง
        sender.openInventory(gui);
    }
}
