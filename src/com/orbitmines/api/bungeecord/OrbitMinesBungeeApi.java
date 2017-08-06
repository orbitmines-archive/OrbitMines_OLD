package com.orbitmines.api.bungeecord;

import net.md_5.bungee.api.plugin.Plugin;

/*
* OrbitMines - @author Fadi Shawki - 7/28/2017
*/
public class OrbitMinesBungeeApi extends Plugin {

    private static OrbitMinesBungeeApi api;

    @Override
    public void onEnable() {
        api = this;
    }

    public static OrbitMinesBungeeApi getApi() {
        return api;
    }
}
