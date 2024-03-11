package me.rownox.rowmissiles;

import me.rownox.rowmissiles.objects.MissileObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.List;

public class ConfigUtils {

    private static final YamlConfiguration missilesConfig = RowMissiles.getMissilesConfig();

    public static void initMissiles() {
        for (String key : missilesConfig.getKeys(false)) {

            String name = missilesConfig.getString(key + ".name");
            String potionTypeString = missilesConfig.getString(key + ".material");
            int range = missilesConfig.getInt(key + ".range");
            int magnitude = missilesConfig.getInt(key + ".magnitude");
            int speed = missilesConfig.getInt(key + ".speed");

            PotionType potionType = PotionType.valueOf(potionTypeString.toUpperCase());

            ItemStack arrowItem = missileItem(name, potionType);

            List<String> recipeParts = missilesConfig.getStringList(key + ".recipe_parts");
            String recipeShapeString = missilesConfig.getString(key + ".recipe_shape");

            NamespacedKey namespacedKey = new NamespacedKey(RowMissiles.getInstance(), key);
            ShapedRecipe recipe = new ShapedRecipe(namespacedKey, arrowItem);
            String[] recipeShape = recipeShapeString.split("\\|");
            recipe.shape(recipeShape[0], recipeShape[1], recipeShape[2]);

            for (String str : recipeParts) {
                String[] parts = str.split(", ");
                char partName = str.charAt(0);
                String matString = parts[1];

                int amount = Integer.parseInt(parts[2]);
                ItemStack item = new ItemStack(Material.matchMaterial(matString), amount);
                RecipeChoice inputChoice = new RecipeChoice.ExactChoice(item);

                recipe.setIngredient(partName, inputChoice);
            }

            if (Bukkit.getRecipe(namespacedKey) != null) {
                Bukkit.removeRecipe(namespacedKey);
            }

            Bukkit.addRecipe(recipe);

            MissileObject obj = new MissileObject(arrowItem, potionType, range, magnitude, speed);
            RowMissiles.missileList.put(obj, recipe);
        }
    }

    public static ItemStack missileItem(String name, PotionType type) {
        ItemStack arrowItem = new ItemStack(Material.TIPPED_ARROW);

        PotionMeta potionMeta = (PotionMeta) arrowItem.getItemMeta();
        PotionData potionData = new PotionData(type);
        potionMeta.setBasePotionData(potionData);
        arrowItem.setItemMeta(potionMeta);

        ItemMeta itemMeta = arrowItem.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        arrowItem.setItemMeta(itemMeta);

        return arrowItem;
    }
}
