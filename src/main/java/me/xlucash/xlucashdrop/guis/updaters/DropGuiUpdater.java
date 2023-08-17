package me.xlucash.xlucashdrop.guis.updaters;

import me.xlucash.xlucashdrop.DropMain;
import me.xlucash.xlucashdrop.config.DropConfig;
import me.xlucash.xlucashdrop.enums.Message;
import me.xlucash.xlucashdrop.guis.DropGui;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class DropGuiUpdater {
    private final DropMain plugin;
    private final DropConfig dropConfig;

    public DropGuiUpdater(DropMain plugin, DropConfig dropConfig) {
        this.plugin = plugin;
        this.dropConfig = dropConfig;
    }

    public void updateGuiForPlayer(Player player, DropGui dropGui) {
        for (String item : dropConfig.getConfigurationSection("drops")) {
            ItemStack guiItem = dropGui.getInventory().getItem(dropConfig.getSlotForItem(item));
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
