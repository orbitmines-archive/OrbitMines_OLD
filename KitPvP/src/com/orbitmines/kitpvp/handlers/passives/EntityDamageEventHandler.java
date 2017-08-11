package com.orbitmines.kitpvp.handlers.passives;

import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import org.bukkit.event.entity.EntityDamageEvent;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public interface EntityDamageEventHandler {

    void onDamage(EntityDamageEvent event, KitPvPPlayer damaged);

}
