package me.xlucash.xldrop.guis.slots;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages the slots for different items in the Drop GUI.
 */
public class DropSlotManager {
    // A map to store the slot positions for each item type.
    private final Map<String, Integer> itemSlots;

    /**
     * Constructs a new DropSlotManager instance and initializes the item slots.
     */
    public DropSlotManager() {
        this.itemSlots = new HashMap<>();

        // Define the slot positions for each item type.
        itemSlots.put("DIAMOND", 10);
        itemSlots.put("EMERALD", 11);
        itemSlots.put("GOLD_INGOT", 12);
        itemSlots.put("IRON_INGOT", 13);
        itemSlots.put("COAL", 14);
        itemSlots.put("LAPIS_LAZULI", 15);
        itemSlots.put("REDSTONE", 16);
        itemSlots.put("NETHERITE_INGOT", 22);
        itemSlots.put("COBBLESTONE",33);
    }

    /**
     * Retrieves the slot position for a given item type.
     * @param itemKey The key representing the item type.
     * @return The slot position for the item, or -1 if not defined.
     */
    public int getSlotForItem(String itemKey) {
        return itemSlots.getOrDefault(itemKey.toUpperCase(), -1);
    }
}
