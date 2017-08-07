package com.orbitmines.bungee.commands;

import com.orbitmines.bungee.handlers.BungeePlayer;
import net.md_5.bungee.api.chat.TextComponent;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandWebsite extends Command {

    private final String[] alias = {"/website","/site"};

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void dispatch(BungeePlayer omp, String[] a) {
        omp.getPlayer().sendMessage(new TextComponent("§7§lWebsite: §6www.orbitmines.com"));
    }
}
