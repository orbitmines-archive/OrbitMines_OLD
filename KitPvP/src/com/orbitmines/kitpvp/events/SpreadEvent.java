package com.orbitmines.kitpvp.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockSpreadEvent;

/*
* OrbitMines - @author Fadi Shawki - 11-8-2017
*/
public class SpreadEvent implements Listener {

    @EventHandler
    public void onSpread(BlockSpreadEvent event) {
        event.setCancelled(true);
    }
}
