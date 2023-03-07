package me.rownox.rowmissles;

import me.rownox.rowmissles.missiles.Missile;
import me.rownox.rowmissles.missiles.MissileCommand;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class RowMissles extends JavaPlugin {

    public RowMissles plugin = this;
    public static List<Missile> missileList = List.of(
            new Missile(
                    "MK II Helldive Missile",
                    "Range: 2000\n" + "Radius: 20"  + "Speed: 50 b/s",
                    Material.SPECTRAL_ARROW,
                    2000,
                    20,
                    50,
                    false),
            new Missile(
                    "MK XII Anchor Nuke",
                    "Range: 2000\n" + "Radius: 20"  + "Speed: 50 b/s",
                    Material.SPECTRAL_ARROW,
                    2000,
                    35,
                    75,
                    true)
    );

    @Override
    public void onEnable() {

        plugin.getCommand("missiles").setExecutor(new MissileCommand());
    }
}
