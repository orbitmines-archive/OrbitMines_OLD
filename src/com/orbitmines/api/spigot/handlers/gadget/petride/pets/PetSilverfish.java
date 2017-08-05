package com.orbitmines.api.spigot.handlers.gadget.petride.pets;


import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.Mob;
import com.orbitmines.api.spigot.handlers.Cooldown;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.gadget.petride.PetHandler;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.particle.Particle;
import com.orbitmines.api.spigot.handlers.playerdata.PetData;
import com.orbitmines.api.spigot.perks.Pet;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class PetSilverfish extends PetHandler {

    private Cooldown BOMB = new Cooldown(6000, "§7§lSilverfish Bomb", "§7§lSilverfish Bomb", Cooldown.Action.RIGHT_CLICK);
    private Cooldown LEAP = new Cooldown(4000, "§8§lLeap", "§8§lLeap", Cooldown.Action.RIGHT_CLICK);

    private List<Entity> entities;
    private Particle particle;

    public PetSilverfish() {
        super(Pet.SILVERFISH, 1);

        entities = new ArrayList<>();
        particle = new Particle(org.bukkit.Particle.EXPLOSION_LARGE);
        particle.setAmount(4);

        add(2, new ItemInstance() {
            @Override
            public void onInteract(PlayerInteractEvent event, OMPlayer omp, PetData petData) {
                event.setCancelled(true);
                omp.updateInventory();

                if (omp.onCooldown(BOMB))
                    return;

                Player p = omp.getPlayer();

                ItemStack item = getItem(omp, petData);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(p.getName() + System.currentTimeMillis());
                item.setItemMeta(meta);


                Item iEn = p.getWorld().dropItem(new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() + 1, p.getLocation().getZ()), item);
                iEn.setVelocity(p.getLocation().getDirection().multiply(1.1));

                entities.add(iEn);

                omp.resetCooldown(BOMB);
            }

            @Override
            public ItemStack getItem(OMPlayer omp, PetData petData) {
                return api.getNms().customItem().setEggId(new ItemBuilder(Material.MONSTER_EGG, 1, 0, "§7§nSilverfish " + omp.getMessage(new Message("Bom", "Bomb"))).build(), Mob.SILVERFISH);
            }
        });

        add(6, new ItemInstance() {
            @Override
            public void onInteract(PlayerInteractEvent event, OMPlayer omp, PetData petData) {
                event.setCancelled(true);
                omp.updateInventory();

                if (omp.onCooldown(LEAP))
                    return;

                Player p = omp.getPlayer();
                Silverfish s = (Silverfish) petData.getPet();

                s.setVelocity(p.getLocation().getDirection().multiply(1.3).add(new Vector(0, 0.3, 0)));
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_FIREWORK_BLAST, 6, 1);

                omp.resetCooldown(LEAP);
            }

            @Override
            public ItemStack getItem(OMPlayer omp, PetData petData) {
                return new ItemBuilder(Material.STONE_HOE, 1, 0, "§8§nLeap").build();
            }
        });
    }

    @Override
    protected void onRun() {
        for (Entity en : new ArrayList<>(entities)) {
            if (!en.isOnGround())
                continue;

            Location l = en.getLocation();
            l.getWorld().playSound(l, Sound.ENTITY_GENERIC_EXPLODE, 5, 1);
            particle.setLocation(l);
            particle.send();

            Location l2 = new Location(l.getWorld(), l.getX() + 1, l.getY(), l.getZ() + 0);
            Location l3 = new Location(l.getWorld(), l.getX() + 0, l.getY(), l.getZ() + 1);
            Location l4 = new Location(l.getWorld(), l.getX() - 1, l.getY(), l.getZ() + 0);
            Location l5 = new Location(l.getWorld(), l.getX() + 0, l.getY(), l.getZ() - 1);

            for (Location lo : Arrays.asList(l, l2, l3, l4, l5)) {
                final Silverfish s = (Silverfish) en.getWorld().spawnEntity(lo, EntityType.SILVERFISH);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        s.remove();
                    }
                }.runTaskLater(api, 60);
            }

            en.remove();
            entities.remove(en);
        }
    }

    @Override
    public void onLogout(OMPlayer omp) {

    }
}
