package com.orbitmines.api.spigot.handlers.gadget.petride.pets;


import com.orbitmines.api.Message;
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
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class PetSquid extends PetHandler implements Listener {

    private Cooldown COOLDOWN = new Cooldown(3000, "§8§lInk Bomb", "§8§lInk Bomb", Cooldown.Action.RIGHT_CLICK);

    private Map<Entity, Integer> entities;
    private List<Entity> fallingBlocks;

    public PetSquid() {
        super(Pet.SQUID, 1);

        api.getServer().getPluginManager().registerEvents(this, api);

        entities = new HashMap<>();
        fallingBlocks = new ArrayList<>();

        add(2, new ItemInstance() {
            @Override
            public void onInteract(PlayerInteractEvent event, OMPlayer omp, PetData petData) {
                event.setCancelled(true);
                omp.updateInventory();

                if (omp.onCooldown(COOLDOWN))
                    return;

                Player p = omp.getPlayer();

                ItemStack itemStack = getItem(omp, petData);
                ItemMeta meta = itemStack.getItemMeta();
                meta.setDisplayName(p.getName() + System.currentTimeMillis());
                itemStack.setItemMeta(meta);

                Item itemEn = p.getWorld().dropItem(p.getLocation(), itemStack);
                itemEn.setVelocity(p.getLocation().getDirection().multiply(1.3));
                itemEn.setPickupDelay(Integer.MAX_VALUE);

                entities.put(itemEn, 10 * 3);

                omp.resetCooldown(COOLDOWN);
            }

            @Override
            public ItemStack getItem(OMPlayer omp, PetData petData) {
                return new ItemBuilder(Material.INK_SACK, 1, 0, "§8§nInk " + omp.getMessage(new Message("Bom", "Bomb"))).build();
            }
        });

        add(6, new ItemInstance() {
            @Override
            public void onInteract(PlayerInteractEvent event, OMPlayer omp, PetData petData) {
                event.setCancelled(true);
                omp.updateInventory();

                Player p = omp.getPlayer();
                FallingBlock block = p.getWorld().spawnFallingBlock(new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() +1, p.getLocation().getZ()), new MaterialData(Material.STAINED_GLASS, (byte) 11));
                block.setVelocity(p.getLocation().getDirection().multiply(1.1));
                block.setDropItem(false);

                fallingBlocks.add(block);

                p.getWorld().playSound(p.getLocation(), Sound.BLOCK_WATER_AMBIENT, 6, 1);
            }

            @Override
            public ItemStack getItem(OMPlayer omp, PetData petData) {
                return new ItemBuilder(Material.WATER_BUCKET, 1, 0, "§9§nWater Spout").build();
            }
        });
    }

    @Override
    protected void onRun() {
        for (Entity en : entities.keySet()) {
            Particle p = new Particle(org.bukkit.Particle.SMOKE_LARGE, en.getLocation());
            p.send();

            entities.put(en, entities.get(en) - 1);

            if (entities.get(en) != 0)
                continue;

            Location l1 = en.getLocation();
            Location l2 = new Location(l1.getWorld(), l1.getX() + 1, l1.getY(), l1.getZ() + 0);
            Location l3 = new Location(l1.getWorld(), l1.getX() + 0, l1.getY(), l1.getZ() + 1);
            Location l4 = new Location(l1.getWorld(), l1.getX() - 1, l1.getY(), l1.getZ() - 0);
            Location l5 = new Location(l1.getWorld(), l1.getX() - 0, l1.getY(), l1.getZ() - 1);

            Particle p2 = new Particle(org.bukkit.Particle.LAVA);
            p2.setAmount(3);

            for (Location location : Arrays.asList(l1, l2, l3, l4, l5)) {
                p2.setLocation(location);
                p2.send();
            }

            l1.getWorld().playSound(l1, Sound.ENTITY_GENERIC_EXPLODE, 10, 1);

            for (Entity ens : en.getNearbyEntities(3, 3, 3)) {
                if (ens instanceof Player) {
                    Player player = (Player) ens;
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 0));
                }
            }

            en.remove();
            entities.remove(en);
        }
    }

    @Override
    public void onLogout(OMPlayer omp) {

    }

    @EventHandler
    public void onChange(EntityChangeBlockEvent e) {
        if (!(e.getEntity() instanceof FallingBlock))
            return;

        FallingBlock b = (FallingBlock) e.getEntity();

        if (fallingBlocks.contains(b)) {
            e.setCancelled(true);

            fallingBlocks.remove(b);
        }
    }
}
