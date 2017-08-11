package com.orbitmines.kitpvp.handlers.kits.knight;

import com.orbitmines.kitpvp.handlers.KitPvPKit;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public class Knight extends KitPvPKit {

    public Knight() {
        super(Type.KNIGHT, 3);

        handlers[0] = new Knight_LvL_1();
        handlers[1] = new Knight_LvL_2();
        handlers[2] = new Knight_LvL_3();
    }
}
