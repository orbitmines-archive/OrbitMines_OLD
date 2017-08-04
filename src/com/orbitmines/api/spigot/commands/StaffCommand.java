package com.orbitmines.api.spigot.commands;

import com.orbitmines.api.StaffRank;
import com.orbitmines.api.spigot.handlers.OMPlayer;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public abstract class StaffCommand extends Command {

    private StaffRank staffRank;

    public StaffCommand(StaffRank staffRank) {
        this.staffRank = staffRank;
    }

    public abstract void onDispatch(OMPlayer omp, String[] a);

    @Override
    public void dispatch(OMPlayer omp, String[] a) {
        if (omp.isEligible(staffRank))
            onDispatch(omp, a);
        else
            omp.msgUnknownCommand(a[0]);
    }
}
