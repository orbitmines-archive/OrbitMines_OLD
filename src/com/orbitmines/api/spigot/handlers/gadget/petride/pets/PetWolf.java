package com.orbitmines.api.spigot.handlers.gadget.petride.pets;


import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.Cooldown;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.gadget.petride.PetHandler;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.playerdata.PetData;
import com.orbitmines.api.spigot.perks.Pet;
import com.orbitmines.api.utils.RandomUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class PetWolf extends PetHandler implements Listener {

    private Cooldown COOLDOWN = new Cooldown(3000, "§6§lBark", "§6§lBark", Cooldown.Action.RIGHT_CLICK);

    private List<Entity> entities;

    public PetWolf() {
        super(Pet.WOLF);

        api.getServer().getPluginManager().registerEvents(this, api);

        entities = new ArrayList<>();

        add(2, new ItemInstance() {
            @Override
            public void onInteract(PlayerInteractEvent event, OMPlayer omp, PetData petData) {
                event.setCancelled(true);
                omp.updateInventory();

                if (omp.onCooldown(COOLDOWN))
                    return;

                Player p = omp.getPlayer();
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WOLF_GROWL, 10, 1);

                for (Entity en : p.getNearbyEntities(3, 3, 3)) {
                    if (en instanceof Player) {
                        Player p2 = (Player) en;
                        OMPlayer omp2 = OMPlayer.getPlayer(p2);
                        if (omp2.canReceiveVelocity())
                            p2.setVelocity(p.getLocation().getDirection().subtract(p2.getLocation().getDirection()).multiply(4));
                    }
                }

                for (int iB = 0; iB < 20; iB++) {
                    ItemStack item = new ItemStack(Material.BONE, 1);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName("Bark " + iB + System.currentTimeMillis());
                    item.setItemMeta(meta);
                    Item iEn = p.getWorld().dropItem(p.getLocation(), item);

                    entities.add(iEn);

                    iEn.setVelocity(RandomUtils.randomVelocity());

                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            iEn.remove();
                            entities.remove(iEn);
                        }
                    }.runTaskLater(api, 60);
                }

                omp.resetCooldown(COOLDOWN);
            }

            @Override
            public ItemStack getItem(OMPlayer omp, PetData petData) {
                return new ItemBuilder(Material.COOKED_BEEF, 1, 0, "§6§nBark").build();
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
                return new ItemBuilder(Material.BONE, ((Ageable) petData.getPet()).isAdult() ? 2 : 1, 0, omp.getMessage(new Message("§7§nVerander Leeftijd", "§7§nChange Age"))).build();
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
    public void onPickUp(PlayerPickupItemEvent event) {
        if (event.isCancelled() || !entities.contains(event.getItem()))
            return;

        event.setCancelled(true);
    }
}
