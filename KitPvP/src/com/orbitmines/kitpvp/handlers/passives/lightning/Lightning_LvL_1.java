package com.orbitmines.kitpvp.handlers.passives.lightning;

import com.orbitmines.api.utils.RandomUtils;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public class Lightning_LvL_1 extends Lightning {

    public Lightning_LvL_1() {
        super(1);
    }

    @Override
    public boolean strikeLightning() {
        return RandomUtils.RANDOM.nextBoolean();
    }

    @Override
    public boolean onlyTarget() {
        return true;
    }

    @Override
    public double getDamage() {
        return 2.0D;
    }

    @Override
    public int amount() {
        return RandomUtils.random(1, 3);
    }
}
