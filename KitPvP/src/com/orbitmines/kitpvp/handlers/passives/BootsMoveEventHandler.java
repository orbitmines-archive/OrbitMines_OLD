package com.orbitmines.kitpvp.handlers.passives;

import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import org.bukkit.event.player.PlayerMoveEvent;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public interface BootsMoveEventHandler {

    void onMove(PlayerMoveEvent event, KitPvPPlayer omp);

}
