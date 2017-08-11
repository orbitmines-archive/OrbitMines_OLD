package com.orbitmines.kitpvp.handlers.kits.drunk;

import com.orbitmines.kitpvp.handlers.KitPvPKit;
import com.orbitmines.kitpvp.handlers.kits.knight.Knight_LvL_1;
import com.orbitmines.kitpvp.handlers.kits.knight.Knight_LvL_2;
import com.orbitmines.kitpvp.handlers.kits.knight.Knight_LvL_3;

/*
* OrbitMines - @author Fadi Shawki - 11-8-2017
*/
public class Drunk extends KitPvPKit {

    public Drunk() {
        super(Type.DRUNK, 3);

        handlers[0] = new Drunk_LvL_1();
        handlers[1] = new Drunk_LvL_2();
        handlers[2] = new Drunk_LvL_3();
    }
}
