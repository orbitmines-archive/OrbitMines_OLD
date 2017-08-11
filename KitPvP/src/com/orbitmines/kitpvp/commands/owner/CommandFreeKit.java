package com.orbitmines.kitpvp.commands.owner;

import com.orbitmines.api.Language;
import com.orbitmines.api.Message;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.spigot.commands.StaffCommand;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.kitpvp.KitPvP;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandFreeKit extends StaffCommand {

    private final String[] alias = {"/freekits"};
    private KitPvP kitPvP;

    public CommandFreeKit() {
        super(StaffRank.OWNER);

        kitPvP = KitPvP.getInstance();
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void onDispatch(OMPlayer omp, String[] a) {
        kitPvP.setFreeKitEnabled(!kitPvP.isFreeKitEnabled());
        new Message("§d§lFree Kits§7 zijn " + omp.statusString(Language.DUTCH, kitPvP.isFreeKitEnabled()) + " §7gezet door " + omp.getName() + "§7.", "§d§lFree Kits§7 have been " + omp.statusString(Language.ENGLISH, kitPvP.isFreeKitEnabled()) + " §7by " + omp.getName() + "§7.").broadcast();
    }
}
