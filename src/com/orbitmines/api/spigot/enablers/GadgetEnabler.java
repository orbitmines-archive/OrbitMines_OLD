package com.orbitmines.api.spigot.enablers;

import com.orbitmines.api.Data;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.gadget.GadgetHandler;
import com.orbitmines.api.spigot.handlers.gadget.gadgets.*;
import com.orbitmines.api.spigot.handlers.gadget.petride.PetHandler;
import com.orbitmines.api.spigot.handlers.playerdata.GadgetData;
import com.orbitmines.api.spigot.handlers.playerdata.PetData;
import com.orbitmines.api.spigot.handlers.playerdata.PlayerData;
import com.orbitmines.api.spigot.perks.Gadget;
import com.orbitmines.api.spigot.runnable.OMRunnable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public abstract class GadgetEnabler extends Enabler implements Listener {

    public abstract int getGadgetSlot();

    @Override
    public void onEnable() {
        api.enableData(Data.GADGETS, new Data.Register() {
            @Override
            public PlayerData getData(OMPlayer omp) {
                return new GadgetData(omp);
            }
        });

        api.getServer().getPluginManager().registerEvents(this, api);

        for (Gadget gadget : Gadget.values()) {
            switch (gadget) {

                case STACKER:
                    gadget.setHandler(new GadgetStacker());
                    break;
                case PAINTBALLS:
                    gadget.setHandler(new GadgetPaintballs());
                    break;
                case CREEPER_LAUNCHER:
                    gadget.setHandler(new GadgetCreeperLauncher());
                    break;
                case PET_RIDE:
                    gadget.setHandler(new GadgetPetRide());
                    break;
                case BOOK_EXPLOSION:
                    gadget.setHandler(new GadgetBookExplosion());
                    break;
                case SWAP_TELEPORTER:
                    gadget.setHandler(new GadgetSwapTeleporter());
                    break;
                case FIREWORK_GUN:
                    gadget.setHandler(new GadgetFireworkGun());
                    break;
                case SNOWMAN_ATTACK:
                    gadget.setHandler(new GadgetSnowmanAttack());
                    break;
                case FLAME_THROWER:
                    gadget.setHandler(new GadgetFlameThrower());
                    break;
                case GRAPPLING_HOOK:
                    gadget.setHandler(new GadgetGrapplingHook());
                    break;
                case MAGMA_CUBE_SOCCER:
                    gadget.setHandler(new GadgetMagmaCubeSoccer());
                    break;
            }
        }

        new OMRunnable(OMRunnable.TimeUnit.TICK, 1) {
            @Override
            public void run() {
                for (GadgetHandler gadgetHandler : GadgetHandler.getHandlers()) {
                    gadgetHandler.run();
                }
            }
        };
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        OMPlayer omp = OMPlayer.getPlayer(event.getPlayer());
        GadgetData data = omp.gadgets();
        int slot = event.getPlayer().getInventory().getHeldItemSlot();

        if (api.isPetEnabled()) {
            PetData petData = omp.pets();

            if (petData.hasPetEnabled() && omp.getPlayer().getVehicle() == petData.getPet()) {
                PetHandler handler = petData.getPetEnabled().getHandler();
                if (handler.getItems().containsKey(slot))
                    handler.get(slot).onInteract(event, omp, petData);

                return;
            }
        }

        if (data.getGadgetEnabled() == null || slot != api.gadgets().getGadgetSlot())
            return;

        data.getGadgetEnabled().getHandler().onInteract(event, omp);
    }
}
