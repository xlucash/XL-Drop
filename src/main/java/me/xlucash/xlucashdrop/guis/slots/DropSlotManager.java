package me.xlucash.xlucashdrop.guis.slots;

import java.util.HashMap;
import java.util.Map;

public class DropSlotManager {
    private final Map<String, Integer> itemSlots;

    public DropSlotManager() {
        this.itemSlots = new HashMap<>();
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

    public int getSlotForItem(String itemKey) {
        return itemSlots.getOrDefault(itemKey.toUpperCase(), -1);
    }
}
