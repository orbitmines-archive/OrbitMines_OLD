package com.orbitmines.survival.events;

import com.orbitmines.survival.Survival;
import com.orbitmines.survival.handlers.SurvivalPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/*
* OrbitMines - @author Fadi Shawki - 12-8-2017
*/
public class DeathEvent implements Listener {

    private Survival survival;

    public DeathEvent() {
        survival = Survival.getInstance();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();
        SurvivalPlayer omp = SurvivalPlayer.getPlayer(p);

        p.setHealth(20D);
        p.setFoodLevel(20);
        omp.setBackLocation(p.getLocation());

        new BukkitRunnable() {
            public void run() {
                p.teleport(survival.getSpawnLocation());
                p.setVelocity(new Vector(0, 0, 0));
                p.setFireTicks(0);
                omp.clearExperience();
                omp.clearPotionEffects();
            }
        }.runTaskLater(survival, 1);
    }
}
