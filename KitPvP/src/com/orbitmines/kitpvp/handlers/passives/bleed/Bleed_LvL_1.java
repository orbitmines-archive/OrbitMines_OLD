package com.orbitmines.kitpvp.handlers.passives.bleed;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public class Bleed_LvL_1 extends Bleed {

    public Bleed_LvL_1() {
        super(1);
    }

    @Override
    public double getTotalDamage() {
        return 5.0D;
    }

    @Override
    public double getBleedChance() {
        return 0.075;
    }
}
