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
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class PetMagmaCube extends PetHandler implements Listener {

    private List<Fireball> entities;
    private Particle particle;

    public PetMagmaCube() {
        super(Pet.MAGMA_CUBE, 1);

        api.getServer().getPluginManager().registerEvents(this, api);

        entities = new ArrayList<>();
        particle = new Particle(org.bukkit.Particle.FLAME);

        add(2, new ItemInstance() {
            @Override
            public void onInteract(PlayerInteractEvent event, OMPlayer omp, PetData petData) {
                entities.add(omp.getPlayer().launchProjectile(Fireball.class));
            }

            @Override
            public ItemStack getItem(OMPlayer omp, PetData petData) {
                return new ItemBuilder(Material.FIREBALL, 1, 0, "§6§nFireball").build();
            }
        });

        add(6, new ItemInstance() {
            @Override
            public void onInteract(PlayerInteractEvent event, OMPlayer omp, PetData petData) {
                event.setCancelled(true);
                omp.updateInventory();

                MagmaCube pet = (MagmaCube) petData.getPet();
                pet.setSize(pet.getSize() == 3 ? 0 : pet.getSize() +1);
                event.getItem().setAmount(getItem(omp, petData).getAmount());

                omp.getPlayer().playSound(omp.getPlayer().getLocation(), Sound.UI_BUTTON_CLICK, 5, 1);
            }

            @Override
            public ItemStack getItem(OMPlayer omp, PetData petData) {
                return new ItemBuilder(Material.MAGMA_CREAM, ((MagmaCube) petData.getPet()).getSize(), 0, omp.getMessage(new Message("§c§nVerander Grootte", "§c§nChange Size"))).build();
            }
        });
    }

    @Override
    protected void onRun() {
        for (Entity entity : entities) {
            particle.setLocation(entity.getLocation());
            particle.send();
        }
    }

    @EventHandler
    public void onHit(ProjectileHitEvent e) {
        Projectile projectile = e.getEntity();

        if (!(projectile instanceof Fireball) || !entities.contains(projectile))
            return;

        entities.remove(projectile);
    }

    @Override
    public void onLogout(OMPlayer omp) {

    }
}
