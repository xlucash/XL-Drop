package me.xlucash.xlucashdrop.utils;

import me.xlucash.xlucashdrop.DropMain;
import me.xlucash.xlucashdrop.enums.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class RecipeManager {
    private final DropMain plugin;

    public RecipeManager(DropMain plugin) {
        this.plugin = plugin;
    }

    public void registerRecipes() {
        registerGeneratorRecipe();
    }

    private void registerGeneratorRecipe() {
        ShapedRecipe generatorRecipe = new ShapedRecipe(new NamespacedKey(plugin, "stone_generator"), getGeneratorItem());
        generatorRecipe.shape("CCC", "CDC", "CCC");
        generatorRecipe.setIngredient('C', Material.COBBLESTONE);
        generatorRecipe.setIngredient('D', Material.DIAMOND);
        Bukkit.addRecipe(generatorRecipe);
    }

    public static ItemStack getGeneratorItem() {
        ItemStack generator = new ItemStack(Material.END_STONE);
        ItemMeta meta = generator.getItemMeta();
        meta.setDisplayName(Message.GENERATOR_NAME.getText());

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Po postawieniu w jego miejscu pojawi się kamień,");
        lore.add(ChatColor.GRAY + "który odnawia się co " + ChatColor.WHITE + "2 sekundy");
        lore.add(ChatColor.GRAY + "Generator możesz zniszczyć używając " + ChatColor.GOLD + "złotego kilofa");
        meta.setLore(lore);

        generator.setItemMeta(meta);
        return generator;
    }
}
