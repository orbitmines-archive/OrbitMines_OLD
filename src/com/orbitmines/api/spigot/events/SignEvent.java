package com.orbitmines.api.spigot.events;

import com.orbitmines.api.StaffRank;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class SignEvent implements Listener {

    @EventHandler
    public void onChange(SignChangeEvent event) {
        OMPlayer omp = OMPlayer.getPlayer(event.getPlayer());

        if (!omp.isEligible(StaffRank.OWNER) || !omp.isOpMode())
            return;

        for (int i = 0; i < 4; i++) {
            event.setLine(i, ChatColor.translateAlternateColorCodes('&', event.getLine(i)));
        }
    }
}
