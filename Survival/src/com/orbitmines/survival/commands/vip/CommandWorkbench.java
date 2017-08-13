package com.orbitmines.survival.commands.vip;

import com.orbitmines.api.VipRank;
import com.orbitmines.api.spigot.commands.VipCommand;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import org.bukkit.entity.Player;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandWorkbench extends VipCommand {

    private final String[] alias = {"/workbench"};

    public CommandWorkbench() {
        super(VipRank.DIAMOND);
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void onDispatch(OMPlayer omp, String[] a) {
        Player p = omp.getPlayer();
        p.openWorkbench(null, true);
    }
}
