package me.xlucash.xlucashdrop.listeners;

import me.xlucash.xlucashdrop.DropMain;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class InventoryClickListener implements Listener {
    private final DropMain plugin;

    public InventoryClickListener(DropMain plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(!event.getView().getTitle().equals("Drop ze stone")) {
            return;
        }
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem != null && clickedItem.getType() != Material.BLACK_STAINED_GLASS_PANE) {
            String itemName = clickedItem.getType().name();
            boolean currentStatus = plugin.getDatabaseManager().isDropEnabled(player.getUniqueId(), itemName);
            plugin.getDatabaseManager().setDropEnabled(player.getUniqueId(), itemName, !currentStatus);

            ItemMeta meta = clickedItem.getItemMeta();
            List<String> lore = meta.getLore();
            if (lore == null) lore = new ArrayList<>();

            if (lore.size() == 1) {
                lore.add(!currentStatus ? "§7Drop: §aWłączony" : "§7Drop: §cWyłączony");
            } else if (lore.size() > 1) {
                lore.set(1, !currentStatus ? "§7Drop: §aWłączony" : "§7Drop: §cWyłączony");
            }

            meta.setLore(lore);
            clickedItem.setItemMeta(meta);

            player.sendMessage("§aZmieniono status dropu dla " + itemName + " na: " + (!currentStatus ? "Włączony" : "Wyłączony"));
        }

        refreshGuiForPlayer(player, event.getInventory());
    }

    private void refreshGuiForPlayer(Player player, Inventory inventory) {
        for (String item : plugin.getConfig().getConfigurationSection("drops").getKeys(false)) {
            ItemStack guiItem = inventory.getItem(plugin.getConfig().getInt("drops." + item + ".slot"));
            if (guiItem != null && guiItem.getType().name().equals(item)) {
                boolean isEnabled = plugin.getDatabaseManager().isDropEnabled(player.getUniqueId(), item);
                updateItemLore(guiItem, isEnabled);
            }
        }
    }

    private void updateItemLore(ItemStack item, boolean isEnabled) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore != null && lore.size() > 1) {
            lore.set(1, isEnabled ? "§7Drop: §aWłączony" : "§7Drop: §cWyłączony");
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
    }
}
