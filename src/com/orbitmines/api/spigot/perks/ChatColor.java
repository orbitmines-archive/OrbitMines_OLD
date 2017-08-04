package com.orbitmines.api.spigot.perks;

import com.orbitmines.api.VipRank;
import com.orbitmines.api.spigot.handlers.currency.Currency;
import com.orbitmines.api.spigot.handlers.ItemSet;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.Obtainable;
import com.orbitmines.api.spigot.Color;
import org.bukkit.Material;

/*
* OrbitMines - @author Fadi Shawki - 7/28/2017
*/
public enum ChatColor implements Perk {

    DARK_RED(Color.MAROON, new ItemSet(Material.REDSTONE), new Obtainable(OrbitMinesApi.VIP_POINTS, 475)),
    LIGHT_GREEN(Color.LIME, new ItemSet(Material.INK_SACK, 10), new Obtainable(OrbitMinesApi.VIP_POINTS, 575)),
    PURPLE(Color.PURPLE, new ItemSet(Material.INK_SACK, 5), new Obtainable(VipRank.IRON)),
    DARK_GRAY(Color.GRAY, new ItemSet(Material.INK_SACK, 8), new Obtainable(OrbitMinesApi.VIP_POINTS, 250)),
    RED(Color.RED, new ItemSet(Material.INK_SACK, 1), new Obtainable(OrbitMinesApi.VIP_POINTS, 650)),
    YELLOW(Color.YELLOW, new ItemSet(Material.INK_SACK, 15), new Obtainable(VipRank.GOLD)),
    WHITE(Color.WHITE, new ItemSet(Material.INK_SACK, 11), new Obtainable(OrbitMinesApi.VIP_POINTS, 500)),
    ORANGE(Color.ORANGE, new ItemSet(Material.INK_SACK, 14), new Obtainable(VipRank.EMERALD)),
    LIGHT_BLUE(Color.AQUA, new ItemSet(Material.INK_SACK, 12), new Obtainable(OrbitMinesApi.VIP_POINTS, 700)),
    PINK(Color.FUCHSIA, new ItemSet(Material.INK_SACK, 9), new Obtainable(OrbitMinesApi.VIP_POINTS, 525)),
    BLUE(Color.BLUE, new ItemSet(Material.INK_SACK, 12), new Obtainable(OrbitMinesApi.VIP_POINTS, 475)),
    DARK_BLUE(Color.NAVY, new ItemSet(Material.INK_SACK, 4), new Obtainable(OrbitMinesApi.VIP_POINTS, 375)),
    GRAY(Color.SILVER, new ItemSet(Material.INK_SACK, 7), new Obtainable(VipRank.NONE)),
    CYAN(Color.TEAL, new ItemSet(Material.INK_SACK, 6), new Obtainable(VipRank.DIAMOND)),
    GREEN(Color.GREEN, new ItemSet(Material.INK_SACK, 2), new Obtainable(OrbitMinesApi.VIP_POINTS, 475)),
    BLACK(Color.BLACK, new ItemSet(Material.INK_SACK), new Obtainable(OrbitMinesApi.VIP_POINTS, 200));

    private final Color color;

    private final ItemSet item;

    private final Obtainable obtainable;

    ChatColor(Color color, ItemSet item, Obtainable obtainable) {
        this.color = color;
        this.item = item;
        this.obtainable = obtainable;
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
        return color.getDisplayName() + " Chat Color";
    }

    @Override
    public boolean hasAccess(OMPlayer omp) {
        return omp.chatcolors().hasChatColor(this);
    }
}
