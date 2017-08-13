package com.orbitmines.survival.handlers;

import com.orbitmines.api.spigot.Color;

/*
* OrbitMines - @author Fadi Shawki - 12-8-2017
*/
public interface Teleportable {

    void teleportInstantly(SurvivalPlayer omp);

    Color getColor();

    String getName();

}
