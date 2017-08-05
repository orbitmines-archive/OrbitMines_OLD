package com.orbitmines.api.spigot.handlers.gadget.petride.pets;


import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.gadget.petride.PetHandler;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.particle.Particle;
import com.orbitmines.api.spigot.handlers.playerdata.PetData;
import com.orbitmines.api.spigot.perks.Pet;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Egg;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class PetChicken extends PetHandler implements Listener {

    private List<Egg> entities;
    private Particle particle;

    public PetChicken() {
        super(Pet.CHICKEN);

        api.getServer().getPluginManager().registerEvents(this, api);

        entities = new ArrayList<>();

        particle = new Particle(org.bukkit.Particle.EXPLOSION_LARGE);
        particle.setAmount(5);

        add(2, new ItemInstance() {
            @Override
            public void onInteract(PlayerInteractEvent event, OMPlayer omp, PetData petData) {
                event.setCancelled(true);
                omp.updateInventory();

                entities.add(omp.getPlayer().launchProjectile(Egg.class));
            }

            @Override
            public ItemStack getItem(OMPlayer omp, PetData petData) {
                return new ItemBuilder(Material.EGG, 1, 0, "§7§nEgg " + omp.getMessage(new Message("Bom", "Bomb"))).build();
            }
        });

        add(6, new ItemInstance() {
            @Override
            public void onInteract(PlayerInteractEvent event, OMPlayer omp, PetData petData) {
                event.setCancelled(true);
                omp.updateInventory();

                Ageable pet = (Ageable) petData.getPet();

                if (pet.isAdult())
                    pet.setBaby();
                else
                    pet.setAdult();

                event.getItem().setAmount(getItem(omp, petData).getAmount());

                omp.getPlayer().playSound(omp.getPlayer().getLocation(), Sound.UI_BUTTON_CLICK, 5, 1);
            }

            @Override
            public ItemStack getItem(OMPlayer omp, PetData petData) {
                return new ItemBuilder(Material.RAW_CHICKEN, ((Ageable) petData.getPet()).isAdult() ? 2 : 1, 0, omp.getMessage(new Message("§c§nVerander Leeftijd", "§c§nChange Age"))).build();
            }
        });
    }

    @Override
    protected void onRun() {

    }

    @Override
    public void onLogout(OMPlayer omp) {

    }

    @EventHandler
    public void onThrow(PlayerEggThrowEvent event) {
        event.setHatching(false);
    }

    @EventHandler
    public void onHit(ProjectileHitEvent e) {
        if (!(e.getEntity() instanceof Egg))
            return;

        Egg egg = (Egg) e.getEntity();

        if (!entities.contains(egg))
            return;

        entities.remove(egg);

        particle.setLocation(egg.getLocation());
        particle.send();

        egg.getWorld().playSound(egg.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 5, 1);
        egg.remove();
    }
}
