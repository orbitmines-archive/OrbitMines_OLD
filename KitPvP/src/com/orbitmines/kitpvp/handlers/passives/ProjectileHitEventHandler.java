package com.orbitmines.kitpvp.handlers.passives;

import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import org.bukkit.event.entity.ProjectileHitEvent;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public interface ProjectileHitEventHandler {

    void onProjectileHit(ProjectileHitEvent event, KitPvPPlayer shooter);

}
