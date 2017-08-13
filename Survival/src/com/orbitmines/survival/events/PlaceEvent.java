package com.orbitmines.survival.events;

import com.orbitmines.survival.Survival;
import com.orbitmines.survival.handlers.SurvivalPlayer;
import com.orbitmines.survival.handlers.region.Region;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

/*
* OrbitMines - @author Fadi Shawki - 12-8-2017
*/
public class PlaceEvent implements Listener {

    private Survival survival;

    public PlaceEvent() {
        survival = Survival.getInstance();
    }

    @EventHandler
    public void onBreak(BlockPlaceEvent event) {
        Player p = event.getPlayer();

        if (!p.getWorld().getName().equals(survival.getWorld().getName()))
            return;

        SurvivalPlayer omp = SurvivalPlayer.getPlayer(p);

        if (!Region.isInRegion(omp, event.getBlock().getLocation()))
            return;

        event.setCancelled(true);
        omp.updateInventory();
    }
}
