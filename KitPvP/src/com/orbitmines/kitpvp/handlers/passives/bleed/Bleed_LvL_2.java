package com.orbitmines.kitpvp.handlers.passives.bleed;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public class Bleed_LvL_2 extends Bleed {

    public Bleed_LvL_2() {
        super(2);
    }

    @Override
    public double getTotalDamage() {
        return 5.0D;
    }

    @Override
    public double getBleedChance() {
        return 0.15;
    }
}
