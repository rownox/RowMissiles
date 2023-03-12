package me.rownox.rowmissiles;

import me.rownox.rowmissiles.commands.MissileCmd;
import me.rownox.rowmissiles.listeners.BlockBreakListener;
import me.rownox.rowmissiles.listeners.ChatListener;
import me.rownox.rowmissiles.listeners.InteractListener;
import me.rownox.rowmissiles.listeners.JoinListener;
import me.rownox.rowmissiles.objects.MissileObject;
import me.rownox.rowmissiles.objects.OreObject;
import me.rownox.rowmissiles.objects.PlayerValuesObject;
import org.bukkit.Server;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class RowMissiles extends JavaPlugin {

    private static File missilesFile;
    private static File oresFile;
    private static YamlConfiguration missilesConfig;
    private static YamlConfiguration oresConfig;
    public static FileConfiguration config;
    public static RowMissiles plugin;

    public static HashMap<MissileObject, ShapedRecipe> missileList = new HashMap<>();
    public static WeakHashMap<UUID, PlayerValuesObject> playerValues = new WeakHashMap<>();
    public static List<OreObject> ores = new ArrayList<>();

    public static String prefix;
    public static boolean customMiningEnabled;

    @Override
    public void onEnable() {

        plugin = this;

        loadOreConfig();
        loadMissileConfig();
        loadDefaultConfig();

        prefix = config.getString("prefix");
        customMiningEnabled = config.getBoolean("custom_mining");

        plugin.getCommand("missiles").setExecutor(new MissileCmd());
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new InteractListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);

        ConfigUtils.initOres();
        ConfigUtils.initMissiles();
    }

    public void loadMissileConfig() {
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
    }

    public void loadOreConfig() {
       oresFile = new File(getDataFolder(), "ores.yml");
        if (!oresFile.exists()) {
            oresFile.getParentFile().mkdirs();
            saveResource("ores.yml", false);
        }

        oresConfig = new YamlConfiguration();
        try {
            oresConfig.load(oresFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void loadDefaultConfig() {
        config = getConfig();
        config.options().copyDefaults(true);
        saveDefaultConfig();
    }

    public static RowMissiles getInstance() {
        return plugin;
    }

    public static void registerExternalListener(Listener listener, Server server) {
        server.getPluginManager().registerEvents(listener, getInstance());
    }

    public static YamlConfiguration getMissilesConfig() {
        return missilesConfig;
    }

    public static YamlConfiguration getOresConfig() {
        return oresConfig;
    }

}
