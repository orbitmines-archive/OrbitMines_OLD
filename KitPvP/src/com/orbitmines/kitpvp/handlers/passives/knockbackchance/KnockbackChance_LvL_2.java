package com.orbitmines.kitpvp.handlers.passives.knockbackchance;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public class KnockbackChance_LvL_2 extends KnockbackChance {

    public KnockbackChance_LvL_2() {
        super(2);
    }

    @Override
    public int getKnockbackLevel() {
        return 2;
    }

    @Override
    public double getKnockbackChance() {
        return 0.50D;
    }
}
