package com.orbitmines.api.spigot.perks;

import com.orbitmines.api.Message;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.spigot.Color;
import com.orbitmines.api.spigot.Mob;
import com.orbitmines.api.spigot.handlers.*;
import org.bukkit.Material;

/*
* OrbitMines - @author Fadi Shawki - 7/28/2017
*/
public enum Disguise implements Perk {

    ENDERMAN(Mob.ENDERMAN, Color.GRAY, new Obtainable(Currency.VIP_POINTS, 500)),
    HORSE(Mob.HORSE, Color.ORANGE, new Obtainable(Currency.VIP_POINTS, 475)),
    IRON_GOLEM(Mob.IRON_GOLEM, Color.WHITE, new Obtainable(Currency.VIP_POINTS, 575)),
    GHAST(Mob.GHAST, Color.SILVER, new Obtainable(Currency.VIP_POINTS, 500)),
    SNOWMAN(Mob.SNOWMAN, Color.WHITE, new Obtainable(new Message("§aBehaalt met kerst 2014", "§aAchieved at Christmas 2014"))),
    RABBIT(Mob.RABBIT, Color.ORANGE, new Obtainable(Currency.VIP_POINTS, 650)),
    WITCH(Mob.WITCH, Color.GREEN, new Obtainable(Currency.VIP_POINTS, 450)),
    BAT(Mob.BAT, Color.GRAY, new Obtainable(Currency.VIP_POINTS, 450)),
    CHICKEN(Mob.CHICKEN, Color.WHITE, new Obtainable(Currency.VIP_POINTS, 325)),
    OCELOT(Mob.OCELOT, Color.YELLOW, new Obtainable(Currency.VIP_POINTS, 375)),
    MUSHROOM_COW(Mob.MUSHROOM_COW, Color.MAROON, new Obtainable(Currency.VIP_POINTS, 350)),
    SQUID(Mob.SQUID, Color.BLUE, new Obtainable(Currency.VIP_POINTS, 500)),
    SLIME(Mob.SLIME, Color.LIME, new Obtainable(Currency.VIP_POINTS, 425)),
    ZOMBIE_PIGMAN(Mob.PIG_ZOMBIE, Color.FUCHSIA, new Obtainable(Currency.VIP_POINTS, 400)),
    MAGMA_CUBE(Mob.MAGMA_CUBE, Color.RED, new Obtainable(Currency.VIP_POINTS, 475)),
    SKELETON(Mob.SKELETON, Color.SILVER, new Obtainable(Currency.VIP_POINTS, 500)),
    COW(Mob.COW, Color.GRAY, new Obtainable(Currency.VIP_POINTS, 350)),
    WOLF(Mob.WOLF, Color.SILVER, new Obtainable(Currency.VIP_POINTS, 350)),
    SPIDER(Mob.SPIDER, Color.GRAY, new Obtainable(Currency.VIP_POINTS, 375)),
    SILVERFISH(Mob.SILVERFISH, Color.SILVER, new Obtainable(Currency.VIP_POINTS, 475)),
    SHEEP(Mob.SHEEP, Color.WHITE, new Obtainable(Currency.VIP_POINTS, 375)),
    CAVE_SPIDER(Mob.CAVE_SPIDER, Color.TEAL, new Obtainable(Currency.VIP_POINTS, 400)),
    CREEPER(Mob.CREEPER, Color.LIME, new Obtainable(Currency.VIP_POINTS, 475)),
    PIG(Mob.PIG, Color.FUCHSIA, new Obtainable(VipRank.IRON)),
    BLAZE(Mob.BLAZE, Color.ORANGE, new Obtainable(VipRank.EMERALD)),
    ZOMBIE(Mob.ZOMBIE, Color.GRAY, new Obtainable(VipRank.GOLD)),
    VILLAGER(Mob.VILLAGER, Color.ORANGE, new Obtainable(VipRank.DIAMOND)),
    ARMOR_STAND(Mob.ARMOR_STAND, Color.ORANGE, new ItemSet(Material.ARMOR_STAND), new Obtainable(new Message("§aBehaalt met de verjaardag van OrbitMines 2015", "§aAchieved at the birthday of OrbitMines 2015"))),
    GUARDIAN(Mob.GUARDIAN, Color.TEAL, new Obtainable(Currency.VIP_POINTS, 750)),
    POLAR_BEAR(Mob.POLAR_BEAR, Color.WHITE, new Obtainable(Currency.VIP_POINTS, 600));

    private final Mob mob;
    private final Color color;

    private final ItemSet item;

    private final Obtainable obtainable;

    Disguise(Mob mob, Color color, Obtainable obtainable) {
        this(mob, color, new MobItemSet(mob), obtainable);
    }

    Disguise(Mob mob, Color color, ItemSet item, Obtainable obtainable) {
        this.mob = mob;
        this.color = color;
        this.item = item;
        this.obtainable = obtainable;
    }

    public Mob mob() {
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
        return color.getChatColor() + mob.getName() + " Disguise";
    }

    @Override
    public boolean hasAccess(OMPlayer omp) {
        return omp.disguises().hasDisguise(this);
    }
}
