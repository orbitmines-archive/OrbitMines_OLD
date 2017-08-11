package com.orbitmines.kitpvp.runnables;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.runnable.OMRunnable;
import com.orbitmines.kitpvp.KitPvP;
import com.orbitmines.kitpvp.handlers.ActiveBooster;

/*
* OrbitMines - @author Fadi Shawki - 11-8-2017
*/
public class BoosterRunnable extends OMRunnable {

    private KitPvP kitPvP;

    public BoosterRunnable() {
        super(TimeUnit.HOUR, 1);

        kitPvP = KitPvP.getInstance();
    }

    @Override
    public void run() {
        ActiveBooster booster = kitPvP.getBooster();
        if (booster == null)
            return;

        booster.tickTimer();

        if (booster.getSeconds() != 0)
            return;

        if (booster.getMinutes() != 0) {
            new Message("§a" + booster.getPlayer() + "'s Booster§7 (§ax" + booster.getType().getMultiplier() +"§7) verloopt over §a" + booster.getMinutes() + "m " +  booster.getSeconds() + "s§7.", "§a" + booster.getPlayer() + "'s Booster§7 (§ax" + booster.getType().getMultiplier() +"§7) remains for §a" + booster.getMinutes() + "m " +  booster.getSeconds() + "s§7.").broadcast();
        } else {
            new Message("§a" + booster.getPlayer() + "'s Booster§7 (§ax" + booster.getType().getMultiplier() + "§7) is verlopen.", "§a" + booster.getPlayer() + "'s Booster§7 (§ax" + booster.getType().getMultiplier() + "§7) has been expired.");
            kitPvP.setBooster(null);
        }
    }
}
