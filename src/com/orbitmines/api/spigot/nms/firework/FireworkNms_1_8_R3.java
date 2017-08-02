package com.orbitmines.api.spigot.nms.firework;

import com.orbitmines.api.spigot.OrbitMinesApi;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftFirework;
import org.bukkit.entity.Firework;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Fadi on 11-5-2016.
 */
public class FireworkNms_1_8_R3 implements FireworkNms {

    private OrbitMinesApi api;

    public FireworkNms_1_8_R3(){
        this.api = OrbitMinesApi.getApi();
    }

    @Override
    public void explode(final Firework firework) {
        new BukkitRunnable() {
            public void run() {
                ((CraftFirework) firework).getHandle().expectedLifespan = 0;
            }
        }.runTaskLater(api, 1);
    }
}
