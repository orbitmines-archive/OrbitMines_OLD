package com.orbitmines.api.spigot.enablers;

import com.orbitmines.api.Data;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.playerdata.PetData;
import com.orbitmines.api.spigot.handlers.playerdata.PlayerData;
import com.orbitmines.api.spigot.nms.entity.EntityNms;
import com.orbitmines.api.spigot.perks.Pet;
import com.orbitmines.api.spigot.runnable.OMRunnable;
import com.orbitmines.api.spigot.runnable.PlayerRunnable;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class PetEnabler extends Enabler implements Listener {

    @Override
    public void onEnable() {
        api.enableData(Data.PETS, new Data.Register() {
            @Override
            public PlayerData getData(OMPlayer omp) {
                return new PetData(omp);
            }
        });

        api.getServer().getPluginManager().registerEvents(this, api);

        EntityNms entityNms = api.getNms().entity();

        new PlayerRunnable(OMRunnable.TimeUnit.TICK, 2) {
            @Override
            public void run(OMPlayer omp) {
                PetData data = omp.pets();

                if (!data.hasPetEnabled())
                    return;

                Location location = omp.getPlayer().getLocation();

                if (location.distance(data.getPet().getLocation()) < 20)
                    entityNms.navigate((LivingEntity) data.getPet(), location, 1.2);
                else
                    data.getPet().teleport(location);
            }
        };
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!event.isCancelled() && Pet.getEntities().contains(event.getEntity()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        if (!event.isCancelled() && Pet.getEntities().contains(event.getRightClicked()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        OMPlayer omp = OMPlayer.getPlayer(event.getPlayer());
        PetData data = omp.pets();

        if (!data.hasPetEnabled())
            return;

        Pet pet = data.getPetEnabled();
        data.disablePet();

        new BukkitRunnable() {
            @Override
            public void run() {
                data.spawnPet(pet);
            }
        }.runTaskLater(api, 1);
    }
}
