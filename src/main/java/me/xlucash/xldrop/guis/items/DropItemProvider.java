package me.xlucash.xldrop.guis.items;

import me.xlucash.xldrop.DropMain;
import me.xlucash.xldrop.config.ConfigManager;
import me.xlucash.xldrop.enums.Message;
import me.xlucash.xldrop.hooks.SuperiorSkyblockHook;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides functionality to create drop items for the GUI.
 */
public class DropItemProvider {
    private final DropMain plugin;
    private final ConfigManager configManager;
    private final SuperiorSkyblockHook SuperiorSkyblockHook;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    public DropItemProvider(DropMain plugin, ConfigManager configManager, SuperiorSkyblockHook SuperiorSkyblockHook) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.SuperiorSkyblockHook = SuperiorSkyblockHook;
    }

    /**
     * Creates an ItemStack representing a drop item for the GUI.
     *
     * @param key The key representing the item type.
     * @param player The player for whom the item is being created.
     * @return An ItemStack representing the drop item.
     */
    public ItemStack createDropItem(String key, Player player) {
        Material material = Material.valueOf(key.toUpperCase());
        double dropChance = configManager.getChanceForItem(key) + SuperiorSkyblockHook.getDropChanceMultiplier(player);
        String displayName = configManager.getDisplayNameForItem(key);
        boolean dropEnabled = plugin.getDatabaseManager().isDropEnabled(player.getUniqueId(), key);

        // Create the ItemStack.
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + displayName);
        List<String> lore = new ArrayList<>();
        lore.add(String.format(ChatColor.GRAY + Message.CHANCE_LORE.getText(), ChatColor.GOLD + df.format(dropChance)));
        lore.add(dropEnabled ? Message.DROP_ENABLED.getText() : Message.DROP_DISABLED.getText());
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }
}
