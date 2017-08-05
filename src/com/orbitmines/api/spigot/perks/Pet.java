package com.orbitmines.api.spigot.perks;

import com.orbitmines.api.spigot.Color;
import com.orbitmines.api.spigot.Mob;
import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.handlers.ItemSet;
import com.orbitmines.api.spigot.handlers.MobItemSet;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.Obtainable;
import com.orbitmines.api.spigot.handlers.gadget.petride.PetHandler;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 7/28/2017
*/
public enum Pet implements Perk {

    MUSHROOM_COW(Mob.MUSHROOM_COW, Color.MAROON, new Obtainable(OrbitMinesApi.VIP_POINTS, 475)),
    PIG(Mob.PIG, Color.FUCHSIA, new Obtainable(OrbitMinesApi.VIP_POINTS, 425)),
    WOLF(Mob.WOLF, Color.SILVER, new Obtainable(OrbitMinesApi.VIP_POINTS, 475)),
    SHEEP(Mob.SHEEP, Color.WHITE, new Obtainable(OrbitMinesApi.VIP_POINTS, 425)),
    HORSE(Mob.HORSE, Color.ORANGE, new Obtainable(OrbitMinesApi.VIP_POINTS, 525)),
    MAGMA_CUBE(Mob.MAGMA_CUBE, Color.RED, new Obtainable(OrbitMinesApi.VIP_POINTS, 500)),
    SLIME(Mob.SLIME, Color.LIME, new Obtainable(OrbitMinesApi.VIP_POINTS, 475)),
    COW(Mob.COW, Color.GRAY, new Obtainable(OrbitMinesApi.VIP_POINTS, 425)),
    SILVERFISH(Mob.SILVERFISH, Color.SILVER, new Obtainable(OrbitMinesApi.VIP_POINTS, 450)),
    OCELOT(Mob.OCELOT, Color.YELLOW, new Obtainable(OrbitMinesApi.VIP_POINTS, 450)),
    CREEPER(Mob.CREEPER, Color.LIME, new Obtainable(OrbitMinesApi.VIP_POINTS, 425)),
    SQUID(Mob.SQUID, Color.BLUE, new Obtainable(OrbitMinesApi.VIP_POINTS, 600)),
    SPIDER(Mob.SPIDER, Color.GRAY, new Obtainable(OrbitMinesApi.VIP_POINTS, 525)),
    CHICKEN(Mob.CHICKEN, Color.SILVER, new Obtainable(OrbitMinesApi.VIP_POINTS, 425));

    private static List<Entity> entities = new ArrayList<>();

    private final Mob mob;
    private final Color color;

    private final MobItemSet item;

    private final Obtainable obtainable;

    private PetHandler petHandler;

    Pet(Mob mob, Color color, Obtainable obtainable) {
        this.mob = mob;
        this.color = color;
        this.item = new MobItemSet(mob);
        this.obtainable = obtainable;
    }

    public Mob getMob() {
        return mob;
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
        return color.getChatColor() + mob.getName() + " Pet";
    }

    @Override
    public boolean hasAccess(OMPlayer omp) {
        return omp.pets().hasPet(this);
    }

    public static List<Entity> getEntities() {
        return entities;
    }

    public PetHandler getHandler() {
        return petHandler;
    }

    public void setHandler(PetHandler petHandler) {
        this.petHandler = petHandler;
    }
}
