package com.orbitmines.kitpvp.handlers;

import com.orbitmines.api.spigot.handlers.Obtainable;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.kit.KitInteractive;
import com.orbitmines.kitpvp.Active;
import com.orbitmines.kitpvp.HealthRegenType;
import com.orbitmines.kitpvp.Passive;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public abstract class KitHandler {

    protected final KitPvPKit.Type type;
    protected final int level;
    protected final Obtainable obtainable;

    protected final List<Passive> passives;

    protected final KitInteractive kit;

    public KitHandler(KitPvPKit.Type type, int level, Obtainable obtainable) {
        this.type = type;
        this.level = level;
        this.obtainable = obtainable;
        this.passives = new ArrayList<>();
        kit = registerKit();
    }

    protected abstract KitInteractive registerKit();

    public abstract void update(KitPvPPlayer omp);

    public abstract double getMaxHealth();

    public abstract double getKnockbackMultiplier();

    public abstract double getDamageMultiplier();

    public abstract HealthRegenType getHealthRegenType(KitPvPPlayer omp);

    public KitPvPKit.Type getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public Obtainable obtainable() {
        return obtainable;
    }

    public KitInteractive getKit() {
        return kit;
    }

    public String name() {
        return type.getDisplayName() + " §a§lLvL " + level + " §8|| ";
    }

    protected ItemBuilder add(ItemBuilder builder, Active... abilities) {
        if (builder.getLore() == null)
            builder.setLore(new ArrayList<>());

        List<String> lore = builder.getLore();
        for (Active active : abilities) {
            lore.add(active.getName());
        }

        if (builder.getEnchantments() == null || builder.getEnchantments().size() == 0)
            builder.glow();

        return builder;
    }

    protected ItemBuilder add(ItemBuilder builder, Passive... passives) {
        if (builder.getLore() == null)
            builder.setLore(new ArrayList<>());

        List<String> lore = builder.getLore();
        for (Passive passive : passives) {
            lore.add(passive.getName());
            this.passives.add(passive);
        }

        if (builder.getEnchantments() == null || builder.getEnchantments().size() == 0)
            builder.glow();

        return builder;
    }

    public Passive getPassive(ItemStack itemStack) {
        if (itemStack.getItemMeta() == null || itemStack.getItemMeta().getLore() == null)
            return null;

        List<String> lore = itemStack.getItemMeta().getLore();

        for (Passive passive : passives) {
            if (lore.contains(passive.getName()))
                return passive;
        }

        return null;
    }
}
