package com.orbitmines.kitpvp.runnables;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.runnable.OMRunnable;
import com.orbitmines.kitpvp.KitPvP;
import com.orbitmines.kitpvp.handlers.KitPvPMap;
import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import org.bukkit.Sound;

/*
* OrbitMines - @author Fadi Shawki - 11-8-2017
*/
public class MapRunnable extends OMRunnable {

    private KitPvP kitPvP;

    public MapRunnable() {
        super(TimeUnit.SECOND, 1);

        kitPvP = KitPvP.getInstance();
    }

    @Override
    public void run() {
        KitPvPMap map = kitPvP.getCurrentMap();
        map.tickTimer();

        if (map.getSeconds() <= 10 && map.getSeconds() != 0 && map.getMinutes() == 0) {
            Message message = new Message("§7Maps gaan wisselen in §6§l" + map.getSeconds() + "§7...", "§7Switching Maps in §6§l" + map.getSeconds() + "§7...");

            for (KitPvPPlayer omp : KitPvPPlayer.getKitPvPPlayers()) {
                omp.sendMessage(message);
                omp.getPlayer().playSound(omp.getPlayer().getLocation(), Sound.UI_BUTTON_CLICK, 5, 1);
            }
        } else if (map.getSeconds() == 0 && map.getMinutes() == 0) {
            kitPvP.nextMap();

            Message message = new Message("§7§lMaps Gewisseld!", "§7§lMaps Switched!");

            for (KitPvPPlayer omp : KitPvPPlayer.getKitPvPPlayers()) {
                if (omp.isSpectator() || omp.getKitActive() != null)
                    omp.teleportToMap();

                omp.sendMessage(message);
            }
        }
    }
}
