package com.orbitmines.api.spigot.commands;

import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.inventory.ServerSelectorInventory;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandServers extends Command {

    private final String[] alias = {"/servers"};

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void dispatch(OMPlayer omp, String[] a) {
        new ServerSelectorInventory().open(omp);
    }
}
