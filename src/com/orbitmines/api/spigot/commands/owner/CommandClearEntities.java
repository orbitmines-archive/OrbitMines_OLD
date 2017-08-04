package com.orbitmines.api.spigot.commands.owner;

import com.orbitmines.api.StaffRank;
import com.orbitmines.api.spigot.commands.StaffCommand;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.utils.WorldUtils;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandClearEntities extends StaffCommand {

    private final String[] alias = {"/clearentities"};

    public CommandClearEntities() {
        super(StaffRank.OWNER);
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void onDispatch(OMPlayer omp, String[] a) {
        WorldUtils.removeEntities(omp.getPlayer().getWorld());
    }
}
