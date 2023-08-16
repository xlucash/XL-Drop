package me.xlucash.xlucashdrop.guis;

import me.xlucash.xlucashdrop.DropMain;
import me.xlucash.xlucashdrop.enums.Message;
import me.xlucash.xlucashdrop.hooks.SuperiorSkyblockHook;
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
        this.inventory = Bukkit.createInventory(null, 45, Message.GUI_TITLE.getText());

        this.itemSlots = new HashMap<>();
        itemSlots.put("DIAMOND", 10);
        itemSlots.put("EMERALD", 11);
        itemSlots.put("GOLD_INGOT", 12);
        itemSlots.put("IRON_INGOT", 13);
        itemSlots.put("COAL", 14);
        itemSlots.put("LAPIS_LAZULI", 15);
        itemSlots.put("REDSTONE", 16);
        itemSlots.put("NETHERITE_INGOT", 22);
        itemSlots.put("COBBLESTONE",33);
    }

    public void open(Player player) {
        populateItemsForPlayer(player);
        player.openInventory(inventory);
    }

    private void populateItemsForPlayer(Player player) {
        for (String key : plugin.getConfig().getConfigurationSection("drops").getKeys(false)) {
            Material material = Material.valueOf(key.toUpperCase());
            double chance = plugin.getConfig().getDouble("drops." + key + ".chance");
            chance += SuperiorSkyblockHook.getDropChanceMultiplier(player);
            String displayName = plugin.getConfig().getString("drops." + key + ".displayName");
            boolean isEnabled = plugin.getDatabaseManager().isDropEnabled(player.getUniqueId(), key);

            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.WHITE + displayName);
            List<String> lore = new ArrayList<>();
            lore.add(String.format(ChatColor.GRAY + Message.CHANCE_LORE.getText(), ChatColor.GOLD + String.valueOf(chance)));
            lore.add(isEnabled ? Message.DROP_ENABLED.getText() : Message.DROP_DISABLED.getText());
            meta.setLore(lore);
            item.setItemMeta(meta);

            int slot = itemSlots.getOrDefault(key.toUpperCase(), -1);
            if (slot != -1) {
                inventory.setItem(slot, item);
            }

            prepareCobblestoneSlot(player);
        }

        ItemStack blackGlassPane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = blackGlassPane.getItemMeta();
        meta.setDisplayName(Message.EMPTY_SLOT_NAME.getText());
        blackGlassPane.setItemMeta(meta);

        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, blackGlassPane);
            }
        }
    }

    private void prepareCobblestoneSlot(Player player) {
        ItemStack cobbleItem = new ItemStack(Material.COBBLESTONE);
        ItemMeta cobbleMeta = cobbleItem.getItemMeta();
        List<String> cobbleLore = new ArrayList<>();
        cobbleLore.add(Message.EMPTY_SLOT_NAME.getText());
        cobbleLore.add(plugin.getDatabaseManager().isDropEnabled(player.getUniqueId(), "COBBLESTONE") ? "§7Drop: §aWłączony" : "§7Drop: §cWyłączony");
        cobbleMeta.setLore(cobbleLore);
        cobbleMeta.setDisplayName(ChatColor.WHITE + "Kamien");
        cobbleItem.setItemMeta(cobbleMeta);
        inventory.setItem(itemSlots.get("COBBLESTONE"), cobbleItem);
    }

    public Inventory getInventory() {
        return this.inventory;
    }
}
