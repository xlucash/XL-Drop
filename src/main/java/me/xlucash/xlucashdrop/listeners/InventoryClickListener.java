package me.xlucash.xlucashdrop.listeners;

import me.xlucash.xlucashdrop.DropMain;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class InventoryClickListener implements Listener {
    private final DropMain plugin;
    private final Map<UUID, Long> lastClickTime = new HashMap<>();

    public InventoryClickListener(DropMain plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(!event.getView().getTitle().equals("Drop ze stone")) {
            return;
        }
        event.setCancelled(true);

        if (event.getClickedInventory() != event.getView().getTopInventory()) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        long currentTime = System.currentTimeMillis();

        if (lastClickTime.containsKey(player.getUniqueId())) {
            long timeSinceLastClick = currentTime - lastClickTime.get(player.getUniqueId());
            if (timeSinceLastClick < 250) { // 250ms
                player.sendMessage("§cNie klikaj tak szybko!");
                return;
            }
        }

        lastClickTime.put(player.getUniqueId(), currentTime);

        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem != null && clickedItem.getType() != Material.BLACK_STAINED_GLASS_PANE) {
            String itemName = clickedItem.getType().name();
            String displayName = plugin.getConfig().getString("drops." + itemName + ".displayName");
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
            if ("COBBLESTONE".equals(itemName)) {
                player.sendMessage("§7Status dropu " + ChatColor.WHITE + "Cobblestone" + " §7zostal zmieniony na: " + (!currentStatus ? "§aWłączony" : "§cWyłączony"));
            } else {
                player.sendMessage("§7Status dropu " + ChatColor.WHITE + displayName + " §7zostal zmieniony na: " + (!currentStatus ? "§aWłączony" : "§cWyłączony"));
            }
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
        updateItemLore(new ItemStack(Material.COBBLESTONE), plugin.getDatabaseManager().isDropEnabled(player.getUniqueId(), "COBBLESTONE"));
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
