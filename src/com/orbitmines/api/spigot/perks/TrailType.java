package com.orbitmines.api.spigot.perks;

import com.orbitmines.api.VipRank;
import com.orbitmines.api.spigot.handlers.Currency;
import com.orbitmines.api.spigot.handlers.ItemSet;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.Obtainable;
import org.bukkit.Material;

/*
* OrbitMines - @author Fadi Shawki - 7/28/2017
*/
public enum TrailType implements Perk {
    
    BASIC_TRAIL("Basic Trail", false, new Obtainable(VipRank.NONE)),
    GROUND_TRAIL("Ground Trail", false, new Obtainable(Currency.VIP_POINTS, 600)),
    HEAD_TRAIL("Head Trail", false, new Obtainable(Currency.VIP_POINTS, 400)),
    BODY_TRAIL("Body Trail", false, new Obtainable(Currency.VIP_POINTS, 400)),
    BIG_TRAIL("Big Trail", false, new Obtainable(Currency.VIP_POINTS, 650)),
    VERTICAL_TRAIL("Vertical Trail", false, new Obtainable(Currency.VIP_POINTS, 500)),
    CYLINDER_TRAIL("Cylinder Trail", true, new Obtainable(Currency.VIP_POINTS, 1500)),
    ORBIT_TRAIL("Orbit Trail", true, new Obtainable(Currency.VIP_POINTS, 1750)),
    SNAKE_TRAIL("Snake Trail", true, new Obtainable(Currency.VIP_POINTS, 1000)),

    FULL_CYLINDER(null, true, new Obtainable());

    private final String name;
    private final boolean animated;

    private final Obtainable obtainable;

    TrailType(String name, boolean animated, Obtainable obtainable) {
        this.name = name;
        this.animated = animated;
        this.obtainable = obtainable;
    }

    public String getName() {
        return name;
    }

    public boolean isAnimated() {
        return animated;
    }

    @Override
    public Obtainable obtainable() {
        return obtainable;
    }

    @Override
    public ItemSet item() {
        return new ItemSet(Material.STRING);
    }

    @Override
    public String getDisplayName() {
        return "§7§l" + name;
    }

    @Override
    public boolean hasAccess(OMPlayer omp) {
        return omp.trails().hasTrailType(this);
    }
}
