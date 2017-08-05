package com.orbitmines.api.spigot.handlers.gadget.gadgets;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.gadget.GadgetHandler;
import com.orbitmines.api.spigot.handlers.particle.Particle;
import com.orbitmines.api.spigot.perks.Gadget;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class GadgetMagmaCubeSoccer extends GadgetHandler implements Listener {

    private Map<OMPlayer, Entity> entities;

    public GadgetMagmaCubeSoccer() {
        super(Gadget.MAGMA_CUBE_SOCCER, 1);

        entities = new HashMap<>();

        api.getServer().getPluginManager().registerEvents(this, api);
    }

    @Override
    public void onRun() {
        Particle p = new Particle(org.bukkit.Particle.FLAME, null);

        for (Entity en : entities.values()) {
            p.setLocation(en.getLocation());
            p.send();
        }
    }

    @Override
    public void onLogout(OMPlayer omp) {
        clear(omp);
    }

    @Override
    public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
        Player p = omp.getPlayer();

        if (entities.containsKey(omp)) {
            entities.put(omp, spawn(p.getLocation()));

            omp.sendMessage(new Message("§cMagmaCube Voetbal§7 staat nu §a§lAAN§7. §eRechtermuisknop§7 om het uit te zetten. §eLinkermuisknop§7 om het te schieten.", "§a§lENABLED§7 your §cMagmaCube Ball§7. §eRight Click§7 to remove it, §eLeft Click§7 to shoot it."));
            p.getWorld().playEffect(p.getLocation(), Effect.STEP_SOUND, 152);
        } else {
            if (!entities.get(omp).isValid()) {
                entities.remove(omp);

                entities.put(omp, spawn(p.getLocation()));
            }
            entities.get(omp).teleport(p.getLocation());

            omp.sendMessage(new Message("§7Jouw §cMagmaCube Voetbal§7 is naar jou toe geteleporteerd!", "§7Teleported your §cMagmaCube Ball§7 to you!"));
            p.getWorld().playEffect(p.getLocation(), Effect.STEP_SOUND, 152);
        }
    }

    private Entity spawn(Location location) {
        MagmaCube mc = (MagmaCube) location.getWorld().spawnEntity(location, EntityType.MAGMA_CUBE);
        mc.setSize(1);
        mc.setRemoveWhenFarAway(false);
        mc.setCustomName("§cSoccer Ball");
        mc.setCustomNameVisible(true);

        return mc;
    }

    public void clear(OMPlayer omp) {
        if (!entities.containsKey(omp))
            return;

        entities.get(omp).remove();
        entities.remove(omp);
    }

    @EventHandler
    public void onAnimation(PlayerAnimationEvent event) {
        Player p = event.getPlayer();

        for (Entity en : p.getNearbyEntities(0.7, 0.7, 0.7)) {
            if (en instanceof MagmaCube && entities.containsValue(en))
                en.setVelocity(p.getLocation().getDirection().multiply(1.2));
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        Player p = event.getPlayer();
        OMPlayer omp = OMPlayer.getPlayer(p);

        if (!entities.containsKey(omp) || event.getRightClicked().getEntityId() != entities.get(omp).getEntityId())
            return;

        event.setCancelled(true);
        omp.updateInventory();

        clear(omp);
        omp.sendMessage(new Message("§7Je §cMagmaCube Ball§7 staat nu §c§lUIT§7.", "§c§lDISABLED§7 your §cMagmaCube Ball§7!"));
    }
}
