package com.orbitmines.kitpvp.runnables;

import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.runnable.OMRunnable;
import com.orbitmines.api.spigot.runnable.PlayerRunnable;
import com.orbitmines.kitpvp.KitPvP;
import com.orbitmines.kitpvp.handlers.KitPvPMap;
import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/*
* OrbitMines - @author Fadi Shawki - 11-8-2017
*/
public class LobbyRunnable extends PlayerRunnable {

    private KitPvP kitPvP;

    public LobbyRunnable() {
        super(OMRunnable.TimeUnit.SECOND, 1);

        kitPvP = KitPvP.getInstance();
    }

    @Override
    public void run(OMPlayer player) {
        KitPvPPlayer omp = (KitPvPPlayer) player;

        if (omp.isSpectator() || omp.getKitActive() != null)
            return;

        Player p = omp.getPlayer();
        if (p.getLocation().getY() <= 50 || p.getLocation().distance(kitPvP.getSpawnLocation()) >= 50)
            p.teleport(kitPvP.getSpawnLocation());

        Location location = new Location(kitPvP.getLobby(), -21, 77, 0);
        if (p.getLocation().distance(location) > 16)
            return;

        for (KitPvPMap map : KitPvPMap.getMaps()) {
            map.updateVoteSign(omp);
        }

        String[] lines = {"", "§lMap Reset", kitPvP.getCurrentMap().getMinutes() + "m" + kitPvP.getCurrentMap().getSeconds() + "s", ""};
        omp.getPlayer().sendSignChange(location, lines);
    }
}
