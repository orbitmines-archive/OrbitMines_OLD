package com.orbitmines.bungee.commands;

import com.orbitmines.api.Server;
import com.orbitmines.bungee.handlers.BungeePlayer;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandHub extends Command {

    private final String[] alias = {"/hub","/lobby"};

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void dispatch(BungeePlayer omp, String[] a) {
        omp.connect(Server.HUB);
    }
}
