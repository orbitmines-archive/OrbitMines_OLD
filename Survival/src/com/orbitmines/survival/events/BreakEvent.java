package com.orbitmines.survival.events;

import com.orbitmines.survival.Survival;
import com.orbitmines.survival.handlers.SurvivalPlayer;
import com.orbitmines.survival.handlers.region.Region;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/*
* OrbitMines - @author Fadi Shawki - 12-8-2017
*/
public class BreakEvent implements Listener {

    private Survival survival;

    public BreakEvent() {
        survival = Survival.getInstance();
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player p = event.getPlayer();
        if (!p.getWorld().getName().equals(survival.getWorld().getName()))
            return;

        SurvivalPlayer omp = SurvivalPlayer.getPlayer(p);

        if (Region.isInRegion(omp, event.getBlock().getLocation())) {
            event.setCancelled(true);
            return;
        }

//        ShopSign sign = ShopSign.getShopSign(e.getBlock().getLocation());
//
//        if(sign != null && omp.getShopSigns().contains(sign)){
//            sign.delete();
//            p.sendMessage(SurvivalMessages.REMOVE_CHESTSHOP.get(omp));
//        }
    }
}
