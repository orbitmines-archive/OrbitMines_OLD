package com.orbitmines.api.spigot.commands.owner;

import com.orbitmines.api.Message;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.spigot.commands.StaffCommand;
import com.orbitmines.api.spigot.handlers.OMPlayer;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandOpMode extends StaffCommand {

    private final String[] alias = {"/opmode"};

    public CommandOpMode() {
        super(StaffRank.OWNER);
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void onDispatch(OMPlayer omp, String[] a) {
        omp.setOpMode(!omp.isOpMode());
        omp.sendMessage(new Message("§7Je §4§lOP Mode§7 staat nu " + omp.statusString(omp.isOpMode()), "§7Your §4§lOP Mode§7 has been " + omp.statusString(omp.isOpMode())));
    }
}
