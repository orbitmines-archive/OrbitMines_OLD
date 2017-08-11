package com.orbitmines.kitpvp.handlers.passives.knockbackchance;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public class KnockbackChance_LvL_3 extends KnockbackChance {

    public KnockbackChance_LvL_3() {
        super(3);
    }

    @Override
    public int getKnockbackLevel() {
        return 3;
    }

    @Override
    public double getKnockbackChance() {
        return 0.55D;
    }
}
