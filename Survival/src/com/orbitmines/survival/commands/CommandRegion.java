package com.orbitmines.survival.commands;

import com.orbitmines.api.spigot.commands.Command;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.survival.handlers.inventory.RegionInventory;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandRegion extends Command {

    private final String[] alias = {"/region","/regions"};

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void dispatch(OMPlayer player, String[] a) {
        new RegionInventory(0, 0).open(player);
    }
}
