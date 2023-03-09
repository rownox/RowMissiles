package me.rownox.rowmissiles;

import me.rownox.rowmissiles.commands.MissileCmd;
import me.rownox.rowmissiles.listeners.ChatListener;
import me.rownox.rowmissiles.listeners.InteractListener;
import me.rownox.rowmissiles.listeners.JoinListener;
import me.rownox.rowmissiles.objects.MissileObject;
import me.rownox.rowmissiles.objects.PlayerValuesObject;
import me.rownox.rowmissiles.utils.MissileUtils;
import org.bukkit.Server;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.WeakHashMap;

public final class RowMissiles extends JavaPlugin {

    private static File missilesFile;
    private static YamlConfiguration missilesConfig;
    public static FileConfiguration config;

    public static RowMissiles plugin;

    public static HashMap<MissileObject, ShapedRecipe> missileList = new HashMap<>();
    public static WeakHashMap<UUID, PlayerValuesObject> playerValues = new WeakHashMap<>();
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

        plugin.getCommand("missiles").setExecutor(new MissileCmd());
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new InteractListener(), this);

        MissileUtils.initMissiles();
    }

    public static YamlConfiguration getMissilesConfig() {
        return missilesConfig;
    }

    public static RowMissiles getInstance() {
        return plugin;
    }

    public static void registerExternalListener(Listener listener, Server server) {
        server.getPluginManager().registerEvents(listener, getInstance());
    }

}
