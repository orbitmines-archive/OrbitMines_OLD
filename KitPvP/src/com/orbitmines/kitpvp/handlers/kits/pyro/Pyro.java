package com.orbitmines.kitpvp.handlers.kits.pyro;

import com.orbitmines.kitpvp.handlers.KitPvPKit;
import com.orbitmines.kitpvp.handlers.kits.wizard.Wizard_LvL_1;
import com.orbitmines.kitpvp.handlers.kits.wizard.Wizard_LvL_2;
import com.orbitmines.kitpvp.handlers.kits.wizard.Wizard_LvL_3;

/*
* OrbitMines - @author Fadi Shawki - 11-8-2017
*/
public class Pyro extends KitPvPKit {

    public Pyro() {
        super(Type.PYRO, 3);

        handlers[0] = new Pyro_LvL_1();
        handlers[1] = new Pyro_LvL_2();
        handlers[2] = new Pyro_LvL_3();
    }
}
