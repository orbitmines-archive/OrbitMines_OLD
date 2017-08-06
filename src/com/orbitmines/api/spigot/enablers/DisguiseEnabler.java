package com.orbitmines.api.spigot.enablers;

import com.orbitmines.api.Data;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.playerdata.DisguiseData;
import com.orbitmines.api.spigot.handlers.playerdata.PlayerData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.scheduler.BukkitRunnable;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class DisguiseEnabler extends Enabler implements Listener {

    @Override
    public void onEnable() {
        api.enableData(Data.DISGUISES, new Data.Register() {
            @Override
            public PlayerData getData(OMPlayer omp) {
                return new DisguiseData(omp);
            }
        });

        api.getServer().getPluginManager().registerEvents(this, api);
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        OMPlayer omp = OMPlayer.getPlayer(event.getPlayer());
        DisguiseData data = omp.disguises();

        if (!data.isDisguised())
            return;

        if (data.getDisguisedAs() != null) {
            new BukkitRunnable() {
                public void run() {
                    data.disguiseAsMob(data.getDisguisedAs(), omp.getName(), data.isDisguisedBaby());
                }
            }.runTaskLater(api, 1);
        } else {
            new BukkitRunnable() {
                public void run() {
                    data.disguiseAsBlock(data.getDisguiseBlockId());
                }
            }.runTaskLater(api, 1);
        }
    }
}
