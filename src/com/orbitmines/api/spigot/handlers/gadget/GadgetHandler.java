package com.orbitmines.api.spigot.handlers.gadget;

import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.perks.Gadget;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 30-7-2017
*/
public abstract class GadgetHandler implements Listener {

    protected OrbitMinesApi api;

    private static List<GadgetHandler> handlers = new ArrayList<>();

    private final int ticks;
    private int tickIndex;
    private final Gadget gadget;

    public GadgetHandler(Gadget gadget) {
        this(gadget, -1);
    }

    public GadgetHandler(Gadget gadget, int ticks) {
        api = OrbitMinesApi.getApi();

        handlers.add(this);

        this.ticks = ticks;
        tickIndex = 0;
        this.gadget = gadget;
        this.gadget.setHandler(this);
    }

    protected abstract void onRun();

    public abstract void onLogout(OMPlayer omp);

    public abstract void onInteract(PlayerInteractEvent event, OMPlayer omp);

    public void run() {
        if (ticks == -1)
            return;

        tickIndex++;

        if (ticks >= tickIndex) {
            onRun();

            tickIndex = 0;
        }
    }

    /* Override to change */
    public ItemStack getItem(OMPlayer omp) {
        Gadget gadget = getGadget();
        return new ItemBuilder(gadget.item().getMaterial(), 1, gadget.item().getDurability(), gadget.color().getChatColor() + "§l" + gadget.getName()).build();
    }

    public Gadget getGadget() {
        return gadget;
    }

    public static List<GadgetHandler> getHandlers() {
        return handlers;
    }
}
