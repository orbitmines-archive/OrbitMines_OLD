package com.orbitmines.api.spigot.perks;

import com.orbitmines.api.Message;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.spigot.Color;
import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.handlers.ItemSet;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.Obtainable;
import com.orbitmines.api.utils.RandomUtils;
import org.bukkit.Material;

import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 28-7-2017
*/
public enum Wardrobe implements Perk {

    AQUA(Color.AQUA, new Obtainable(OrbitMinesApi.VIP_POINTS, 250)),
    BLACK(Color.BLACK, new Obtainable(OrbitMinesApi.VIP_POINTS, 250)),
    BLUE(Color.BLUE, new Obtainable(OrbitMinesApi.VIP_POINTS, 250)),
    EMERALD("Emerald", Color.LIME, new ItemSet(Material.CHAINMAIL_CHESTPLATE), new Obtainable(VipRank.EMERALD)),
    FUCHSIA(Color.FUCHSIA, new Obtainable(OrbitMinesApi.VIP_POINTS, 250)),
    GRAY(Color.GRAY, new Obtainable(OrbitMinesApi.VIP_POINTS, 250)),
    GREEN(Color.GREEN, new Obtainable(OrbitMinesApi.VIP_POINTS, 250)),
    IRON("Iron", Color.SILVER, new ItemSet(Material.IRON_CHESTPLATE), new Obtainable(VipRank.IRON)),
    LIME(Color.LIME, new Obtainable(OrbitMinesApi.VIP_POINTS, 250)),
    NAVY(Color.NAVY, new Obtainable(OrbitMinesApi.VIP_POINTS, 250)),
    DIAMOND("Diamond", Color.AQUA, new ItemSet(Material.DIAMOND_CHESTPLATE), new Obtainable(VipRank.DIAMOND)),
    ORANGE(Color.ORANGE, new Obtainable(OrbitMinesApi.VIP_POINTS, 250)),
    PURPLE(Color.PURPLE, new Obtainable(OrbitMinesApi.VIP_POINTS, 250)),
    RED(Color.RED, new Obtainable(OrbitMinesApi.VIP_POINTS, 250)),
    TEAL(Color.TEAL, new Obtainable(OrbitMinesApi.VIP_POINTS, 250)),
    GOLD("Gold", Color.ORANGE, new ItemSet(Material.GOLD_CHESTPLATE), new Obtainable(VipRank.GOLD)),
    WHITE(Color.WHITE, new Obtainable(OrbitMinesApi.VIP_POINTS, 250)),
    YELLOW(Color.YELLOW, new Obtainable(OrbitMinesApi.VIP_POINTS, 250)),
    ELYTRA("Elytra", Color.SILVER, new ItemSet(Material.ELYTRA), new Obtainable(new Message("§aBehaalt met de verjaardag van OrbitMines 2016", "§aAchieved at the birthday of OrbitMines 2016")));

    public static final Wardrobe[] COLORS = { AQUA, BLACK, BLACK, FUCHSIA, GRAY, GREEN, LIME, NAVY, ORANGE, PURPLE, RED, TEAL, WHITE, YELLOW };

    private final String name;
    private final Color color;

    private final ItemSet item;

    private final Obtainable obtainable;
    
    Wardrobe(Color color, Obtainable obtainable) {
        this(color.getName(), color, new ItemSet(Material.LEATHER_CHESTPLATE), obtainable);
    }
    
    Wardrobe(String name, Color color, ItemSet item, Obtainable obtainable) {
        this.name = name;
        this.color = color;
        this.item = item;
        this.obtainable = obtainable;
    }

    public String getName() {
        return name;
    }

    public Color color() {
        return color;
    }

    @Override
    public ItemSet item() {
        return item;
    }

    @Override
    public Obtainable obtainable() {
        return obtainable;
    }

    @Override
    public String getDisplayName() {
        return color.getChatColor() + name + " Armor";
    }

    @Override
    public boolean hasAccess(OMPlayer omp) {
        return omp.wardrobe().hasWardrobe(this);
    }

    public static Wardrobe random(List<Wardrobe> colors) {
        return colors.get(RandomUtils.RANDOM.nextInt(colors.size()));
    }
}
