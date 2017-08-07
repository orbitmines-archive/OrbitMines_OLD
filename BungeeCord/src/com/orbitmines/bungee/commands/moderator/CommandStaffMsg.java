package com.orbitmines.bungee.commands.moderator;

import com.orbitmines.api.StaffRank;
import com.orbitmines.bungee.commands.StaffCommand;
import com.orbitmines.bungee.handlers.BungeePlayer;
import net.md_5.bungee.api.chat.TextComponent;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandStaffMsg extends StaffCommand {

    private final String[] alias = {"/staffmsg"};

    public CommandStaffMsg() {
        super(StaffRank.MODERATOR);
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void onDispatch(BungeePlayer omp, String[] a) {
        omp.setShowStaffMsg(!omp.showStaffMsg());
        omp.getPlayer().sendMessage(new TextComponent("§dStaff Messages §8» " + omp.statusString(omp.showStaffMsg()) + "§7!"));
    }
}
