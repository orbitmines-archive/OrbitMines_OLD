package com.orbitmines.api.spigot.handlers.gadget;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.perks.Gadget;
import org.bukkit.Effect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/*
* OrbitMines - @author Fadi Shawki - 30-7-2017
*/
public class GadgetStacker extends DamageEntityGadgetHandler {

    public GadgetStacker(Gadget gadget) {
        super(gadget);
    }

    @Override
    public void run() {

    }

    @Override
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player))
            return;

        Player pE = (Player) event.getEntity();
        Player pD = (Player) event.getDamager();
        OMPlayer ompE = OMPlayer.getOMPlayer(pE);
        OMPlayer ompD = OMPlayer.getOMPlayer(pD);

        if (!ompE.canReceiveVelocity() || !ompD.canReceiveVelocity() || !ompE.isLoaded() || !ompD.isLoaded())
            return;

        if (ompE.hasStackerEnabled()) {
            if (ompD.hasStackerEnabled()) {
                if (ompE.hasPlayersEnabled()) {
                    Entity vehicle = pD;
                    while (vehicle.getPassenger() != null) {
                        vehicle = vehicle.getPassenger();
                    }

                    if (vehicle.getEntityId() == pE.getEntityId())
                        return;

                    vehicle.setPassenger(pE);

                    if (vehicle instanceof Player)
                        api.getNms().entity().mountUpdate(vehicle);

                    api.getNms().entity().mountUpdate(pE);

                    pD.sendMessage(new Message("§7Je hebt " + ompE.getName() + "§7 op je hoofd gezet!", "§7You've §6§lstacked§f " + ompE.getName() + "§7 on your Head!"));
                    pD.playEffect(pD.getLocation(), Effect.STEP_SOUND, (byte) 152);
                    pE.sendMessage(Messages.STACK_PLAYER.get(ompE, ompD.getName()));
                    event.setCancelled(true);
                } else {
                    pD.sendMessage(Messages.PLAYERS_DISABLED.get(ompD, ompE.getName()));
                }
            } else {
                pD.sendMessage(Messages.STACK_DISABLED.get(ompD));
            }
        } else {
            pD.sendMessage(Messages.STACKER_DISABLED.get(ompD, ompE.getName()));
        }
    }
}
