package com.orbitmines.api.spigot.handlers.gadget;

import com.orbitmines.api.spigot.perks.Gadget;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 30-7-2017
*/
public abstract class DamageEntityGadgetHandler extends GadgetHandler {

    private static List<DamageEntityGadgetHandler> damageEntityHandlers = new ArrayList<>();

    public DamageEntityGadgetHandler(Gadget gadget) {
        super(gadget);
    }

    public abstract void run();

    public abstract void onDamage(EntityDamageByEntityEvent event);

    public static List<DamageEntityGadgetHandler> getDamageEntityHandlers() {
        return damageEntityHandlers;
    }
}
