package com.orbitmines.api.spigot.commands.perks;

import com.orbitmines.api.spigot.commands.Command;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.inventory.perks.HatInventory;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandHats extends Command {

    private final String[] alias = {"/hats"};

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void dispatch(OMPlayer omp, String[] a) {
        new HatInventory().open(omp);
    }
}
