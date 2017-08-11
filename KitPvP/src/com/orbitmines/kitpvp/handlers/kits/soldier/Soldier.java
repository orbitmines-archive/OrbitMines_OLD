package com.orbitmines.kitpvp.handlers.kits.soldier;

import com.orbitmines.kitpvp.handlers.KitPvPKit;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public class Soldier extends KitPvPKit {

    public Soldier() {
        super(Type.SOLDIER, 3);

        handlers[0] = new Soldier_LvL_1();
        handlers[1] = new Soldier_LvL_2();
        handlers[2] = new Soldier_LvL_3();
    }
}
