package me.xlucash.xldrop.listeners;

import me.xlucash.xldrop.DropMain;
import me.xlucash.xldrop.config.ConfigManager;
import me.xlucash.xldrop.guis.updaters.GuiRefresher;
import me.xlucash.xldrop.handlers.InventoryHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.*;

/**
 * Listener responsible for handling inventory click events.
 */
public class InventoryClickListener implements Listener {
    private final DropMain plugin;
    private final ConfigManager configManager;
    // Stores the last click time of players to prevent rapid clicks
    private final Map<UUID, Long> lastClickTime = new HashMap<>();
    private final InventoryHandler inventoryHandler;
    private final GuiRefresher guiRefresher;

    public InventoryClickListener(DropMain plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.inventoryHandler = new InventoryHandler(plugin, configManager);
        this.guiRefresher = new GuiRefresher(plugin);
    }

    /**
     * Handles the inventory click event.
     * Processes the click through the InventoryHandler and updates the GUI if necessary.
     * @param event The inventory click event.
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        inventoryHandler.handleInventoryClick(event, lastClickTime);
        guiRefresher.refreshGuiForPlayer((Player) event.getWhoClicked(), event.getInventory());
    }
}
