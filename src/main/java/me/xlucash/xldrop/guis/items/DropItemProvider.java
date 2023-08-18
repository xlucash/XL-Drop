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

import java.util.ArrayList;
import java.util.List;

public class DropItemProvider {
    private final DropMain plugin;
    private final ConfigManager configManager;
    private final SuperiorSkyblockHook SuperiorSkyblockHook;

    public DropItemProvider(DropMain plugin, ConfigManager configManager, SuperiorSkyblockHook SuperiorSkyblockHook) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.SuperiorSkyblockHook = SuperiorSkyblockHook;
    }

    public ItemStack createDropItem(String key, Player player) {
        Material material = Material.valueOf(key.toUpperCase());
        double chance = configManager.getChanceForItem(key);
        chance += SuperiorSkyblockHook.getDropChanceMultiplier(player);
        String displayName = configManager.getDisplayNameForItem(key);
        boolean isEnabled = plugin.getDatabaseManager().isDropEnabled(player.getUniqueId(), key);

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + displayName);
        List<String> lore = new ArrayList<>();
        lore.add(String.format(ChatColor.GRAY + Message.CHANCE_LORE.getText(), ChatColor.GOLD + String.valueOf(chance)));
        lore.add(isEnabled ? Message.DROP_ENABLED.getText() : Message.DROP_DISABLED.getText());
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }
}
