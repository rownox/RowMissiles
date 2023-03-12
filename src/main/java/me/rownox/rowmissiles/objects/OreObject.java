package me.rownox.rowmissiles.objects;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class OreObject {

    private final Material blockFrom;
    private final String unrefinedName;
    private final Material unrefinedMat;
    private final String refinedName;
    private final Material refinedMat;

    public OreObject(Material blockFrom, String unrefinedName, Material unrefinedMat, String refinedName, Material refinedMat) {

        this.blockFrom = blockFrom;
        this.unrefinedName = unrefinedName;
        this.unrefinedMat = unrefinedMat;
        this.refinedName = refinedName;
        this.refinedMat = refinedMat;
    }

    public Material getBlockFrom() {return blockFrom;}
    public String getUnrefinedName() {return unrefinedName;}
    public Material getUnrefinedMat() {return unrefinedMat;}
    public String getRefinedName() {return refinedName;}
    public Material getRefinedMat() {return refinedMat;}
}
