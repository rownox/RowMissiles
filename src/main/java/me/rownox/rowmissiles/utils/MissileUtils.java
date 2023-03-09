package me.rownox.rowmissiles.utils;

import me.rownox.rowmissiles.RowMissiles;
import me.rownox.rowmissiles.objects.Missile;
import me.rownox.rowmissiles.objects.PlayerValues;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import javax.annotation.Nullable;
import java.util.List;

public class MissileUtils {

    private static final YamlConfiguration config = RowMissiles.getMissilesConfig();

    public static void initMissiles() {
        for (String key : config.getKeys(false)) {
//            String recipeString = config.getString(key + ".recipe");
//            String[] parts = recipeString.split(" ");

            String name = config.getString(key + ".name");
            String potionTypeString = config.getString(key + ".material");
            int range = config.getInt(key + ".range");
            int radius = config.getInt(key + ".radius");
            int speed = config.getInt(key + ".speed");
            boolean nuclear = config.getBoolean(key + ".nuclear");
            int guiSlot = config.getInt(key + ".gui_slot");

            PotionType potionType = PotionType.valueOf(potionTypeString.toUpperCase());

            List<String> lore = List.of(
                    ChatColor.GRAY + "Range: " + range,
                    ChatColor.GRAY + "Radius: " + radius,
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
            itemMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + name);
            itemMeta.setLore(lore);
            arrowItem.setItemMeta(itemMeta);

            Material arrowMaterial = arrowItem.getType();

            NamespacedKey namespacedKey = new NamespacedKey(RowMissiles.getInstance(), name);
            ShapedRecipe recipe = new ShapedRecipe(namespacedKey, arrowItem);
//
//            recipe.shape(parts);
//            for (String str : parts) {
//                recipe.setIngredient(str, Material.STONE);
//            }

            RowMissiles.missileList.put(new Missile(name, lore, arrowItem, arrowMaterial, range, radius, speed, nuclear, guiSlot), recipe);
        }
    }

    public static void launchMissile(Player p, @Nullable Location target) {
        PlayerValues pValues = RowMissiles.playerValues.get(p.getUniqueId());

        if (pValues.isReadyToLaunch()) {
            for (Player op : Bukkit.getOnlinePlayers()) {
                op.sendMessage(ChatColor.translateAlternateColorCodes('&', RowMissiles.prefix + "&b&lAn intercontinental ballistic missile was launched."));
                op.playSound(op.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_4, 1, 1);
            }
            pValues.setReadyToLaunch(false);
        } else {
            p.sendMessage(
                    ChatColor.translateAlternateColorCodes('&',RowMissiles.prefix + "&ePlease enter the location of your target. Type '&ccancel&e' to abort the launch.'"),
                    ChatColor.translateAlternateColorCodes('&',RowMissiles.prefix + "&eFormat: &cX Y Z")
            );
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            pValues.setSettingLocation(true);
        }

    }
}
