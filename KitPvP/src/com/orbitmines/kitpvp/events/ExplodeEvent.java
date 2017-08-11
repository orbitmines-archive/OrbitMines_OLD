package com.orbitmines.kitpvp.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

/*
* OrbitMines - @author Fadi Shawki - 11-8-2017
*/
public class ExplodeEvent implements Listener {

    @EventHandler
    public void EntityExplodeEvent(EntityExplodeEvent e) {
        e.blockList().clear();
    }
}
