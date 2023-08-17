package me.xlucash.xlucashdrop.guis.items;

import me.xlucash.xlucashdrop.DropMain;
import me.xlucash.xlucashdrop.config.DropConfig;
import me.xlucash.xlucashdrop.enums.Message;
import me.xlucash.xlucashdrop.hooks.SuperiorSkyblockHook;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class DropItemProvider {
    private final DropMain plugin;
    private final DropConfig dropConfig;
    private final SuperiorSkyblockHook SuperiorSkyblockHook;

    public DropItemProvider(DropMain plugin, DropConfig dropConfig, SuperiorSkyblockHook SuperiorSkyblockHook) {
        this.plugin = plugin;
        this.dropConfig = dropConfig;
        this.SuperiorSkyblockHook = SuperiorSkyblockHook;
    }

    public ItemStack createDropItem(String key, Player player) {
        Material material = Material.valueOf(key.toUpperCase());
        double chance = dropConfig.getChanceForItem(key);
        chance += SuperiorSkyblockHook.getDropChanceMultiplier(player);
        String displayName = dropConfig.getDisplayNameForItem(key);
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
