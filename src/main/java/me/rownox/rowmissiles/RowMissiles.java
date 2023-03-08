package me.rownox.rowmissiles;

import me.rownox.rowmissiles.listeners.EventListener;
import me.rownox.rowmissiles.objects.Missile;
import me.rownox.rowmissiles.commands.MissileCommand;
import me.rownox.rowmissiles.objects.PlayerValues;
import me.rownox.rowmissiles.utils.MissileUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.WeakHashMap;

public final class RowMissiles extends JavaPlugin {

    private static File missilesFile;
    private static YamlConfiguration missilesConfig;
    public static FileConfiguration config;

    public static RowMissiles plugin;

    public static WeakHashMap<Missile, ShapedRecipe> missileList = new WeakHashMap<>();
    public static WeakHashMap<UUID, PlayerValues> playerValues = new WeakHashMap<>();
    public static String prefix;

    @Override
    public void onEnable() {

        plugin = this;

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

        config = getConfig();
        config.options().copyDefaults(true);
        saveDefaultConfig();

        prefix = config.getString("prefix");

        MissileUtils.initMissiles();

        plugin.getCommand("missiles").setExecutor(new MissileCommand());
        getServer().getPluginManager().registerEvents(new EventListener(), this);
    }

    public static YamlConfiguration getMissilesConfig() {
        return missilesConfig;
    }

    public static RowMissiles getInstance() {
        return plugin;
    }

}
