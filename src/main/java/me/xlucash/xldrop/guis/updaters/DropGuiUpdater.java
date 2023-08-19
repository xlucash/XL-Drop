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

public class DropGuiUpdater {
    private final DropMain plugin;
    private final ConfigManager configManager;

    public DropGuiUpdater(DropMain plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    public void updateGuiForPlayer(Player player, DropGui dropGui) {
        for (String item : configManager.getConfigurationSection("drops")) {
            ItemStack guiItem = dropGui.getInventory().getItem(configManager.getSlotForItem(item));
            if (guiItem != null && guiItem.getType().name().equals(item)) {
                ItemMeta meta = guiItem.getItemMeta();
                List<String> lore = meta.getLore();
                if (lore == null) lore = new ArrayList<>();
                if (lore.size() == 1) {
                    lore.add(plugin.getDatabaseManager().isDropEnabled(player.getUniqueId(), item) ? Message.DROP_ENABLED.getText() : Message.DROP_DISABLED.getText());
                } else if (lore.size() > 1) {
                    lore.set(1, plugin.getDatabaseManager().isDropEnabled(player.getUniqueId(), item) ? Message.DROP_ENABLED.getText() : Message.DROP_DISABLED.getText());
                }
                meta.setLore(lore);
                guiItem.setItemMeta(meta);
            }
        }
    }
}
