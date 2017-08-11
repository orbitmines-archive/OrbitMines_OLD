package com.orbitmines.kitpvp.handlers.passives.thor;

import com.orbitmines.api.utils.RandomUtils;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public class Thor_LvL_1 extends Thor {

    public Thor_LvL_1() {
        super(1);
    }

    @Override
    public boolean strikeLightning() {
        return RandomUtils.RANDOM.nextInt(4) == 0;
    }

    @Override
    public boolean onlyTarget() {
        return false;
    }

    @Override
    public double getDamage() {
        return 5.0;
    }
}
