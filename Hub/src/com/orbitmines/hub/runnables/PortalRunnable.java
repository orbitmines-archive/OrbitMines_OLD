package com.orbitmines.hub.runnables;

import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.runnable.OMRunnable;
import com.orbitmines.api.spigot.runnable.PlayerRunnable;
import org.bukkit.entity.Player;

/*
* OrbitMines - @author Fadi Shawki - 9-8-2017
*/
public class PortalRunnable extends PlayerRunnable {

    public PortalRunnable() {
        super(OMRunnable.TimeUnit.SECOND, 1);
    }

    @Override
    public void run(OMPlayer omp) {
        Player player = omp.getPlayer();

//        for (ServerPortal portal : hub.getServerPortals()) {
//            for (Block b : portal.getBlocks()) {
//                if (!b.isEmpty())
//                    continue;
//
//                p.sendBlockChange(b.getLocation(), Material.PORTAL, (byte) (portal.getServer() != Server.SURVIVAL && portal.getServer() != Server.SKYBLOCK && portal.getServer() != Server.FOG ? 2 : 1));
//            }
//        }
    }
}
