package me.rownox.rowmissles;

import me.rownox.rowmissles.missiles.Missile;
import me.rownox.rowmissles.missiles.MissileCommand;
import me.rownox.rowmissles.missiles.MissileUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class RowMissles extends JavaPlugin {

    private static File missilesFile;
    private static YamlConfiguration missilesConfig;

    public RowMissles plugin = this;

    public static List<Missile> missileList = new ArrayList<>();

    @Override
    public void onEnable() {

        missilesFile = new File(getDataFolder(), "missiles.yml");
        if (!missilesFile.exists()) {
            missilesFile.getParentFile().mkdirs();
            saveResource("missiles.yml", false);
        }

        missilesConfig = new YamlConfiguration();
        try {
            missilesConfig.load(missilesFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        MissileUtils.initMissiles();

        plugin.getCommand("missiles").setExecutor(new MissileCommand());
    }

    public static YamlConfiguration getMissilesConfig() {
        return missilesConfig;
    }
}
