package com.orbitmines.api.spigot.handlers.gadget.gadgets;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.gadget.GadgetHandler;
import com.orbitmines.api.spigot.handlers.playerdata.GadgetData;
import com.orbitmines.api.spigot.perks.Gadget;
import org.bukkit.Effect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

/*
* OrbitMines - @author Fadi Shawki - 30-7-2017
*/
public class GadgetStacker extends GadgetHandler implements Listener {

    public GadgetStacker() {
        super(Gadget.STACKER);

        api.getServer().getPluginManager().registerEvents(this, api);
    }

    @Override
    public void onRun() {

    }

    @Override
    public void onLogout(OMPlayer omp) {

    }

    @Override
    public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
        event.setCancelled(true);
        omp.updateInventory();
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player))
            return;

        Player pE = (Player) event.getEntity();
        Player pD = (Player) event.getDamager();
        OMPlayer ompD = OMPlayer.getPlayer(pD);
        OMPlayer ompE = OMPlayer.getPlayer(pE);

        if (!ompE.canReceiveVelocity() || !ompD.canReceiveVelocity())
            return;

        GadgetData dataD = ompD.gadgets();

        if (dataD.getGadgetEnabled() != getGadget() || pD.getInventory().getHeldItemSlot() != api.gadgets().getGadgetSlot())
            return;

        GadgetData dataE = ompE.gadgets();

        if (dataE.hasStackerEnabled()) {
            if (dataD.hasStackerEnabled()) {
                if (!ompE.getHiddenPlayers().contains(pD)) {
                    Entity vehicle = pD;
                    while (vehicle.getPassengers().size() != 0) {
                        vehicle = vehicle.getPassengers().get(0);
                    }

                    if (vehicle.getEntityId() == pE.getEntityId())
                        return;

                    vehicle.addPassenger(pE);

                    if (vehicle instanceof Player)
                        api.getNms().entity().mountUpdate(vehicle);

                    api.getNms().entity().mountUpdate(pE);

                    ompD.sendMessage(new Message("§7Je hebt " + ompE.getName() + "§7 op je hoofd gezet!", "§7You've §6§lstacked§f " + ompE.getName() + "§7 on your Head!"));
                    pD.getWorld().playEffect(pD.getLocation(), Effect.STEP_SOUND, 152);
                    ompE.sendMessage(new Message("§7" + ompD.getName() + " §7heeft je op zijn/haar hoofd gezet!", "§7" + ompD.getName() + " §6§lstacked§7 you on their Head!"));
                    event.setCancelled(true);
                } else {
                    ompD.sendMessage(new Message(ompE.getName() + "§7heeft §3§lSpelers§7 §c§lUIT§7 staan.", ompE.getName() + "§7 has §c§lDISABLED §3§lPlayers§7!"));
                }
            } else {
                ompD.sendMessage(new Message(ompE.getName() + "§7 heeft §6§lStacking§7 §c§lUIT§7 staan.", ompE.getName() + "§7 has §c§lDISABLED §6§lStacking§7!"));
            }
        } else {
            ompD.sendMessage(new Message("§7Je hebt §6§lStacking§7 §c§lUIT§7 staan! Zet het aan in je §c§nInstellingen§7!", "§7You §c§lDISABLED§6§l stacking§7! Enable it in your §c§nSettings§7!"));
        }
    }

    @EventHandler
    public void onDismount(EntityDismountEvent e) {
        if (e.getDismounted() instanceof Player && e.getEntity() instanceof Player) {
            api.getNms().entity().mountUpdate(e.getDismounted());
            api.getNms().entity().mountUpdate(e.getEntity());
        }
    }
}
