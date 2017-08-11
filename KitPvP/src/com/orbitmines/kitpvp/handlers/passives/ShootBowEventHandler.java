package com.orbitmines.kitpvp.handlers.passives;

import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import org.bukkit.event.entity.EntityShootBowEvent;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public interface ShootBowEventHandler extends ProjectileHitEventHandler {

    void onShootBowEvent(EntityShootBowEvent event, KitPvPPlayer shooter);

}
