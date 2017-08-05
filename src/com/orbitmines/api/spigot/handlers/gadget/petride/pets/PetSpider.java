package com.orbitmines.api.spigot.handlers.gadget.petride.pets;


import com.orbitmines.api.spigot.handlers.Cooldown;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.gadget.petride.PetHandler;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.playerdata.PetData;
import com.orbitmines.api.spigot.perks.Pet;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class PetSpider extends PetHandler implements Listener {

    private Cooldown WEBS = new Cooldown(4000, "§f§lWebs", "§f§lWebs", Cooldown.Action.RIGHT_CLICK);
    private Cooldown LAUNCHER = new Cooldown(2000, "§5§lSpider Launcher", "§5§lSpider Launcher", Cooldown.Action.RIGHT_CLICK);

    private List<Entity> entities;
    private List<Entity> fallingBlocks;

    public PetSpider() {
        super(Pet.SPIDER);

        api.getServer().getPluginManager().registerEvents(this, api);

        entities = new ArrayList<>();
        fallingBlocks = new ArrayList<>();

        add(2, new ItemInstance() {
            @Override
            public void onInteract(PlayerInteractEvent event, OMPlayer omp, PetData petData) {
                event.setCancelled(true);
                omp.updateInventory();

                if (omp.onCooldown(WEBS))
                    return;

                Player p = omp.getPlayer();
                FallingBlock block1 = p.getWorld().spawnFallingBlock(new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() +1, p.getLocation().getZ()), new MaterialData(Material.WEB, (byte) 0));
                block1.setVelocity(p.getLocation().getDirection().multiply(1.1));
                block1.setDropItem(false);

                fallingBlocks.add(block1);

                Vector velocity = block1.getVelocity();
                double speed = velocity.length();
                Vector direction = new Vector(velocity.getX() / speed, velocity.getY() / speed, velocity.getZ() / speed);
                double spray = 5D;

                for (int i = 0; i < 2; i++) {
                    FallingBlock block = p.getWorld().spawnFallingBlock(new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() +1, p.getLocation().getZ()), new MaterialData(Material.WEB, (byte) 0));

                    block.setVelocity(new Vector(direction.getX() + (Math.random() - 1.5) / spray, direction.getY() + (Math.random() - 1.5) / spray, direction.getZ() + (Math.random() - 1.5) / spray).normalize().multiply(speed));
                    block.setDropItem(false);
                }

                omp.resetCooldown(WEBS);
            }

            @Override
            public ItemStack getItem(OMPlayer omp, PetData petData) {
                return new ItemBuilder(Material.WEB, 1, 0, "§f§nWebs").build();
            }
        });

        add(6, new ItemInstance() {
            @Override
            public void onInteract(PlayerInteractEvent event, OMPlayer omp, PetData petData) {
                event.setCancelled(true);
                omp.updateInventory();

                if (omp.onCooldown(LAUNCHER))
                    return;

                Player p = omp.getPlayer();
                Spider s = (Spider) p.getWorld().spawnEntity(p.getLocation(), EntityType.SPIDER);
                s.setVelocity(p.getLocation().getDirection().multiply(1.5));
                s.setRemoveWhenFarAway(false);

                entities.add(s);

                new BukkitRunnable(){
                    public void run(){
                        s.remove();
                        entities.remove(s);
                    }
                }.runTaskLater(api, 80);

                omp.resetCooldown(LAUNCHER);
            }

            @Override
            public ItemStack getItem(OMPlayer omp, PetData petData) {
                return new ItemBuilder(Material.SPIDER_EYE, 1, 0, "§5§nSpider Launcher").build();
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
    public void onChange(EntityChangeBlockEvent e) {
        if (!(e.getEntity() instanceof FallingBlock))
            return;

        FallingBlock b = (FallingBlock) e.getEntity();

        if (fallingBlocks.contains(b)) {
            fallingBlocks.remove(b);

            new BukkitRunnable() {
                public void run() {
                    e.getBlock().setType(Material.AIR);
                }
            }.runTaskLater(api, 100);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Spider && entities.contains(e.getDamager())) {
            Player p = (Player) e.getEntity();

            p.playSound(p.getLocation(), Sound.ENTITY_SPIDER_AMBIENT, 5, 1);
            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 0));

            new BukkitRunnable() {
                public void run() {
                    p.playSound(p.getLocation(), Sound.ENTITY_SPIDER_AMBIENT, 5, 1);
                }
            }.runTaskLater(api, 1);
            new BukkitRunnable() {
                public void run() {
                    p.playSound(p.getLocation(), Sound.ENTITY_SPIDER_AMBIENT, 5, 1);
                }
            }.runTaskLater(api, 3);
            new BukkitRunnable() {
                public void run() {
                    p.playSound(p.getLocation(), Sound.ENTITY_SPIDER_AMBIENT, 5, 1);
                }
            }.runTaskLater(api, 5);
        }
    }
}
