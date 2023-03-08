package me.rownox.rowmissiles.utils;

import me.rownox.rowmissiles.RowMissiles;
import me.rownox.rowmissiles.objects.Missile;
import me.rownox.rowmissiles.objects.PlayerValues;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class MissileUtils {

    private static final YamlConfiguration config = RowMissiles.getMissilesConfig();

    public static void initMissiles() {
        Set<String> keys = config.getKeys(false);
        for (String key : keys) {
            String translationKey = config.getString(key + ".material");
            Material material = Material.STONE;

//            String recipeString = config.getString(key + ".recipe");
//            String[] parts = recipeString.split(" ");

            String name = config.getString(key + ".name");
            int range = config.getInt(key + ".range");
            int radius = config.getInt(key + ".radius");
            int speed = config.getInt(key + ".speed");
            boolean nuclear = config.getBoolean(key + ".nuclear");
            int guiSlot = config.getInt(key + ".gui_slot");

            if (translationKey != null) {
                if (Material.matchMaterial(translationKey) != null) {
                    material = Material.valueOf(translationKey);
                }
            }

            List<String> lore = List.of(
                    "Range: " + range,
                    "Radius: " + radius,
                    "Speed: " + speed,
                    "Nuclear: " + nuclear
            );

            ItemStack item = new ItemStack(material);
            ItemMeta itemMeta = item.getItemMeta();

            itemMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + name);
            itemMeta.setLore(lore);
            item.setItemMeta(itemMeta);

            NamespacedKey namespacedKey = new NamespacedKey(RowMissiles.getInstance(), name);
            ShapedRecipe recipe = new ShapedRecipe(namespacedKey, item);
//
//            recipe.shape(parts);
//            for (String str : parts) {
//                recipe.setIngredient(str, Material.STONE);
//            }

            RowMissiles.missileList.put(new Missile(name, lore, material, range, radius, speed, nuclear, guiSlot), recipe);
        }
    }

    public static void launchMissile(Player p, @Nullable Location target) {
        PlayerValues pValues = RowMissiles.playerValues.get(p.getUniqueId());

        if (pValues.isReadyToLaunch()) {
            p.sendMessage("The missile is now bound to hit " + target.getX() + ", " + target.getY() + ", " + target.getZ());
        } else {
            p.sendMessage(
                    RowMissiles.prefix + ChatColor.YELLOW + "Please enter the location of your target.",
                    RowMissiles.prefix + ChatColor.YELLOW + "Format: X Y Z"
            );
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            p.setMetadata("settingLocation", new FixedMetadataValue(RowMissiles.getInstance(), true));
        }

    }
}
