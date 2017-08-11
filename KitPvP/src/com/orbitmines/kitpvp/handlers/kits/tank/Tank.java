package com.orbitmines.kitpvp.handlers.kits.tank;

import com.orbitmines.kitpvp.handlers.KitPvPKit;
import com.orbitmines.kitpvp.handlers.kits.wizard.Wizard_LvL_1;
import com.orbitmines.kitpvp.handlers.kits.wizard.Wizard_LvL_2;
import com.orbitmines.kitpvp.handlers.kits.wizard.Wizard_LvL_3;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public class Tank extends KitPvPKit {

    public Tank() {
        super(Type.TANK, 3);

        handlers[0] = new Tank_LvL_1();
        handlers[1] = new Tank_LvL_2();
        handlers[2] = new Tank_LvL_3();
    }
}
