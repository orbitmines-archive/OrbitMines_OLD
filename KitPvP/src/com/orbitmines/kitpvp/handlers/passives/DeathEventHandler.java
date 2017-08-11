package com.orbitmines.kitpvp.handlers.passives;

import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import org.bukkit.event.entity.PlayerDeathEvent;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public interface DeathEventHandler {

    void onKill(PlayerDeathEvent event, KitPvPPlayer died, KitPvPPlayer killer);

}
