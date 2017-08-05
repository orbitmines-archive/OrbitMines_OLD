package com.orbitmines.api.spigot.handlers.gadget.gadgets;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.Cooldown;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.gadget.GadgetHandler;
import com.orbitmines.api.spigot.handlers.particle.Particle;
import com.orbitmines.api.spigot.perks.Gadget;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class GadgetSwapTeleporter extends GadgetHandler {

    public final Cooldown COOLDOWN = new Cooldown(4000, Gadget.SWAP_TELEPORTER.getName(), Gadget.SWAP_TELEPORTER.getName(), Cooldown.Action.LEFT_CLICK);

    private Map<OMPlayer, Entity> entities;

    public GadgetSwapTeleporter() {
        super(Gadget.SWAP_TELEPORTER, 1);

        entities = new HashMap<>();
    }

    @Override
    public void onLogout(OMPlayer omp) {

    }

    @Override
    public void onRun() {
        for (OMPlayer omp : entities.keySet()) {
            Entity en = entities.get(omp);

            Particle p = new Particle(org.bukkit.Particle.SMOKE_LARGE, en.getLocation());
            p.setAmount(10);
            p.send();

            Player player = omp.getPlayer();
            for (Entity e : en.getNearbyEntities(0.5, 1, 0.5)) {
                if (!(e instanceof Player) || player == e)
                    continue;

                Player player2 = (Player) e;
                OMPlayer omp2 = OMPlayer.getPlayer(player2);

                if (omp.canReceiveVelocity() && omp2.canReceiveVelocity()) {
                    if (!omp2.getHiddenPlayers().contains(player)) {
                        Location l1 = player.getLocation();
                        Location l2 = player2.getLocation();

                        player.teleport(l2);
                        player2.teleport(l1);

                        omp.sendMessage(new Message("§7Je bent van positie §2§lgeswapt§7 met " + omp2.getName() + "§7!", "§7You've §2§lswapped§7 positions with " + omp2.getName() + "§7!"));
                        omp2.sendMessage(new Message("§7Je bent van positie §2§lgeswapt§7 met " + omp.getName() + "§7!", omp.getName() + " §2§lswapped§7 positions with you!"));

                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 5, 1);
                        player2.playSound(player2.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 5, 1);

                        entities.remove(omp);
                        en.remove();
                    } else {
                        omp.sendMessage(new Message(omp2.getName() + "§7heeft §3§lSpelers§7 §c§lUIT§7 staan.", omp2.getName() + "§7 has §c§lDISABLED §3§lPlayers§7!"));
                        entities.remove(omp);
                        en.remove();
                    }
                }
            }
        }
    }

    @Override
    public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
        event.setCancelled(true);

        if (!omp.canReceiveVelocity() || event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK || omp.onCooldown(COOLDOWN))
            return;

        Player p = omp.getPlayer();
        ItemStack item = new ItemStack(Material.EYE_OF_ENDER, 1);
        Entity en = p.getWorld().dropItem(new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() + 1, p.getLocation().getZ()), item);
        en.setVelocity(p.getLocation().getDirection().multiply(1.5));

        if (entities.containsKey(omp)) {
            entities.get(omp).remove();
            entities.remove(omp);
        }

        entities.put(omp, en);

        new BukkitRunnable() {
            public void run() {
                if (entities.containsKey(omp)) {
                    entities.remove(omp);
                    en.remove();
                }
            }
        }.runTaskLater(api, 100);

        omp.resetCooldown(COOLDOWN);
    }
}
