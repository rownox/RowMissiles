package me.rownox.rowmissles.missiles;

import org.bukkit.Material;

public class Missile {

    private final String name;
    private final String lore;
    private final Material material;
    private final int range;
    private final int radius;
    private final int speed;
    private final boolean nuclear;

    public Missile(String name, String lore, Material material, int range, int radius, int speed, boolean radioactive) {
        this.name = name;
        this.lore = lore;
        this.material = material;
        this.range = range;
        this.radius = radius;
        this.speed = speed;
        this.nuclear = radioactive;
    }

    public String getName() {
        return name;
    }
    public String getLore() {
        return lore;
    }
    public Material getMaterial() {
        return material;
    }
    public int getRange() {
        return range;
    }
    public int getRadius() {
        return radius;
    }
    public int getSpeed() {
        return speed;
    }
    public boolean isNuclear() {
        return nuclear;
    }
}
