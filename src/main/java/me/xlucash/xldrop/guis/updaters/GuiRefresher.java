package me.xlucash.xldrop.guis.updaters;

import me.xlucash.xldrop.DropMain;
import me.xlucash.xldrop.enums.Message;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class GuiRefresher {
    private final DropMain plugin;

    public GuiRefresher(DropMain plugin) {
        this.plugin = plugin;
    }

    public void refreshGuiForPlayer(Player player, Inventory inventory) {
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
            lore.set(1, isEnabled ? Message.DROP_ENABLED.getText() : Message.DROP_DISABLED.getText());
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
    }
}
