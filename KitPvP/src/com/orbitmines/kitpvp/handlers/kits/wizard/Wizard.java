package com.orbitmines.kitpvp.handlers.kits.wizard;

import com.orbitmines.kitpvp.handlers.KitPvPKit;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public class Wizard extends KitPvPKit {

    public Wizard() {
        super(Type.WIZARD, 3);

        handlers[0] = new Wizard_LvL_1();
        handlers[1] = new Wizard_LvL_2();
        handlers[2] = new Wizard_LvL_3();
    }
}
