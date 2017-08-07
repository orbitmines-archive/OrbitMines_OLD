package com.orbitmines.bungee.commands;

import com.orbitmines.api.Message;
import com.orbitmines.bungee.handlers.BungeePlayer;
import net.md_5.bungee.api.chat.TextComponent;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandDonate extends Command {

    private final String[] alias = {"/donate","/buy","/winkel","/shop"};

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void dispatch(BungeePlayer omp, String[] a) {
        omp.getPlayer().sendMessage(new TextComponent("§7§l" + omp.getMessage(new Message("Winkel", "Shop")) + ": §3shop.orbitmines.com"));
    }
}
