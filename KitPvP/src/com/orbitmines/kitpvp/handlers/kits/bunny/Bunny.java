package com.orbitmines.kitpvp.handlers.kits.bunny;

import com.orbitmines.api.spigot.handlers.Cooldown;
import com.orbitmines.kitpvp.handlers.KitPvPKit;
import org.bukkit.Color;

/*
* OrbitMines - @author Fadi Shawki - 11-8-2017
*/
public class Bunny extends KitPvPKit {

    public static Color LEATHER_COLOR = Color.WHITE;
    public static Cooldown FLUFFY_BUNNY_COOLDOWN = new Cooldown(10 * 1000, "§fFluffy Bunny", "§fFluffy Bunny", Cooldown.Action.RIGHT_CLICK);

    public Bunny() {
        super(Type.BUNNY, 3);

        handlers[0] = new Bunny_LvL_1();
        handlers[1] = new Bunny_LvL_2();
        handlers[2] = new Bunny_LvL_3();
    }
}
