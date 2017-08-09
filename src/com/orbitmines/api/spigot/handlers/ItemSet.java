package com.orbitmines.api.spigot.handlers;

import org.bukkit.Material;

/*
* OrbitMines - @author Fadi Shawki - 7/28/2017
*/
public class ItemSet {

    private final Material material;
    private final int amount;
    private final short durability;
    private final String displayName;

    public ItemSet(Material material) {
        this(material, 1);
    }

    public ItemSet(Material material, int amount) {
        this(material, amount, 0);
    }

    public ItemSet(Material material, int amount, int durability) {
        this(material, amount, durability, null);
    }

    public ItemSet(Material material, int amount, int durability, String displayName) {
        this.material = material;
        this.amount = amount;
        this.durability = (short) durability;
        this.displayName = displayName;
    }

    public Material getMaterial() {
        return material;
    }

    public short getDurability() {
        return durability;
    }

    public int getAmount() {
        return amount;
    }

    public String getDisplayName() {
        return displayName;
    }
}
