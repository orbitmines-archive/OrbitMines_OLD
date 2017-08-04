package com.orbitmines.api.spigot.commands;

import com.orbitmines.api.VipRank;
import com.orbitmines.api.spigot.handlers.OMPlayer;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public abstract class VipCommand extends Command {

    private VipRank vipRank;

    public VipCommand(VipRank vipRank) {
        this.vipRank = vipRank;
    }

    public abstract void onDispatch(OMPlayer omp, String[] a);

    @Override
    public void dispatch(OMPlayer omp, String[] a) {
        if (omp.isEligible(vipRank))
            onDispatch(omp, a);
        else
            omp.msgRequiredVipRank(vipRank);
    }
}
