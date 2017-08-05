package com.orbitmines.api.spigot.enablers;

import com.orbitmines.api.spigot.OrbitMinesApi;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public abstract class Enabler {

    protected OrbitMinesApi api;

    public Enabler() {
        api = OrbitMinesApi.getApi();
    }

    public abstract void onEnable();

}
