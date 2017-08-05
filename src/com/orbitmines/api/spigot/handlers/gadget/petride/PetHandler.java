package com.orbitmines.api.spigot.handlers.gadget.petride;

import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.playerdata.PetData;
import com.orbitmines.api.spigot.perks.Pet;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public abstract class PetHandler {

    protected OrbitMinesApi api;

    private static List<PetHandler> handlers = new ArrayList<>();

    private final int ticks;
    private int tickIndex;
    private final Pet pet;

    private Map<Integer, ItemInstance> items;

    public PetHandler(Pet pet) {
        this(pet, -1);
    }

    public PetHandler(Pet pet, int ticks) {
        api = OrbitMinesApi.getApi();

        handlers.add(this);

        this.ticks = ticks;
        tickIndex = 0;
        this.pet = pet;
        this.items = new HashMap<>();

        this.pet.setHandler(this);
    }

    protected abstract void onRun();

    public abstract void onLogout(OMPlayer omp);

    public void run() {
        if (ticks == -1)
            return;

        tickIndex++;

        if (ticks >= tickIndex) {
            onRun();

            tickIndex = 0;
        }
    }

    public Pet getPet() {
        return pet;
    }

    protected void add(int slot, ItemInstance itemInstance) {
        this.items.put(slot, itemInstance);
    }

    public ItemInstance get(int slot) {
        return items.get(slot);
    }

    public Map<Integer, ItemInstance> getItems() {
        return items;
    }

    public static List<PetHandler> getHandlers() {
        return handlers;
    }

    public abstract static class ItemInstance {

        public abstract void onInteract(PlayerInteractEvent event, OMPlayer omp, PetData petData);

        public abstract ItemStack getItem(OMPlayer omp, PetData petData);

    }
}
