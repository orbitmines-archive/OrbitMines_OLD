package com.orbitmines.api.spigot.handlers.playerdata;

import com.orbitmines.api.Data;
import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.handlers.OMPlayer;

/*
* OrbitMines - @author Fadi Shawki - 30-7-2017
*/
public abstract class PlayerData {

    /* Class is used to save data cross-servers */

    protected OrbitMinesApi api;

    private final Data type;
    protected final OMPlayer omp;

    /* Load Defaults in Constructor */
    public PlayerData(Data type, OMPlayer omp) {
        api = OrbitMinesApi.getApi();
        omp.getData().add(this);

        this.type = type;
        this.omp = omp;
    }

    /* Called when player is logging in */
    public abstract void onLogin();

    /* Called when player is logging out */
    public abstract void onLogout();


    /*  Serialized with '~' DO NOT USE
        DO NOT USE THE FOLLOWING CHARACTERS (used in Serializer): '|', ';', '/', ':'

        Use the following characters: '-', '<', '>', '='
    */

//    /* Override this and call super#serialize */
//    public String serialize() {
//        return getName() + "~";
//    }
    public abstract String serialize();

    public abstract void parse(String string);

    public Data getType() {
        return type;
    }

    public OMPlayer getPlayer() {
        return omp;
    }
}
