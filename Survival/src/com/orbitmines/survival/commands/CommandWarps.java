package com.orbitmines.survival.commands;

import com.orbitmines.api.spigot.commands.Command;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.survival.handlers.inventory.WarpInventory;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandWarps extends Command {

    private final String[] alias = {"/warps","/warp"};

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void dispatch(OMPlayer player, String[] a) {
        new WarpInventory(0, false).open(player);
    }
}
