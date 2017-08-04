package com.orbitmines.api.spigot.handlers.gadget.gadgets;

import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.gadget.GadgetHandler;
import com.orbitmines.api.spigot.handlers.playerdata.PetData;
import com.orbitmines.api.spigot.perks.Gadget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class GadgetPetRide extends GadgetHandler implements Listener {

    public GadgetPetRide() {
        super(Gadget.PET_RIDE);

        api.getServer().getPluginManager().registerEvents(this, api);
    }

    @Override
    public void onRun() {
        //TODO PET INVENTORY
    }

    @Override
    public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
        event.setCancelled(true);
        omp.updateInventory();
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        Player p = event.getPlayer();
        OMPlayer omp = OMPlayer.getPlayer(p);
        PetData data = omp.pets();

        if (!data.hasPetEnabled() || data.getPet().getEntityId() != event.getRightClicked().getEntityId())
            return;

        event.setCancelled(true);
        event.getRightClicked().addPassenger(p);
        omp.updateInventory();
    }
}
