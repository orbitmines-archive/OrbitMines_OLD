package com.orbitmines.bungee.commands;

import com.orbitmines.api.StaffRank;
import com.orbitmines.bungee.handlers.BungeePlayer;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public abstract class StaffCommand extends Command {

    private StaffRank staffRank;

    public StaffCommand(StaffRank staffRank) {
        this.staffRank = staffRank;
    }

    public abstract void onDispatch(BungeePlayer omp, String[] a);

    @Override
    public void dispatch(BungeePlayer omp, String[] a) {
        if (omp.isEligible(staffRank))
            onDispatch(omp, a);
        else
            omp.msgUnknownCommand(a[0]);
    }
}
