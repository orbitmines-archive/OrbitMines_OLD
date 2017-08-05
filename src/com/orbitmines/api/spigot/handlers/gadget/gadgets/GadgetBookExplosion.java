package com.orbitmines.api.spigot.handlers.gadget.gadgets;

import com.orbitmines.api.spigot.handlers.Cooldown;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.gadget.GadgetHandler;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.perks.Gadget;
import com.orbitmines.api.spigot.utils.RandomUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class GadgetBookExplosion extends GadgetHandler implements Listener {

    private final Cooldown COOLDOWN = new Cooldown(6000, Gadget.BOOK_EXPLOSION.getName(), Gadget.BOOK_EXPLOSION.getName(), Cooldown.Action.RIGHT_CLICK);

    private List<Entity> entities;

    public GadgetBookExplosion() {
        super(Gadget.BOOK_EXPLOSION);

        entities = new ArrayList<>();

        api.getServer().getPluginManager().registerEvents(this, api);
    }

    @Override
    public void onRun() {

    }

    @Override
    public void onLogout(OMPlayer omp) {

    }

    @Override
    public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
        event.setCancelled(true);
        omp.updateInventory();

        if (!omp.canReceiveVelocity() || omp.onCooldown(COOLDOWN))
            return;

        Player p = omp.getPlayer();
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 5, 1);

        ItemBuilder itemBuilder = new ItemBuilder(Material.PAPER);
        for (int i = 1; i <= 12; i++) {
            itemBuilder.setDisplayName("Gadget-" + i + System.currentTimeMillis());

            Item paper = p.getWorld().dropItem(p.getLocation(), itemBuilder.build());
            paper.setVelocity(RandomUtils.randomVelocity());

            entities.add(paper);

            new BukkitRunnable() {
                @Override
                public void run() {
                    paper.remove();
                    entities.remove(paper);
                }
            }.runTaskLater(api, 200);

            omp.resetCooldown(COOLDOWN);
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        if (!entities.contains(event.getItem()))
            return;

        event.setCancelled(true);

        Player p = event.getPlayer();
        OMPlayer omp = OMPlayer.getPlayer(p);

        if (!omp.canReceiveVelocity())
            return;

        p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 5, 1);

        p.removePotionEffect(PotionEffectType.SPEED);
        p.removePotionEffect(PotionEffectType.JUMP);
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 5, 4));
        p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 5, 4));

        event.getItem().remove();
        entities.remove(event.getItem());
    }
}
