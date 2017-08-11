package com.orbitmines.kitpvp;

import org.bukkit.Material;

/*
* OrbitMines - @author Fadi Shawki - 11-8-2017
*/
@Deprecated
public enum Mastery {

    MELEE("§7§lMelee Damage", "Melee Damage", Material.DIAMOND_SWORD, 0.02, "§a§l+", "§c§l-"),
    MELEE_PROTECTION("§7§lMelee Protection", "Melee Damage Taken", Material.IRON_CHESTPLATE, 0.02, "§c§l-", "§a§l+"),
    RANGE("§7§lRange Damage", "Range Damage", Material.BOW, 0.04, "§a§l+", "§c§l-"),
    RANGE_PROTECTION("§7§lRange Protection", "Range Damage Taken", Material.ARROW, 0.03, "§c§l-", "§a§l+");

    private String name;
    private String effectName;
    private Material material;
    private double multiplier;
    private String color;
    private String oppositeColor;

    Mastery(String name, String effectName, Material material, double multiplier, String color, String oppositeColor) {
        this.name = name;
        this.effectName = effectName;
        this.material = material;
        this.multiplier = multiplier;
        this.color = color;
        this.oppositeColor = oppositeColor;
    }

    public String getName() {
        return name;
    }

    public String getEffectName() {
        return effectName;
    }

    public Material getMaterial() {
        return material;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public String getColor() {
        return color;
    }

    public String getOppositeColor() {
        return oppositeColor;
    }
}
