package com.orbitmines.kitpvp.handlers.passives.bleed;

import com.orbitmines.api.utils.RandomUtils;
import com.orbitmines.kitpvp.Passive;
import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import com.orbitmines.kitpvp.handlers.passives.EntityDamageByEntityEventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public abstract class Bleed extends Passive implements EntityDamageByEntityEventHandler {

    public Bleed(int level) {
        super(Type.BLEED, level);
    }

    public abstract double getTotalDamage();

    public abstract double getBleedChance();

    @Override
    public void onDamage(EntityDamageByEntityEvent event, KitPvPPlayer ompD, KitPvPPlayer ompE) {
        if (RandomUtils.RANDOM.nextDouble() >= getBleedChance())
            return;

        ompE.setBleedingTime((int) (getTotalDamage() / 0.5));
    }
}
