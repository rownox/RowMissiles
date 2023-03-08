package me.rownox.rowmissles.missiles;

import me.rownox.rowmissles.RowMissles;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;
import java.util.Set;

public class MissileUtils {

    private static final YamlConfiguration config = RowMissles.getMissilesConfig();

    public static void initMissiles() {
        Set<String> keys = config.getKeys(false);
        for (String key : keys) {
            String translationKey = config.getString(key + ".material");
            Material material = Material.ARROW;

            if (translationKey != null) {
                material = Material.matchMaterial(translationKey);
            }

            String name = config.getString(key + ".name");
            int range = config.getInt(key + ".range");
            int radius = config.getInt(key + ".radius");
            int speed = config.getInt(key + ".speed");
            boolean nuclear = config.getBoolean(key + ".nuclear");
            int guiSlot = config.getInt(key + ".guiSlot");

            List<String> lore = List.of(
                    "Range: " + range,
                    "Radius: " + radius,
                    "Speed: " + speed,
                    "Nuclear: " + nuclear
            );

            RowMissles.missileList.add(new Missile(name, lore, material, range, radius, speed, nuclear, guiSlot));
        }
    }

    public static void launchMissile() {

    }
}
