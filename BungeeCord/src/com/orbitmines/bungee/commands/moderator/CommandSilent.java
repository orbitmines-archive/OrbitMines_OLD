package com.orbitmines.bungee.commands.moderator;

import com.orbitmines.api.StaffRank;
import com.orbitmines.bungee.commands.StaffCommand;
import com.orbitmines.bungee.handlers.BungeePlayer;
import net.md_5.bungee.api.chat.TextComponent;

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
    public void onDispatch(BungeePlayer omp, String[] a) {
        omp.setSilent(!omp.isSilent());
        omp.getPlayer().sendMessage(new TextComponent("§6§lSilent Mode §8» " + omp.statusString(omp.isSilent()) + "§7!"));
    }
}
