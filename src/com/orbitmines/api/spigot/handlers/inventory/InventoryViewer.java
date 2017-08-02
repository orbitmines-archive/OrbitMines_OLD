package com.orbitmines.api.spigot.handlers.inventory;

import com.orbitmines.api.spigot.handlers.OMPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.PlayerInventory;

/*
* OrbitMines - @author Fadi Shawki - 30-7-2017
*/
public class InventoryViewer extends OMInventory {

    private Player viewing;

    public InventoryViewer(Player viewing) {
        this.viewing = viewing;

        newInventory(45, "§0§l" + viewing.getName() + "'s Inventory");
    }

    @Override
    protected boolean onOpen(OMPlayer omp) {
        update();

        return true;
    }

    @Override
    protected void onClick(InventoryClickEvent event, OMPlayer omp) {

    }

    public void update() {
        PlayerInventory inv = viewing.getInventory();

        for(int i = 0; i < 27; i++){
            add(i, new EmptyItemInstance(inv.getItem(i + 9)));
        }
        for(int i = 27; i < 36; i++){
            add(i, new EmptyItemInstance(inv.getItem(i - 27)));
        }
        add(36, new EmptyItemInstance(inv.getHelmet()));
        add(37, new EmptyItemInstance(inv.getChestplate()));
        add(38, new EmptyItemInstance(inv.getLeggings()));
        add(39, new EmptyItemInstance(inv.getBoots()));
    }
}
