package com.orbitmines.api.spigot.commands.moderator;

import com.orbitmines.api.StaffRank;
import com.orbitmines.api.spigot.commands.StaffCommand;
import com.orbitmines.api.spigot.handlers.OMPlayer;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandSilent extends StaffCommand {

    private final String[] alias = {"/silent"};

    public CommandSilent() {
        super(StaffRank.MODERATOR);
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void onDispatch(OMPlayer omp, String[] a) {
        omp.setSilent(!omp.isSilent());
    }
}
