package com.orbitmines.api.spigot.handlers.gadget;

import com.orbitmines.api.spigot.perks.Gadget;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 30-7-2017
*/
public abstract class GadgetHandler {

    private static List<GadgetHandler> handlers = new ArrayList<>();

    private final Gadget gadget;

    public GadgetHandler(Gadget gadget) {
        handlers.add(this);

        this.gadget = gadget;
    }

    public abstract void run();

    public Gadget getGadget() {
        return gadget;
    }

    public static List<GadgetHandler> getHandlers() {
        return handlers;
    }
}
