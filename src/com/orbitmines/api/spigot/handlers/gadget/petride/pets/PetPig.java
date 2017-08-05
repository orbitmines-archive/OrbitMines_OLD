package com.orbitmines.api.spigot.handlers.gadget.petride.pets;


import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.Mob;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.gadget.petride.PetHandler;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.playerdata.PetData;
import com.orbitmines.api.spigot.nms.entity.EntityNms;
import com.orbitmines.api.spigot.perks.Pet;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class PetPig extends PetHandler {

    private List<Entity> entities;
    private Map<Entity, List<Entity>> babyEntities;

    private EntityNms nms;

    public PetPig() {
        super(Pet.PIG, 2);

        entities = new ArrayList<>();
        babyEntities = new HashMap<>();

        nms = api.getNms().entity();

        add(2, new ItemInstance() {
            @Override
            public void onInteract(PlayerInteractEvent event, OMPlayer omp, PetData petData) {
                event.setCancelled(true);
                omp.updateInventory();

                Entity pet = petData.getPet();

                if (entities.contains(pet))
                    entities.remove(pet);
                else
                    entities.add(pet);

                ItemMeta meta = event.getItem().getItemMeta();
                meta.setDisplayName(getItem(omp, petData).getItemMeta().getDisplayName());
                event.getItem().setItemMeta(meta);

                Player p = omp.getPlayer();
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 5, 1);

                if (entities.contains(pet)) {
                    List<Entity> list = new ArrayList<>();
                    for (int i = 0; i < 2; i++) {
                        Pig pig = (Pig) p.getWorld().spawnEntity(p.getLocation(), EntityType.PIG);
                        pig.setBaby();
                        pig.setAgeLock(true);
                        pig.setRemoveWhenFarAway(false);
                        list.add(pig);
                    }

                    babyEntities.put(pet, list);
                } else {
                    for (Entity en : babyEntities.get(pet)) {
                        en.remove();
                    }
                    babyEntities.remove(pet);
                }
            }

            @Override
            public ItemStack getItem(OMPlayer omp, PetData petData) {
                return api.getNms().customItem().setEggId(new ItemBuilder(Material.MONSTER_EGG, 1, 0, "§d§nBaby Pigs§7 (§a§l" + omp.statusString(entities.contains(petData.getPet())) + "§7)").build(), Mob.PIG);
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
                return new ItemBuilder(Material.PORK, ((Ageable) petData.getPet()).isAdult() ? 2 : 1, 0, omp.getMessage(new Message("§d§nVerander Leeftijd", "§d§nChange Age"))).build();
            }
        });
    }

    @Override
    protected void onRun() {
        for (Entity entity : entities) {
            LivingEntity follow = (LivingEntity) entity;

            for (Entity baby : babyEntities.get(entity)) {
                double distance = follow.getLocation().distance(baby.getLocation());

                if (distance > 1 && distance < 7)
                    nms.navigate((LivingEntity) baby, follow.getLocation(), 1.2);
                else
                    baby.teleport(follow);

                follow = (LivingEntity) baby;
            }
        }
    }

    @Override
    public void onLogout(OMPlayer omp) {
        Entity pet = omp.pets().getPet();

        if (!entities.contains(pet))
            return;

        for (Entity en : babyEntities.get(pet)) {
            en.remove();
        }

        babyEntities.remove(pet);
        entities.remove(pet);
    }
}
