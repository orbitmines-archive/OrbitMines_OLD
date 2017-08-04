package com.orbitmines.api.spigot.perks;

import com.orbitmines.api.VipRank;
import com.orbitmines.api.spigot.Color;
import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.handlers.ItemSet;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.Obtainable;
import com.orbitmines.api.spigot.handlers.gadget.GadgetHandler;
import org.bukkit.Material;

/*
* OrbitMines - @author Fadi Shawki - 7/28/2017
*/
public enum Gadget implements Perk {

    STACKER("Stacker", Color.ORANGE, new ItemSet(Material.LEASH), new Obtainable(VipRank.NONE)),
    PAINTBALLS("Paintballs", Color.WHITE, new ItemSet(Material.SNOW_BALL), new Obtainable(OrbitMinesApi.VIP_POINTS, 700)),
    CREEPER_LAUNCHER("Creeper Launcher", Color.LIME, new ItemSet(Material.SKULL_ITEM, 4), new Obtainable(OrbitMinesApi.VIP_POINTS, 525)),
    PET_RIDE("Pet Ride", Color.YELLOW, new ItemSet(Material.SADDLE), new Obtainable(OrbitMinesApi.VIP_POINTS, 500)),
    BOOK_EXPLOSION("Book Explosion", Color.SILVER, new ItemSet(Material.BOOK), new Obtainable(OrbitMinesApi.VIP_POINTS, 475)),
    SWAP_TELEPORTER("Swap Teleporter", Color.GREEN, new ItemSet(Material.EYE_OF_ENDER), new Obtainable(OrbitMinesApi.VIP_POINTS, 500)),
    FIREWORK_GUN("Firework Gun", Color.RED, new ItemSet(Material.FIREBALL), new Obtainable(VipRank.NONE)),
    SNOWMAN_ATTACK("Snowman Attack", Color.ORANGE, new ItemSet(Material.PUMPKIN), new Obtainable(OrbitMinesApi.VIP_POINTS, 1200)),
    FLAME_THROWER("Flame Thrower", Color.YELLOW, new ItemSet(Material.BLAZE_POWDER), new Obtainable(OrbitMinesApi.VIP_POINTS, 575)),
    GRAPPLING_HOOK("Grappling Hook", Color.SILVER, new ItemSet(Material.FISHING_ROD), new Obtainable(OrbitMinesApi.VIP_POINTS, 1250)),
    MAGMA_CUBE_SOCCER("MagmaCube Soccer", Color.RED, new ItemSet(Material.MAGMA_CREAM), new Obtainable(OrbitMinesApi.VIP_POINTS, 1500));

    private final String name;
    private final Color color;

    private final ItemSet item;

    private final Obtainable obtainable;

    private GadgetHandler handler;

    Gadget(String name, Color color, ItemSet item, Obtainable obtainable) {
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
        return color.getChatColor() + name + " Gadget";
    }

    @Override
    public boolean hasAccess(OMPlayer omp) {
        return omp.gadgets().hasGadget(this);
    }

    public GadgetHandler getHandler() {
        return handler;
    }

    public void setHandler(GadgetHandler handler) {
        this.handler = handler;
    }
}
