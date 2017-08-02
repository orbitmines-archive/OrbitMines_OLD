package com.orbitmines.api.spigot.handlers.gadget;

import com.orbitmines.api.spigot.perks.Gadget;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 30-7-2017
*/
public abstract class InteractGadgetHandler extends GadgetHandler {

    private static List<InteractGadgetHandler> interactHandlers;

    public InteractGadgetHandler(Gadget gadget) {
        super(gadget);
    }

    public abstract void run();

    public abstract void onInteract(PlayerInteractEvent event);

    public static List<InteractGadgetHandler> getInteractHandlers() {
        return interactHandlers;
    }
}
