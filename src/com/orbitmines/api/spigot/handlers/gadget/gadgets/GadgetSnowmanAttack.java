package com.orbitmines.api.spigot.handlers.gadget.gadgets;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.Cooldown;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.gadget.GadgetHandler;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.particle.Particle;
import com.orbitmines.api.spigot.nms.entity.EntityNms;
import com.orbitmines.api.spigot.perks.Gadget;
import com.orbitmines.api.utils.RandomUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class GadgetSnowmanAttack extends GadgetHandler implements Listener {

    private final Cooldown COOLDOWN = new Cooldown(180000, Gadget.SNOWMAN_ATTACK.getName(), Gadget.SNOWMAN_ATTACK.getName(), Cooldown.Action.RIGHT_CLICK);

    private Map<OMPlayer, Integer> seconds;
    private Map<OMPlayer, Item> item;

    private List<Snowman> allSnowmen;
    private List<Entity> entities;

    public GadgetSnowmanAttack() {
        super(Gadget.SNOWMAN_ATTACK, 20);

        api.getServer().getPluginManager().registerEvents(this, api);

        seconds = new HashMap<>();
        item = new HashMap<>();

        allSnowmen = new ArrayList<>();
        entities = new ArrayList<>();
    }

    @Override
    public void onRun() {
        for (OMPlayer omp : item.keySet()) {
            int seconds = this.seconds.get(omp) + 1;
            this.seconds.put(omp, seconds);

            Location location = this.item.get(omp).getLocation();

            if (seconds < 10) {
                location.getWorld().playSound(location, Sound.ENTITY_WITHER_SPAWN, 5, 1);
                Particle pa = new Particle(org.bukkit.Particle.FLAME, location);
                pa.setSize(1, 1, 1);
                pa.setAmount(30);
                pa.send();
            } else if (seconds == 10) {
                start(omp, location);
            }
        }
    }

    @Override
    public void onLogout(OMPlayer omp) {
        //TODO REMEMBER COOLDOWN
    }

    @Override
    public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
        event.setCancelled(true);
        omp.updateInventory();

        if (omp.onCooldown(COOLDOWN))
            return;

        Player p = omp.getPlayer();
        Item iEn = p.getWorld().dropItem(p.getLocation(), new ItemBuilder(Material.PUMPKIN, 1, 0, p.getName()).build());
        iEn.setVelocity(p.getLocation().getDirection().multiply(0.5));

        seconds.put(omp, 0);
        item.put(omp, iEn);

        for (OMPlayer player : OMPlayer.getPlayers()) {
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_WITHER_SPAWN, 5, 1);
            player.sendMessage(new Message(omp.getName() + "§7 heeft een §6§lSnowman Attack§7 gespawned!", omp.getName() + "§7 summoned a §6§lSnowman Attack§7!"));
        }

        omp.resetCooldown(COOLDOWN);
    }

    private void start(OMPlayer omp, Location location) {
        item.get(omp).remove();
        item.remove(omp);
        seconds.remove(omp);

        location.getWorld().playSound(location, Sound.ENTITY_WITHER_DEATH, 5, 1);

        Snowman[] snowmen = new Snowman[3];
        for (int i = 0; i < 3; i++) {
            snowmen[i] = (Snowman) location.getWorld().spawnEntity(location, EntityType.SNOWMAN);
            allSnowmen.add(snowmen[i]);

            if (i != 0)
                snowmen[i - 1].addPassenger(snowmen[i]);
            else
                api.getNms().entity().setAttribute(snowmen[i], EntityNms.Attribute.MOVEMENT_SPEED, api.getNms().entity().getAttribute(snowmen[i], EntityNms.Attribute.MOVEMENT_SPEED) * 2);
        }

        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                List<Player> players = location.getWorld().getPlayers();
                if (players.size() == 0)
                    return;

                for (Entity entity : snowmen) {
                    Player player = players.get(RandomUtils.RANDOM.nextInt(players.size()));

                    Snowball s = ((Snowman) entity).launchProjectile(Snowball.class, player.getLocation().toVector().subtract(entity.getLocation().toVector()).multiply(0.15));
                    entity.getWorld().playSound(entity.getLocation(), Sound.BLOCK_SNOW_BREAK, 2, 1);
                    entities.add(s);
                }
            }
        }.runTaskTimer(api, 0, 1);

        new BukkitRunnable() {
            @Override
            public void run() {
                task.cancel();

                Location l = snowmen[1].getLocation();
                l.getWorld().playSound(l, Sound.ENTITY_WITHER_DEATH, 5, 1);

                Particle pa = new Particle(org.bukkit.Particle.FLAME, l);
                pa.setSize(2, 2, 2);
                pa.setAmount(30);
                pa.send();

                for(Snowman en : snowmen){
                    en.remove();
                    allSnowmen.remove(en);
                }
            }
        }.runTaskLater(api, 20 * 20);
    }


    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Entity ball = event.getEntity();

        if (!(ball instanceof Snowball) || !entities.contains(ball))
            return;

        entities.remove(ball);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Snowball))
            return;

        Entity ent = e.getDamager();
        if (!entities.contains(ent))
            return;

        entities.remove(ent);

        for (Entity en : ent.getNearbyEntities(0.5, 0.5, 0.5)) {
            if (!(en instanceof Player))
                continue;

            Player p = (Player) en;
            OMPlayer omp = OMPlayer.getPlayer(p);

            if (omp.canReceiveVelocity()) {
                p.playSound(p.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 5, 1);
                p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 300, 0));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 300, 4));
            }
        }
    }

    @EventHandler
    public void onForm(EntityBlockFormEvent e) {
        if (e.getEntity() instanceof Snowman && allSnowmen.contains(e.getEntity())) {
            new BukkitRunnable() {
                public void run() {
                    if (e.getBlock().getState().getType() == Material.SNOW)
                        e.getBlock().setType(Material.AIR);
                }
            }.runTaskLater(api, 100);
        }
    }
}
