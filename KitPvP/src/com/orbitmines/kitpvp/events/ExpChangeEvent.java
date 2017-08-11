package com.orbitmines.kitpvp.events;

import com.orbitmines.kitpvp.KitPvP;
import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ExpChangeEvent implements Listener {

    private KitPvP kitPvP;

    public ExpChangeEvent(){
        kitPvP = KitPvP.getInstance();
    }

	@EventHandler
	public void onExpChange(PlayerExpChangeEvent event){
		Player p = event.getPlayer();
		KitPvPPlayer omp = KitPvPPlayer.getPlayer(p);

		new BukkitRunnable(){
			public void run(){
				omp.kitPvP().updateLevel();
			}
		}.runTaskLater(kitPvP, 1);
	}
}
