package com.orbitmines.api.spigot.commands.perks;

import com.orbitmines.api.spigot.commands.Command;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.inventory.perks.FireworkInventory;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandFireworks extends Command {

    private final String[] alias = {"/fireworks"};

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void dispatch(OMPlayer omp, String[] a) {
        new FireworkInventory().open(omp);
    }
}
