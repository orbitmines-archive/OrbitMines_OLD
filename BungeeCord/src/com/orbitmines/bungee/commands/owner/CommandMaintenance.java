package com.orbitmines.bungee.commands.owner;

import com.orbitmines.api.StaffRank;
import com.orbitmines.bungee.OrbitMinesBungee;
import com.orbitmines.bungee.commands.StaffCommand;
import com.orbitmines.bungee.handlers.BungeePlayer;
import net.md_5.bungee.api.chat.TextComponent;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandMaintenance extends StaffCommand {

    private OrbitMinesBungee bungee;

    private final String[] alias = {"/maintenance"};

    public CommandMaintenance() {
        super(StaffRank.OWNER);

        bungee = OrbitMinesBungee.getBungee();
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void onDispatch(BungeePlayer omp, String[] a) {
        bungee.setInMaintenance(!bungee.isInMaintenance());
        omp.getPlayer().sendMessage(new TextComponent("§dMaintenance Mode §8» " + omp.statusString(bungee.isInMaintenance())));

        //TODO PER SERVER
    }
}
