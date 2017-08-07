package com.orbitmines.bungee.commands.moderator;

import com.orbitmines.api.StaffRank;
import com.orbitmines.bungee.commands.StaffCommand;
import com.orbitmines.bungee.handlers.BungeePlayer;
import net.md_5.bungee.api.chat.TextComponent;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandAllChat extends StaffCommand {

    private final String[] alias = {"/all","/allchat"};

    public CommandAllChat() {
        super(StaffRank.MODERATOR);
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void onDispatch(BungeePlayer omp, String[] a) {
        omp.setAllChat(!omp.allChat());
        omp.getPlayer().sendMessage(new TextComponent("§d§lAllChat §8» " + omp.statusString(omp.allChat()) + "§7!"));
    }
}
