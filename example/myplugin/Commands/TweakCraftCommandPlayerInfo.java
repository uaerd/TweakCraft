package example.myplugin.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

import example.myplugin.TweakCraft;

public class TweakCraftCommandPlayerInfo implements CommandExecutor, Listener {
    private TweakCraft plugin;

    public TweakCraftCommandPlayerInfo(TweakCraft plugin) {
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
            sender.sendMessage("Usage: /PlayerInfo [player_name]");
            return true;
        }

        String playerName = args[0];
        Player targetPlayer = Bukkit.getPlayer(playerName);

        if (targetPlayer != null) {
            // Register this class as listener for InventoryClickEvent
            Bukkit.getPluginManager().registerEvents(this, plugin);

            // ดำเนินการแสดงข้อมูลผู้เล่นใน GUI
            displayPlayerInfo(player, targetPlayer);
        } else {
            player.sendMessage("Player " + playerName + " is not online or does not exist.");
        }
        return true;
    }

    // เมธอดสำหรับแสดงข้อมูลผู้เล่นใน GUI
    private void displayPlayerInfo(Player sender, Player targetPlayer) {
        Inventory gui = Bukkit.createInventory(null, 27, "Info of " + targetPlayer.getName());

        // สร้างไอเท็มที่ใช้แสดงข้อมูลต่าง ๆ
        ItemStack nameItem = createGuiItem(Material.NAME_TAG, "Name", targetPlayer.getName());
        ItemStack worldItem = createGuiItem(Material.GRASS_BLOCK, "World", targetPlayer.getWorld().getName());
        ItemStack locationItem = createGuiItem(Material.COMPASS, "Location", targetPlayer.getLocation().getBlockX() + ", " + targetPlayer.getLocation().getBlockY() + ", " + targetPlayer.getLocation().getBlockZ());
        ItemStack healthItem = createGuiItem(Material.REDSTONE, "Health", String.valueOf(targetPlayer.getHealth()));
        ItemStack foodLevelItem = createGuiItem(Material.BREAD, "Food Level", String.valueOf(targetPlayer.getFoodLevel()));

        // วางไอเท็มใน GUI
        gui.setItem(10, nameItem);
        gui.setItem(12, worldItem);
        gui.setItem(14, locationItem);
        gui.setItem(16, healthItem);
        gui.setItem(22, foodLevelItem);

        // เปิดอินเวนทอรี่ GUI ให้กับผู้เรียกใช้คำสั่ง
        sender.openInventory(gui);
    }

    // เมธอดสำหรับสร้างไอเท็มที่ใช้ใน GUI
    private ItemStack createGuiItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }

    // Event handler สำหรับตรวจสอบและยกเลิกการคลิกใน GUI
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().startsWith("Info of ")) {
            event.setCancelled(true); // ยกเลิกการดึงของออกจาก GUI
        }
        
        if (event.getView().getTitle().startsWith("Inventory of ")) {
            event.setCancelled(true); // ยกเลิกการดึงของออกจาก GUI
        }
    }
}

