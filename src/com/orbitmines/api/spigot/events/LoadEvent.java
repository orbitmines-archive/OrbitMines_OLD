package com.orbitmines.api.spigot.events;

import com.orbitmines.api.spigot.OrbitMinesApi;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

/*
* OrbitMines - @author Fadi Shawki - 8-8-2017
*/
public class LoadEvent implements Listener {

    public LoadEvent() {
        for (World world : Bukkit.getWorlds()) {
            hideAdvancementsFor(world);
        }
    }

    private void hideAdvancementsFor(World world) {
        world.setGameRuleValue("announceAdvancements", "false");
        OrbitMinesApi.getApi().getLogger().info("Achievements are now hidden for world '" + world.getName() + "'.");
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        hideAdvancementsFor(event.getWorld());
    }
}
