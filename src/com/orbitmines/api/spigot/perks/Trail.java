package com.orbitmines.api.spigot.perks;

import com.orbitmines.api.VipRank;
import com.orbitmines.api.spigot.Color;
import com.orbitmines.api.spigot.handlers.currency.Currency;
import com.orbitmines.api.spigot.handlers.ItemSet;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.Obtainable;
import org.bukkit.Material;
import org.bukkit.Particle;

/*
* OrbitMines - @author Fadi Shawki - 7/28/2017
*/
public enum Trail implements Perk {

    FIREWORK_SPARK("Firework Spark", Particle.FIREWORKS_SPARK, Color.RED, new ItemSet(Material.FIREWORK), new Obtainable(OrbitMinesApi.VIP_POINTS, 400)),
    HAPPY_VILLAGER("Happy Villager", Particle.VILLAGER_HAPPY, Color.LIME, new ItemSet(Material.EMERALD), new Obtainable(VipRank.EMERALD)),
    HEART("Heart", Particle.HEART, Color.RED, new ItemSet(Material.NETHER_STALK), new Obtainable(OrbitMinesApi.VIP_POINTS, 300)),
    TNT("TNT", Particle.EXPLOSION_NORMAL, Color.MAROON, new ItemSet(Material.TNT), new Obtainable(OrbitMinesApi.VIP_POINTS, 475)),
    MAGIC("Magic", Particle.CRIT_MAGIC, Color.TEAL, new ItemSet(Material.INK_SACK, 6), new Obtainable(VipRank.IRON)),
    ANGRY_VILLAGER("Angry Villager", Particle.VILLAGER_ANGRY, Color.SILVER, new ItemSet(Material.COAL), new Obtainable(OrbitMinesApi.VIP_POINTS, 400)),
    LAVA("Lava", Particle.LAVA, Color.ORANGE, new ItemSet(Material.LAVA_BUCKET), new Obtainable(VipRank.DIAMOND)),
    SLIME("Slime", Particle.SLIME, Color.LIME, new ItemSet(Material.SLIME_BALL), new Obtainable(OrbitMinesApi.VIP_POINTS, 275)),
    SMOKE("Smoke", Particle.SMOKE_LARGE, Color.BLACK, new ItemSet(Material.INK_SACK), new Obtainable(OrbitMinesApi.VIP_POINTS, 325)),
    WITCH("Witch", Particle.SPELL_WITCH, Color.PURPLE, new ItemSet(Material.INK_SACK, 5), new Obtainable(OrbitMinesApi.VIP_POINTS, 350)),
    CRIT("Crit", Particle.CRIT_MAGIC, Color.AQUA, new ItemSet(Material.DIAMOND_SWORD), new Obtainable(OrbitMinesApi.VIP_POINTS, 375)),
    WATER("Water", Particle.WATER_SPLASH, Color.BLUE, new ItemSet(Material.WATER_BUCKET), new Obtainable(OrbitMinesApi.VIP_POINTS, 425)),
    MUSIC("Music", Particle.NOTE, Color.FUCHSIA, new ItemSet(Material.NOTE_BLOCK), new Obtainable(OrbitMinesApi.VIP_POINTS, 625)),
    SNOW("Snow", Particle.SNOW_SHOVEL, Color.WHITE, new ItemSet(Material.SNOW_BALL), new Obtainable(OrbitMinesApi.VIP_POINTS, 475)),
    ENCHANTMENT_TABLE("Enchantment Table", Particle.ENCHANTMENT_TABLE, Color.NAVY, new ItemSet(Material.ENCHANTMENT_TABLE), new Obtainable(OrbitMinesApi.VIP_POINTS, 400)),
    RAINBOW("Rainbow", Particle.REDSTONE, Color.MAROON, new ItemSet(Material.REDSTONE), new Obtainable(VipRank.GOLD)),
    BUBBLE("Bubble", Particle.SPELL, Color.WHITE, new ItemSet(Material.WEB), new Obtainable(OrbitMinesApi.VIP_POINTS, 375)),
    MOB_SPAWNER("Mob Spawner", Particle.FLAME, Color.SILVER, new ItemSet(Material.MOB_SPAWNER), new Obtainable(OrbitMinesApi.VIP_POINTS, 525));

    private final String name;

    private final Particle particle;
    private final Color color;

    private final ItemSet item;

    private final Obtainable obtainable;

    Trail(String name, Particle particle, Color color, ItemSet item, Obtainable obtainable) {
        this.name = name;
        this.particle = particle;
        this.color = color;
        this.item = item;
        this.obtainable = obtainable;
    }

    public String getName() {
        return name;
    }

    public Particle getParticle() {
        return particle;
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
        return color.getChatColor() + name + " Trail";
    }

    @Override
    public boolean hasAccess(OMPlayer omp) {
        return omp.trails().hasTrail(this);
    }
}
