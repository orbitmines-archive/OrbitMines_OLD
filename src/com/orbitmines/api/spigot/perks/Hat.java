package com.orbitmines.api.spigot.perks;

import com.orbitmines.api.VipRank;
import com.orbitmines.api.spigot.Color;
import com.orbitmines.api.spigot.handlers.currency.Currency;
import com.orbitmines.api.spigot.handlers.ItemSet;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.Obtainable;
import org.bukkit.Material;

/*
* OrbitMines - @author Fadi Shawki - 7/28/2017
*/
public enum Hat implements Perk {

    STONE_BRICKS("Stone Bricks", Color.SILVER, new ItemSet(Material.SMOOTH_BRICK), new Obtainable(OrbitMinesApi.VIP_POINTS, 75)),
    GREEN_GLASS("Lime Stained Glass", Color.LIME, new ItemSet(Material.STAINED_GLASS, 5), new Obtainable(OrbitMinesApi.VIP_POINTS, 125)),
    CACTUS("Cactus", Color.GREEN, new ItemSet(Material.CACTUS), new Obtainable(OrbitMinesApi.VIP_POINTS, 100)),
    SNOW("Snow Block", Color.WHITE, new ItemSet(Material.SNOW_BLOCK), new Obtainable(OrbitMinesApi.VIP_POINTS, 75)),
    TNT("TNT", Color.RED, new ItemSet(Material.TNT), new Obtainable(OrbitMinesApi.VIP_POINTS, 125)),
    COAL_ORE("Coal Ore", Color.GRAY, new ItemSet(Material.COAL_ORE), new Obtainable(OrbitMinesApi.VIP_POINTS, 100)),
    BLACK_GLASS("Black Stained Glass", Color.GRAY, new ItemSet(Material.STAINED_GLASS, 15), new Obtainable(OrbitMinesApi.VIP_POINTS, 125)),
    FURNACE("Furnace", Color.SILVER, new ItemSet(Material.FURNACE), new Obtainable(OrbitMinesApi.VIP_POINTS, 100)),
    QUARTZ_ORE("Quartz Ore", Color.RED, new ItemSet(Material.QUARTZ_ORE), new Obtainable(OrbitMinesApi.VIP_POINTS, 100)),
    GOLD_BLOCK("Gold Block", Color.ORANGE, new ItemSet(Material.GOLD_BLOCK), new Obtainable(VipRank.GOLD)),
    HAY_BALE("Hay Bale", Color.YELLOW, new ItemSet(Material.HAY_BLOCK), new Obtainable(OrbitMinesApi.VIP_POINTS, 100)),
    REDSTONE_ORE("Redstone Ore", Color.MAROON, new ItemSet(Material.REDSTONE_ORE), new Obtainable(OrbitMinesApi.VIP_POINTS, 126)),
    ICE("Ice", Color.AQUA, new ItemSet(Material.ICE), new Obtainable(OrbitMinesApi.VIP_POINTS, 150)),
    WORKBENCH("Workbench", Color.ORANGE, new ItemSet(Material.WORKBENCH), new Obtainable(OrbitMinesApi.VIP_POINTS, 100)),
    EMERALD_ORE("Emerald Ore", Color.LIME, new ItemSet(Material.EMERALD_ORE), new Obtainable(VipRank.EMERALD)),
    GRASS("Grass", Color.LIME, new ItemSet(Material.GRASS), new Obtainable(OrbitMinesApi.VIP_POINTS, 100)),
    RED_GLASS("Red Stained Glass", Color.MAROON, new ItemSet(Material.STAINED_GLASS, 14), new Obtainable(OrbitMinesApi.VIP_POINTS, 125)),
    BEDROCK("Bedrock", Color.GRAY, new ItemSet(Material.BEDROCK), new Obtainable(OrbitMinesApi.VIP_POINTS, 125)),
    LAPIS_ORE("Lapis Ore", Color.NAVY, new ItemSet(Material.LAPIS_ORE), new Obtainable(OrbitMinesApi.VIP_POINTS, 100)),
    REDSTONE_BLOCK("Redstone Block", Color.MAROON, new ItemSet(Material.REDSTONE_BLOCK), new Obtainable(OrbitMinesApi.VIP_POINTS, 125)),
    GOLD_ORE("Gold Ore", Color.ORANGE, new ItemSet(Material.GOLD_ORE), new Obtainable(VipRank.GOLD)),
    QUARTZ_BLOCK("Quartz Block", Color.WHITE, new ItemSet(Material.QUARTZ_BLOCK), new Obtainable(OrbitMinesApi.VIP_POINTS, 75)),
    LAPIS_BLOCK("Lapis Block", Color.NAVY, new ItemSet(Material.LAPIS_BLOCK), new Obtainable(OrbitMinesApi.VIP_POINTS, 100)),
    MAGENTA_GLASS("Magenta Stained Glass", Color.FUCHSIA, new ItemSet(Material.STAINED_GLASS, 2), new Obtainable(OrbitMinesApi.VIP_POINTS, 125)),
    COAL_BLOCK("Coal Block", Color.BLACK, new ItemSet(Material.COAL_BLOCK), new Obtainable(OrbitMinesApi.VIP_POINTS, 100)),
    MELON("Melon", Color.GREEN, new ItemSet(Material.MELON_BLOCK), new Obtainable(OrbitMinesApi.VIP_POINTS, 100)),
    GLASS("Glass", Color.WHITE, new ItemSet(Material.GLASS), new Obtainable(OrbitMinesApi.VIP_POINTS, 100)),
    YELLOW_GLASS("Yellow Stained Glass", Color.YELLOW, new ItemSet(Material.STAINED_GLASS, 4), new Obtainable(OrbitMinesApi.VIP_POINTS, 125)),
    MYCELIUM("Mycelium", Color.SILVER, new ItemSet(Material.MYCEL, 4), new Obtainable(OrbitMinesApi.VIP_POINTS, 75)),
    LEAVES("Leaves", Color.GREEN, new ItemSet(Material.LEASH), new Obtainable(OrbitMinesApi.VIP_POINTS, 125)),
    ORANGE_GLASS("Orange Stained Glass", Color.ORANGE, new ItemSet(Material.STAINED_GLASS, 1), new Obtainable(OrbitMinesApi.VIP_POINTS, 125)),
    DIORITE("Polished Diorite", Color.WHITE, new ItemSet(Material.STONE, 4), new Obtainable(OrbitMinesApi.VIP_POINTS, 100)),
    IRON_BLOCK("Iron Block", Color.SILVER, new ItemSet(Material.IRON_BLOCK), new Obtainable(VipRank.IRON)),
    DARK_PRISMARINE("Dark Prismarine", Color.TEAL, new ItemSet(Material.PRISMARINE, 2), new Obtainable(OrbitMinesApi.VIP_POINTS, 150)),
    SLIME_BLOCK("Slime Block", Color.LIME, new ItemSet(Material.SLIME_BLOCK), new Obtainable(OrbitMinesApi.VIP_POINTS, 200)),
    GRANITE("Polished Granite", Color.RED, new ItemSet(Material.STONE, 2), new Obtainable(OrbitMinesApi.VIP_POINTS, 100)),
    IRON_ORE("Iron Ore", Color.SILVER, new ItemSet(Material.IRON_ORE), new Obtainable(VipRank.IRON)),
    SEA_LANTERN("Sea Lantern", Color.WHITE, new ItemSet(Material.SEA_LANTERN), new Obtainable(OrbitMinesApi.VIP_POINTS, 225)),
    PRISMARINE_BRICKS("Prismarine Bricks", Color.BLUE, new ItemSet(Material.PRISMARINE, 1), new Obtainable(OrbitMinesApi.VIP_POINTS, 150)),
    SPONGE("Sponge", Color.YELLOW, new ItemSet(Material.SPONGE), new Obtainable(OrbitMinesApi.VIP_POINTS, 100)),
    CHEST("EnderChest", Color.TEAL, new ItemSet(Material.ENDER_CHEST), new Obtainable(OrbitMinesApi.VIP_POINTS, 175)),
    DIAMOND_ORE("Diamond Ore", Color.AQUA, new ItemSet(Material.DIAMOND_ORE), new Obtainable(VipRank.DIAMOND)),
    GLOWSTONE("Glowstone", Color.ORANGE, new ItemSet(Material.GLOWSTONE), new Obtainable(OrbitMinesApi.VIP_POINTS, 200)),
    WET_SPONGE("Wet Sponge", Color.YELLOW, new ItemSet(Material.SPONGE, 1), new Obtainable(OrbitMinesApi.VIP_POINTS, 125)),
    ANDESITE("Polished Andesite", Color.SILVER, new ItemSet(Material.STONE, 6), new Obtainable(OrbitMinesApi.VIP_POINTS, 100)),
    DIAMOND_BLOCK("Diamond Block", Color.AQUA, new ItemSet(Material.DIAMOND_BLOCK), new Obtainable(VipRank.DIAMOND)),
    BLUE_GLASS("Blue Stained Glass", Color.NAVY, new ItemSet(Material.STAINED_GLASS, 11), new Obtainable(OrbitMinesApi.VIP_POINTS, 125)),
    ACACIA_WOOD("Acacia Wood", Color.ORANGE, new ItemSet(Material.WOOD, 4), new Obtainable(OrbitMinesApi.VIP_POINTS, 75)),
    RED_WOOL("Red Wool", Color.RED, new ItemSet(Material.WOOL, 14), new Obtainable(OrbitMinesApi.VIP_POINTS, 100)),
    BROWN_GLASS("Brown Stained Glass", Color.WHITE, new ItemSet(Material.STAINED_GLASS, 12), new Obtainable(OrbitMinesApi.VIP_POINTS, 125)),
    SOUL_SAND("Soul Sand", Color.YELLOW, new ItemSet(Material.SOUL_SAND), new Obtainable(OrbitMinesApi.VIP_POINTS, 100)),
    CHISELLED_STONE_BRICKS("Chiselled Stone Bricks", Color.SILVER, new ItemSet(Material.SMOOTH_BRICK, 3), new Obtainable(OrbitMinesApi.VIP_POINTS, 100)),
    EMERALD_BLOCK("Emerald Block", Color.LIME, new ItemSet(Material.EMERALD_BLOCK), new Obtainable(VipRank.EMERALD)),
    BOOKSHELF("Bookshelf", Color.YELLOW, new ItemSet(Material.BOOKSHELF), new Obtainable(OrbitMinesApi.VIP_POINTS, 100)),
    NETHERRACK("Netherrack", Color.RED, new ItemSet(Material.NETHERRACK), new Obtainable(OrbitMinesApi.VIP_POINTS, 75)),
    CYAN_GLASS("Cyan Stained Glass", Color.TEAL, new ItemSet(Material.STAINED_GLASS, 9), new Obtainable(OrbitMinesApi.VIP_POINTS, 125)),
    BEACON("Beacon", Color.AQUA, new ItemSet(Material.BEACON), new Obtainable(OrbitMinesApi.VIP_POINTS, 100)),
    MAGMA_BLOCK("Magma Block", Color.RED, new ItemSet(Material.MAGMA), new Obtainable(OrbitMinesApi.VIP_POINTS, 200)),
    PURPUR_BLOCK("Purpur Block Hat", Color.FUCHSIA, new ItemSet(Material.PURPUR_BLOCK), new Obtainable(OrbitMinesApi.VIP_POINTS, 75)),
    END_STONE_BRICKS("End Stone Bricks", Color.YELLOW, new ItemSet(Material.END_BRICKS), new Obtainable(OrbitMinesApi.VIP_POINTS, 75));

    private final String name;
    private final Color color;

    private final ItemSet item;

    private final Obtainable obtainable;

    Hat(String name, Color color, ItemSet item, Obtainable obtainable) {
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
        return color.getChatColor() + name + " Hat";
    }

    @Override
    public boolean hasAccess(OMPlayer omp) {
        return omp.hats().hasHat(this);
    }
}
