package com.orbitmines.api.spigot.handlers.gadget.gadgets;

import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.gadget.GadgetHandler;
import com.orbitmines.api.spigot.handlers.gadget.petride.PetHandler;
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
        super(Gadget.PET_RIDE, 1);

        api.getServer().getPluginManager().registerEvents(this, api);
    }

    @Override
    public void onRun() {
        for (PetHandler petHandler : PetHandler.getHandlers()) {
            petHandler.run();
        }
    }

    @Override
    public void onLogout(OMPlayer omp) {
        /* We clear the PetHandler in GadgetData, we want the player to still be able to use another gadget if a pet 'ability' is active */
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

        p.getInventory().clear();

        PetHandler handler = data.getPetEnabled().getHandler();
        for (int slot : handler.getItems().keySet()) {
            p.getInventory().setItem(slot, handler.get(slot).getItem(omp, data));
        }

        omp.updateInventory();
    }
}
