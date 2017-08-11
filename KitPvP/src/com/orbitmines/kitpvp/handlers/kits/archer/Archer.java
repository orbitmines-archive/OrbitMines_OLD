package com.orbitmines.kitpvp.handlers.kits.archer;

import com.orbitmines.kitpvp.handlers.KitPvPKit;
import org.bukkit.Color;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public class Archer extends KitPvPKit {

    public static Color LEATHER_COLOR = Color.fromBGR(204, 255, 51);

    public Archer() {
        super(Type.ARCHER, 3);

        handlers[0] = new Archer_LvL_1();
        handlers[1] = new Archer_LvL_2();
        handlers[2] = new Archer_LvL_3();
    }
}
