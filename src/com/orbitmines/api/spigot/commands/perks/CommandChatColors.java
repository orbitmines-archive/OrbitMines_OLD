package com.orbitmines.api.spigot.commands.perks;

import com.orbitmines.api.spigot.commands.Command;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.inventory.perks.ChatColorInventory;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandChatColors extends Command {

    private final String[] alias = {"/chatcolors"};

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void dispatch(OMPlayer omp, String[] a) {
        new ChatColorInventory().open(omp);
    }
}
