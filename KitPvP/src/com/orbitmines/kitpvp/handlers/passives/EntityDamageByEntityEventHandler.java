package com.orbitmines.kitpvp.handlers.passives;

import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public interface EntityDamageByEntityEventHandler {

    void onDamage(EntityDamageByEntityEvent event, KitPvPPlayer ompD, KitPvPPlayer ompE);

}
