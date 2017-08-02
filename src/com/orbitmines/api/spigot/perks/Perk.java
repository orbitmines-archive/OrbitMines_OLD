package com.orbitmines.api.spigot.perks;

import com.orbitmines.api.spigot.handlers.ItemSet;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.Obtainable;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public interface Perk {

    ItemSet item();

    Obtainable obtainable();

    String getDisplayName();

    boolean hasAccess(OMPlayer omp);

}
