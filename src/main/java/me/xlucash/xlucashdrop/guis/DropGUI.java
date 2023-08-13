package me.xlucash.xlucashdrop.guis;

import me.xlucash.xlucashdrop.DropMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DropGUI {

    private final DropMain plugin;
    private final Inventory inventory;
    private final Map<String, Integer> itemSlots;

    public DropGUI(DropMain plugin) {
        this.plugin = plugin;
        this.inventory = Bukkit.createInventory(null, 36, "Drop ze stone");

        this.itemSlots = new HashMap<>();
        itemSlots.put("DIAMOND", 10);
        itemSlots.put("EMERALD", 11);
        itemSlots.put("GOLD_INGOT", 12);
        itemSlots.put("IRON_INGOT", 13);
        itemSlots.put("COAL", 14);
        itemSlots.put("LAPIS_LAZULI", 15);
        itemSlots.put("REDSTONE", 16);
        itemSlots.put("NETHERITE_INGOT", 22);
    }

    public void open(Player player) {
        populateItemsForPlayer(player);
        player.openInventory(inventory);
    }

    private void populateItemsForPlayer(Player player) {
        for (String key : plugin.getConfig().getConfigurationSection("drops").getKeys(false)) {
            Material material = Material.valueOf(key.toUpperCase());
            double chance = plugin.getConfig().getDouble("drops." + key + ".chance");
            String displayName = plugin.getConfig().getString("drops." + key + ".displayName");
            boolean isEnabled = plugin.getDatabaseManager().isDropEnabled(player.getUniqueId(), key);

            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.WHITE + displayName);
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Szansa: " + ChatColor.WHITE + chance + "%");
            lore.add(isEnabled ? "§7Drop: §aWłączony" : "§7Drop: §cWyłączony");
            meta.setLore(lore);
            item.setItemMeta(meta);

            int slot = itemSlots.getOrDefault(key.toUpperCase(), -1);
            if (slot != -1) {
                inventory.setItem(slot, item);
            }
        }

        ItemStack blackGlassPane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = blackGlassPane.getItemMeta();
        meta.setDisplayName(" ");
        blackGlassPane.setItemMeta(meta);

        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, blackGlassPane);
            }
        }
    }

    public Inventory getInventory() {
        return this.inventory;
    }
}
