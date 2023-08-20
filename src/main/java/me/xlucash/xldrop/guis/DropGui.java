package me.xlucash.xldrop.guis;

import me.xlucash.xldrop.DropMain;
import me.xlucash.xldrop.config.ConfigManager;
import me.xlucash.xldrop.enums.Message;
import me.xlucash.xldrop.guis.items.DropItemProvider;
import me.xlucash.xldrop.guis.slots.DropSlotManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the GUI for managing drops.
 */
public class DropGui {

    private final DropMain plugin;
    private final Inventory inventory;
    private final DropItemProvider itemFactory;
    private final DropSlotManager slotManager;
    private final ConfigManager configManager;

    public DropGui(DropMain plugin, DropItemProvider itemFactory, DropSlotManager slotManager, ConfigManager configManager) {
        this.plugin = plugin;
        this.itemFactory = itemFactory;
        this.slotManager = slotManager;
        this.configManager = configManager;
        this.inventory = Bukkit.createInventory(null, 45, Message.GUI_TITLE.getText());
    }

    /**
     * Opens the drop GUI for the specified player.
     * @param player The player for whom the GUI should be opened.
     */
    public void open(Player player) {
        populateItemsForPlayer(player);
        player.openInventory(inventory);
    }

    /**
     * Populates the GUI with items specific to the player.
     * @param player The player for whom the items should be populated.
     */
    private void populateItemsForPlayer(Player player) {
        for (String key : configManager.getConfigurationSection("drops")) {
            ItemStack item = itemFactory.createDropItem(key, player);
            int slot = slotManager.getSlotForItem(key);
            if (slot != -1) {
                inventory.setItem(slot, item);
            }
        }
        prepareCobblestoneSlot(player);
        fillEmptySlots();
    }

    /**
     * Fills empty slots in the GUI with black stained-glass panes.
     */
    private void fillEmptySlots() {
        ItemStack blackGlassPane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = blackGlassPane.getItemMeta();
        meta.setDisplayName(Message.EMPTY_SLOT_NAME.getText());
        blackGlassPane.setItemMeta(meta);

        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, blackGlassPane);
            }
        }
    }

    /**
     * Prepares the cobblestone slot in the GUI.
     * The cobblestone slot is a special slot that is not configurable in the config file.
     * @param player The player for whom the cobblestone slot should be prepared.
     */
    private void prepareCobblestoneSlot(Player player) {
        ItemStack cobbleItem = new ItemStack(Material.COBBLESTONE);
        ItemMeta cobbleMeta = cobbleItem.getItemMeta();
        List<String> cobbleLore = new ArrayList<>();
        cobbleLore.add(Message.EMPTY_SLOT_NAME.getText());
        cobbleLore.add(plugin.getDatabaseManager().isDropEnabled(player.getUniqueId(), "COBBLESTONE") ? Message.DROP_ENABLED.getText() : Message.DROP_DISABLED.getText());
        cobbleMeta.setLore(cobbleLore);
        cobbleMeta.setDisplayName(ChatColor.WHITE + "Kamien");
        cobbleItem.setItemMeta(cobbleMeta);
        inventory.setItem(slotManager.getSlotForItem("COBBLESTONE"), cobbleItem);
    }

    /**
     * Returns the inventory associated with this GUI.
     * @return The inventory.
     */
    public Inventory getInventory() {
        return this.inventory;
    }
}
