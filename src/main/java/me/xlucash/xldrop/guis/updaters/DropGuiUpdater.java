package me.xlucash.xldrop.guis.updaters;

import me.xlucash.xldrop.DropMain;
import me.xlucash.xldrop.config.ConfigManager;
import me.xlucash.xldrop.enums.Message;
import me.xlucash.xldrop.guis.DropGui;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the updating of the Drop GUI for players.
 */
public class DropGuiUpdater {
    private final DropMain plugin;
    private final ConfigManager configManager;

    public DropGuiUpdater(DropMain plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    /**
     * Updates the Drop GUI for the specified player.
     * @param player The player for whom the GUI should be updated.
     * @param dropGui The Drop GUI instance to be updated.
     */
    public void updateGuiForPlayer(Player player, DropGui dropGui) {
        // Iterate over all items defined in the "drops" section of the config
        for (String item : configManager.getConfigurationSection("drops")) {
            ItemStack guiItem = dropGui.getInventory().getItem(configManager.getSlotForItem(item));

            // Check if the item exists and matches the expected type
            if (guiItem != null && guiItem.getType().name().equals(item)) {
                ItemMeta meta = guiItem.getItemMeta();
                List<String> lore = meta.getLore();

                // Initialize lore if it's null
                if (lore == null) lore = new ArrayList<>();

                // Update the lore based on the drop status
                if (lore.size() == 1) {
                    lore.add(plugin.getDatabaseManager()
                            .isDropEnabled(player.getUniqueId(), item)
                            ? Message.DROP_ENABLED.getText() : Message.DROP_DISABLED.getText());
                } else if (lore.size() > 1) {
                    lore.set(
                            1, plugin.getDatabaseManager()
                                    .isDropEnabled(player.getUniqueId(), item)
                                    ? Message.DROP_ENABLED.getText() : Message.DROP_DISABLED.getText());
                }

                // Set the updated lore back to the item
                meta.setLore(lore);
                guiItem.setItemMeta(meta);
            }
        }
    }
}
