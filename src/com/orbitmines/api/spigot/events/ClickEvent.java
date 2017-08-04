package com.orbitmines.api.spigot.events;

import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.inventory.OMInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class ClickEvent implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        OMPlayer omp = OMPlayer.getPlayer((Player) event.getWhoClicked());
        OMInventory lastInventory = omp.getLastInventory();

        if(lastInventory == null)
            return;

        lastInventory.processClickEvent(event, omp);
    }
}
