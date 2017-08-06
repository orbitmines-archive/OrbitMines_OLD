package com.orbitmines.api;

import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.playerdata.PlayerData;

/*
* OrbitMines - @author Fadi Shawki - 30-7-2017
*/
public enum Data {

    GENERAL,

    /* Perks */
    CHATCOLORS,
    DISGUISES,
    GADGETS,
    HATS,
    PETS,
    TRAILS,
    WARDROBE;

    private Register register;

    public void register(Register register) {
        this.register = register;
    }

    public PlayerData getData(OMPlayer omp) {
        return register.getData(omp);
    }

    public static abstract class Register {

        public abstract PlayerData getData(OMPlayer omp);

    }
}
