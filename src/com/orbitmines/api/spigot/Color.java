package com.orbitmines.api.spigot;

import com.orbitmines.api.spigot.handlers.ItemSet;
import org.bukkit.Material;

/*
* OrbitMines - @author Fadi Shawki - 7/20/2017
*/
public enum Color {

    AQUA("§b", "Light Blue", org.bukkit.Color.AQUA, 12),
    BLACK("§0", "Black", org.bukkit.Color.BLACK, 0),
    BLUE("§9", "Blue", org.bukkit.Color.BLUE, 4),
    FUCHSIA("§d", "Pink", org.bukkit.Color.FUCHSIA, 9),
    GRAY("§8", "Dark Gray", org.bukkit.Color.GRAY, 8),
    GREEN("§2", "Green", org.bukkit.Color.GREEN, 2),
    LIME("§a", "Light Green", org.bukkit.Color.LIME, 10),
    MAROON("§4", "Dark Red", org.bukkit.Color.MAROON, Material.REDSTONE, 0),
    NAVY("§1", "Dark Blue", org.bukkit.Color.NAVY, 12),
    ORANGE("§6", "Orange", org.bukkit.Color.ORANGE, 14),
    PURPLE("§5", "Purple", org.bukkit.Color.PURPLE, 5),
    RED("§c", "Red", org.bukkit.Color.RED, 1),
    SILVER("§7", "Gray", org.bukkit.Color.SILVER, 7),
    TEAL("§3", "Cyan", org.bukkit.Color.TEAL, 6),
    WHITE("§f", "White", org.bukkit.Color.WHITE, 15),
    YELLOW("§e", "Yellow", org.bukkit.Color.YELLOW, 11);

    private final String chatColor;
    private final String name;
    private final org.bukkit.Color bukkitColor;
    private final ItemSet item;

    Color(String chatColor, String name, org.bukkit.Color bukkitColor, int durability) {
        this(chatColor, name, bukkitColor, Material.INK_SACK, durability);
    }

    Color(String chatColor, String name, org.bukkit.Color bukkitColor, Material material, int durability) {
        this.chatColor = chatColor;
        this.name = name;
        this.bukkitColor = bukkitColor;
        this.item = new ItemSet(material, 1, durability);
    }

    public String getChatColor() {
        return chatColor;
    }

    public String getName() {
        return name;
    }

    public org.bukkit.Color getBukkitColor() {
        return bukkitColor;
    }

    public ItemSet item() {
        return item;
    }

    public String getDisplayName() {
        return chatColor + name;
    }
}
