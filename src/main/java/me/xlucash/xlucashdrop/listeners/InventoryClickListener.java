package me.xlucash.xlucashdrop.listeners;

import me.xlucash.xlucashdrop.DropMain;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {
    private final DropMain plugin;

    public InventoryClickListener(DropMain plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(!event.getView().getTitle().equals("Drop ze stone")) {
            return;
        }
        event.setCancelled(true);
    }
}
