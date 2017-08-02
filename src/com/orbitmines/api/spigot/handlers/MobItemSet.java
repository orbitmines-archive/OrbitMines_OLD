package com.orbitmines.api.spigot.handlers;

import com.orbitmines.api.spigot.Mob;
import org.bukkit.Material;

/*
* OrbitMines - @author Fadi Shawki - 28-7-2017
*/
public class MobItemSet extends ItemSet {

    private final Mob mob;

    public MobItemSet(Mob mob) {
        super(Material.MONSTER_EGG);

        this.mob = mob;
    }

    public Mob getMob() {
        return mob;
    }
}
