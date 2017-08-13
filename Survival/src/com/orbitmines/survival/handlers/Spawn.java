package com.orbitmines.survival.handlers;

import com.orbitmines.api.spigot.Color;
import com.orbitmines.survival.Survival;

/*
* OrbitMines - @author Fadi Shawki - 12-8-2017
*/
public class Spawn implements Teleportable {

    public static Spawn SPAWN = new Spawn();

    @Override
    public void teleportInstantly(SurvivalPlayer omp) {
        omp.getPlayer().teleport(Survival.getInstance().getSpawnLocation());
    }

    @Override
    public Color getColor() {
        return Color.ORANGE;
    }

    @Override
    public String getName() {
        return "Spawn";
    }
}
