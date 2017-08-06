package com.orbitmines.api.spigot.enablers;

import com.orbitmines.api.Data;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.playerdata.PlayerData;
import com.orbitmines.api.spigot.handlers.playerdata.WardrobeData;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class WardrobeEnabler extends Enabler {

    @Override
    public void onEnable() {
        api.enableData(Data.WARDROBE, new Data.Register() {
            @Override
            public PlayerData getData(OMPlayer omp) {
                return new WardrobeData(omp);
            }
        });
    }
}
