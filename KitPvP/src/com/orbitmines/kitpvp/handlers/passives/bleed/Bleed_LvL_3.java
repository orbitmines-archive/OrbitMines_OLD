package com.orbitmines.kitpvp.handlers.passives.bleed;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public class Bleed_LvL_3 extends Bleed {

    public Bleed_LvL_3() {
        super(3);
    }

    @Override
    public double getTotalDamage() {
        return 5.0D;
    }

    @Override
    public double getBleedChance() {
        return 0.225;
    }
}
