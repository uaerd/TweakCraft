package example.myplugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.Inventory;
import org.bukkit.event.EventHandler;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import example.myplugin.Commands.TweakCraftCommandColor;
import example.myplugin.Commands.TweakCraftCommandInventoryInfo;
import example.myplugin.Commands.TweakCraftCommandPlayerInfo;

public class TweakCraft extends JavaPlugin implements Listener {
    private static final String PREFIX = "§b[TweakCraft]:§r ";
    private Config CONFIG;
    private badwords BadWords;
    private List<String> Admin;
    private Map<String, Object> run_command_by;

    @Override
    public void onEnable() {
        BadWords = new badwords(getDataFolder());
        CONFIG = new Config(getDataFolder());
        this.Admin = (List<String>) CONFIG.get("Admin");
        this.run_command_by = (Map<String, Object>) CONFIG.get("run-command-by");
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("InventoryInfo").setExecutor(new TweakCraftCommandInventoryInfo(this));
        getCommand("PlayerInfo").setExecutor(new TweakCraftCommandPlayerInfo(this));
        getCommand("Color").setExecutor(new TweakCraftCommandColor());
        getLogger().info("Plugin TweakCraft has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin TweakCraft has been disabled!");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        event.setJoinMessage(PREFIX + playerName + " §6Connected in the server!§f");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        event.setQuitMessage(PREFIX + playerName + " §6Disconnected in the server!§f");
    }

    @EventHandler
    public void onAnvilRename(PrepareAnvilEvent event) {
        ItemStack result = event.getResult();
        if (result != null && result.hasItemMeta()) {
            ItemMeta meta = result.getItemMeta();
            if (meta.hasDisplayName()) {
                String newName = meta.getDisplayName();
                if (BadWords.containsBadWord(newName)) {
                    meta.setDisplayName("§cUnable to name");
                    result.setItemMeta(meta);
                    event.setResult(result);
                    return;
                }
                newName = newName.replace('&', '§');
                meta.setDisplayName(newName);
                getLogger().info(newName);
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        if (BadWords.containsBadWord(message)) {
            event.setCancelled(true);
            player.sendMessage(PREFIX + "§cPlease don't type swear words in chat!");
            return;
        }
        event.setMessage(message.replace('&', '§'));
    }

    public boolean isrun_command(Player player) {
        if (run_command_by.containsKey("op") && run_command_by.get("op").equals(true) && player.isOp()) {
            return true;
        } else if (run_command_by.containsKey("Admin") && run_command_by.get("Admin").equals(true)) {
            List<UUID> adminList = (List<UUID>) run_command_by.get("Admins");
            if (adminList != null && adminList.contains(player.getUniqueId())) {
                return true;
            }
        }
        return false;
    }

   @EventHandler
    public void onPlayerItemBreak(PlayerItemBreakEvent event) {
        try {
            Player player = event.getPlayer();
            ItemStack brokenItem = event.getBrokenItem();

            if (player == null || brokenItem == null) {
                return;
            }

            PlayerInventory inventory = player.getInventory();
            Material brokenItemType = brokenItem.getType();

            // ค้นหาไอเท็มที่ชนิดเดียวกันในช่องเก็บของ
            for (ItemStack item : inventory.getContents()) {
                if (item != null && item.getType() == brokenItemType) {
                    // ย้ายไอเท็มชนิดเดียวกันมายังมือหลักของผู้เล่น
                    inventory.setItemInMainHand(item);
                    inventory.remove(item); // ลบไอเท็มนั้นออกจากช่องเก็บของ
                    player.sendMessage("Moved a similar item to your main hand.");
                    return; // หยุดการค้นหาหลังจากย้ายไอเท็มเสร็จแล้ว
                }
            }

            // แจ้งผู้เล่นหากไม่มีไอเท็มชนิดเดียวกันในช่องเก็บของ
            player.sendMessage("No similar item found in your inventory.");
        } catch (Exception e) {
            getLogger().severe("An error occurred while processing PlayerItemBreakEvent:");
            e.printStackTrace();
        }
    }
}