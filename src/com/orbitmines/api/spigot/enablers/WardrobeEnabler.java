package com.orbitmines.api.spigot.enablers;

import com.orbitmines.api.Data;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class WardrobeEnabler extends Enabler {

    @Override
    public void onEnable() {
        api.enableData(Data.WARDROBE);
    }
}
