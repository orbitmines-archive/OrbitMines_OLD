package com.orbitmines.bungee.commands;

import com.orbitmines.api.VipRank;
import com.orbitmines.bungee.handlers.BungeePlayer;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public abstract class VipCommand extends Command {

    private VipRank vipRank;

    public VipCommand(VipRank vipRank) {
        this.vipRank = vipRank;
    }

    public abstract void onDispatch(BungeePlayer omp, String[] a);

    @Override
    public void dispatch(BungeePlayer omp, String[] a) {
        if (omp.isEligible(vipRank))
            onDispatch(omp, a);
        else
            omp.msgRequiredVipRank(vipRank);
    }
}
