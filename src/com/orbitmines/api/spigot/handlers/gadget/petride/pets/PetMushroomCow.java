package com.orbitmines.api.spigot.handlers.gadget.petride.pets;


import com.orbitmines.api.spigot.handlers.Cooldown;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.firework.FireworkBuilder;
import com.orbitmines.api.spigot.handlers.gadget.petride.PetHandler;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.playerdata.PetData;
import com.orbitmines.api.spigot.nms.entity.EntityNms;
import com.orbitmines.api.spigot.perks.Pet;
import com.orbitmines.api.utils.RandomUtils;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class PetMushroomCow extends PetHandler implements Listener {

    private Cooldown COOLDOWN = new Cooldown(1000, "§c§lBaby Firework", "§c§lBaby Firework", Cooldown.Action.RIGHT_CLICK);

    private List<Entity> entities;

    public PetMushroomCow() {
        super(Pet.MUSHROOM_COW, 2);

        api.getServer().getPluginManager().registerEvents(this, api);

        entities = new ArrayList<>();

        add(2, new ItemInstance() {
            @Override
            public void onInteract(PlayerInteractEvent event, OMPlayer omp, PetData petData) {
                event.setCancelled(true);
                omp.updateInventory();

                if (entities.contains(petData.getPet()))
                    entities.remove(petData.getPet());
                else
                    entities.add(petData.getPet());

                ItemStack item = getItem(omp, petData);
                event.getItem().setType(item.getType());
                ItemMeta meta = event.getItem().getItemMeta();
                meta.setDisplayName(item.getItemMeta().getDisplayName());
                event.getItem().setItemMeta(meta);

                omp.getPlayer().playSound(omp.getPlayer().getLocation(), Sound.UI_BUTTON_CLICK, 5, 1);
            }

            @Override
            public ItemStack getItem(OMPlayer omp, PetData petData) {
                boolean enabled = entities.contains(petData.getPet());
                return new ItemBuilder(enabled ? Material.HUGE_MUSHROOM_2 : Material.HUGE_MUSHROOM_1, 1, 14, "§4§nShroom Trail§7 (" + omp.statusString(enabled) + "§7)").build();
            }
        });

        add(6, new ItemInstance() {
            @Override
            public void onInteract(PlayerInteractEvent event, OMPlayer omp, PetData petData) {
                event.setCancelled(true);
                omp.updateInventory();

                if (omp.onCooldown(COOLDOWN))
                    return;

                Player p = omp.getPlayer();
                MushroomCow cow = (MushroomCow) p.getWorld().spawnEntity(p.getLocation(), EntityType.MUSHROOM_COW);
                cow.setAge(1);
                cow.setAgeLock(true);
                cow.setRemoveWhenFarAway(false);
                cow.setVelocity(p.getLocation().getDirection().multiply(1.2).setY(2));
                api.getNms().entity().setAttribute(cow, EntityNms.Attribute.MAX_HEALTH, Double.MAX_VALUE);

                new BukkitRunnable(){
                    public void run(){
                        FireworkBuilder fw = new FireworkBuilder(cow.getLocation());

                        fw.getBuilder().withColor(Color.RED);
                        fw.getBuilder().withColor(Color.RED);
                        fw.getBuilder().withFade(Color.RED);
                        fw.getBuilder().with(FireworkEffect.Type.BALL);
                        fw.getBuilder().withFlicker();
                        fw.getBuilder().withTrail();
                        fw.build();
                        fw.explode();

                        cow.remove();
                    }
                }.runTaskLater(api, 30);

                omp.resetCooldown(COOLDOWN);
            }

            @Override
            public ItemStack getItem(OMPlayer omp, PetData petData) {
                return new ItemBuilder(Material.FIREWORK, 1, 0, "§c§nBaby Firework").build();
            }
        });
    }

    @Override
    protected void onRun() {
        for (Entity entity : entities) {
            for (int i = 0; i <= 5; i++) {
                Item iEn = entity.getWorld().dropItem(entity.getLocation(), new ItemBuilder(Material.RED_MUSHROOM, 1, 0, System.currentTimeMillis() + i + "").build());
                iEn.setVelocity(entity.getLocation().getDirection().multiply(-0.75).add(RandomUtils.randomVelocity()));

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        iEn.remove();
                    }
                }.runTaskLater(api, 60);
            }
        }
    }

    @Override
    public void onLogout(OMPlayer omp) {
        Entity pet = omp.pets().getPet();

        if (entities.contains(pet))
            entities.remove(pet);
    }
}
