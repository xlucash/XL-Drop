package me.xlucash.xldrop.handlers;

import me.xlucash.xldrop.DropMain;
import me.xlucash.xldrop.config.ConfigManager;
import me.xlucash.xldrop.enums.Message;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Handles inventory interactions related to the plugin's GUI.
 */
public class InventoryHandler {
    private static final long CLICK_INTERVAL = 250; // 250ms
    private final DropMain plugin;
    private final ConfigManager configManager;

    public InventoryHandler(DropMain plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    /**
     * Handles the inventory click event for the plugin's GUI.
     * Ensures that rapid clicks are prevented and updates the drop status for items.
     * @param event The inventory click event.
     * @param lastClickTime A map storing the last click time for each player.
     */
    public void handleInventoryClick(InventoryClickEvent event, Map<UUID, Long> lastClickTime) {
        // Check if the clicked inventory is the plugin's GUI.
        if(!event.getView().getTitle().equals(Message.GUI_TITLE.getText())) {
            return;
        }
        event.setCancelled(true);

        // Ensure the clicked inventory is the GUI inventory and not the player inventory.
        if (event.getClickedInventory() != event.getView().getTopInventory()) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        long currentTime = System.currentTimeMillis();

        // Prevent rapid clicks.
        if (lastClickTime.containsKey(player.getUniqueId())) {
            long timeSinceLastClick = currentTime - lastClickTime.get(player.getUniqueId());
            if (timeSinceLastClick < CLICK_INTERVAL) {
                player.sendMessage(Message.FAST_CLICK.getText());
                return;
            }
        }

        lastClickTime.put(player.getUniqueId(), currentTime);

        ItemStack clickedItem = event.getCurrentItem();

        // Handle item interactions.
        if (clickedItem != null && clickedItem.getType() != Material.BLACK_STAINED_GLASS_PANE) {
            String itemName = clickedItem.getType().name();
            String displayName = plugin.getConfig().getString("drops." + itemName + ".displayName");
            boolean currentStatus = plugin.getDatabaseManager().isDropEnabled(player.getUniqueId(), itemName);
            plugin.getDatabaseManager().setDropEnabled(player.getUniqueId(), itemName, !currentStatus);

            ItemMeta meta = clickedItem.getItemMeta();
            List<String> lore = meta.getLore();
            if (lore == null) lore = new ArrayList<>();

            // Update item lore based on the drop status.
            if (lore.size() == 1) {
                lore.add(!currentStatus ? Message.DROP_ENABLED.getText() : Message.DROP_DISABLED.getText());
            } else if (lore.size() > 1) {
                lore.set(1, !currentStatus ? Message.DROP_ENABLED.getText() : Message.DROP_DISABLED.getText());
            }

            meta.setLore(lore);
            clickedItem.setItemMeta(meta);
            String statusMessage = String.format(Message.DROP_STATUS_CHANGED.getText(),
                    ChatColor.WHITE + (itemName.equals("COBBLESTONE") ? "Cobblestone" : displayName),
                    !currentStatus ? Message.STATUS_ENABLED.getText() : Message.STATUS_DISABLED.getText());
            player.sendMessage(statusMessage);
        }
    }
}
