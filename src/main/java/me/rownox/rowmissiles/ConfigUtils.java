package me.rownox.rowmissiles;

import me.rownox.rowmissiles.objects.MissileObject;
import me.rownox.rowmissiles.objects.OreObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import javax.naming.Name;
import java.util.List;

public class ConfigUtils {

    private static final YamlConfiguration missilesConfig = RowMissiles.getMissilesConfig();
    private static final YamlConfiguration oresConfig = RowMissiles.getOresConfig();

    public static void initMissiles() {
        for (String key : missilesConfig.getKeys(false)) {

            String name = missilesConfig.getString(key + ".name");
            String potionTypeString = missilesConfig.getString(key + ".material");
            int range = missilesConfig.getInt(key + ".range");
            int magnitude = missilesConfig.getInt(key + ".magnitude");
            int speed = missilesConfig.getInt(key + ".speed");
            boolean nuclear = missilesConfig.getBoolean(key + ".nuclear");
            int guiSlot = missilesConfig.getInt(key + ".gui_slot");

            PotionType potionType = PotionType.valueOf(potionTypeString.toUpperCase());

            List<String> lore = List.of(
                    ChatColor.GRAY + "Range: " + range,
                    ChatColor.GRAY + "Radius: " + magnitude,
                    ChatColor.GRAY + "Speed: " + speed,
                    ChatColor.GRAY + "Nuclear: " + nuclear
            );

            ItemStack arrowItem = new ItemStack(Material.TIPPED_ARROW);

            PotionMeta potionMeta = (PotionMeta) arrowItem.getItemMeta();
            PotionData potionData = new PotionData(potionType);
            potionMeta.setBasePotionData(potionData);
            arrowItem.setItemMeta(potionMeta);

            ItemMeta itemMeta = arrowItem.getItemMeta();
            itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            itemMeta.setLore(lore);
            arrowItem.setItemMeta(itemMeta);

            Material arrowMaterial = arrowItem.getType();

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
                ItemStack item = new ItemStack(customOreMatch(matString), amount);
                RecipeChoice inputChoice = new RecipeChoice.ExactChoice(item);

                recipe.setIngredient(partName, inputChoice);
            }

            Bukkit.addRecipe(recipe);

            MissileObject obj = new MissileObject(arrowItem, range, magnitude, speed, nuclear);
            obj.setGuiSlot(guiSlot);
            RowMissiles.missileList.put(obj, recipe);
        }
    }

    public static void initOres() {
        for (String key : oresConfig.getKeys(false)) {

            Material blockFrom = Material.matchMaterial(oresConfig.getString(key + ".block_from"));
            String unrefinedName = oresConfig.getString(key + ".unrefined_name");
            Material unrefinedMat = Material.matchMaterial(oresConfig.getString(key + ".unrefined_mat"));
            int refineDuration = oresConfig.getInt(key + ".refine_duration");
            float experience = (float) oresConfig.getDouble(key + ".experience");
            String refinedName = oresConfig.getString(key + ".refined_name");
            Material refinedMat = Material.matchMaterial(oresConfig.getString(key + ".refined_mat"));

            if (RowMissiles.customMiningEnabled) {
                NamespacedKey namespacedKey = new NamespacedKey(RowMissiles.getInstance(), key);
                RecipeChoice inputChoice = new RecipeChoice.MaterialChoice(unrefinedMat);

                ItemStack refined = new ItemStack(refinedMat);
                ItemMeta itemMeta = refined.getItemMeta();
                itemMeta.setDisplayName(refinedName);
                refined.setItemMeta(itemMeta);

                FurnaceRecipe recipe = new FurnaceRecipe(namespacedKey, new ItemStack(refined), inputChoice, experience, 20*refineDuration);

                Bukkit.addRecipe(recipe);
            }

            RowMissiles.ores.add(new OreObject(blockFrom, unrefinedName, unrefinedMat, refinedName, refinedMat));
        }
    }

    private static Material customOreMatch(String matString) {
        for (OreObject ore : RowMissiles.ores) {
            if (ore.getRefinedName().equalsIgnoreCase(matString)) return ore.getRefinedMat();
        }
        return Material.matchMaterial(matString);
    }
}
