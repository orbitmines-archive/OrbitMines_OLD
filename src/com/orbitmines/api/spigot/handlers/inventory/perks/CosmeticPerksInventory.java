package com.orbitmines.api.spigot.handlers.inventory.perks;

import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.inventory.OMInventory;
import org.bukkit.event.inventory.InventoryClickEvent;

/*
* OrbitMines - @author Fadi Shawki - 30-7-2017
*/
public class CosmeticPerksInventory extends OMInventory {

    public CosmeticPerksInventory() {
        newInventory(36, "§0§lCosmetic Perks");
    }

    @Override
    protected boolean onOpen(OMPlayer omp) {
        return true;
    }

    @Override
    protected void onClick(InventoryClickEvent event, OMPlayer omp) {

    }
}
