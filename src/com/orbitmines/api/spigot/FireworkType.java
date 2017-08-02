package com.orbitmines.api.spigot;

import org.bukkit.FireworkEffect;
import org.bukkit.Material;

/*
* OrbitMines - @author Fadi Shawki - 7/20/2017
*/
public enum FireworkType {

    BALL("§e", "Small", FireworkEffect.Type.BALL, Material.FIREWORK_CHARGE, 0),
    BALL_LARGE("§6", "Large", FireworkEffect.Type.BALL_LARGE, Material.FIREBALL, 0),
    BURST("§c", "Special", FireworkEffect.Type.BURST, Material.TNT, 0),
    CREEPER("§a", "Creeper", FireworkEffect.Type.CREEPER, Material.SKULL_ITEM, 4),
    STAR("§f", "Star", FireworkEffect.Type.STAR, Material.NETHER_STAR, 0);

    private final String chatColor;
    private final String name;
    private final FireworkEffect.Type type;
    private final Material material;
    private final short durability;

    FireworkType(String chatColor, String name, FireworkEffect.Type type, Material material, int durability) {
        this.chatColor = chatColor;
        this.name = name;
        this.type = type;
        this.material = material;
        this.durability = (short) durability;
    }

    public String getChatColor() {
        return chatColor;
    }

    public String getName() {
        return name;
    }

    public FireworkEffect.Type getType() {
        return type;
    }

    public Material getMaterial() {
        return material;
    }

    public short getDurability() {
        return durability;
    }
}
