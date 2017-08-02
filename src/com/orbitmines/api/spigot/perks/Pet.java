package com.orbitmines.api.spigot.perks;

import com.orbitmines.api.spigot.Color;
import com.orbitmines.api.spigot.Mob;
import com.orbitmines.api.spigot.handlers.*;

/*
* OrbitMines - @author Fadi Shawki - 7/28/2017
*/
public enum Pet implements Perk {

    MUSHROOM_COW(Mob.MUSHROOM_COW, Color.MAROON, new Obtainable(Currency.VIP_POINTS, 475)),
    PIG(Mob.PIG, Color.FUCHSIA, new Obtainable(Currency.VIP_POINTS, 425)),
    WOLF(Mob.WOLF, Color.SILVER, new Obtainable(Currency.VIP_POINTS, 475)),
    SHEEP(Mob.SHEEP, Color.WHITE, new Obtainable(Currency.VIP_POINTS, 425)),
    HORSE(Mob.HORSE, Color.ORANGE, new Obtainable(Currency.VIP_POINTS, 525)),
    MAGMA_CUBE(Mob.MAGMA_CUBE, Color.RED, new Obtainable(Currency.VIP_POINTS, 500)),
    SLIME(Mob.SLIME, Color.LIME, new Obtainable(Currency.VIP_POINTS, 475)),
    COW(Mob.COW, Color.GRAY, new Obtainable(Currency.VIP_POINTS, 425)),
    SILVERFISH(Mob.SILVERFISH, Color.SILVER, new Obtainable(Currency.VIP_POINTS, 450)),
    OCELOT(Mob.OCELOT, Color.YELLOW, new Obtainable(Currency.VIP_POINTS, 450)),
    CREEPER(Mob.CREEPER, Color.LIME, new Obtainable(Currency.VIP_POINTS, 425)),
    SQUID(Mob.SQUID, Color.BLUE, new Obtainable(Currency.VIP_POINTS, 600)),
    SPIDER(Mob.SPIDER, Color.GRAY, new Obtainable(Currency.VIP_POINTS, 525)),
    CHICKEN(Mob.CHICKEN, Color.SILVER, new Obtainable(Currency.VIP_POINTS, 425));

    private final Mob mob;
    private final Color color;

    private final MobItemSet item;

    private final Obtainable obtainable;

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
}
