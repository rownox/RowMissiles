package me.rownox.rowmissiles.objects;

import org.bukkit.Material;

import java.util.List;

public class Missile {

    private final String name;
    private final List<String> lore;
    private final Material material;
    private final int range;
    private final int radius;
    private final int speed;
    private final boolean nuclear;
    private final int guiSlot;

    public Missile(String name, List<String> lore, Material material, int range, int radius, int speed, boolean radioactive, int guiSlot) {
        this.name = name;
        this.lore = lore;
        this.material = material;
        this.range = range;
        this.radius = radius;
        this.speed = speed;
        this.nuclear = radioactive;
        this.guiSlot = guiSlot;
    }

    public String getName() {
        return name;
    }
    public List<String> getLore() {
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
    public int getGuiSlot() { return guiSlot; }
}
