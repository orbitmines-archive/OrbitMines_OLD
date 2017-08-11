package com.orbitmines.kitpvp.runnables;

import com.orbitmines.api.spigot.handlers.ItemSet;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.runnable.OMRunnable;
import com.orbitmines.api.spigot.runnable.PlayerRunnable;
import com.orbitmines.api.spigot.utils.PlayerUtils;
import com.orbitmines.kitpvp.handlers.KitHandler;
import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import com.orbitmines.kitpvp.handlers.kits.Shooter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/*
* OrbitMines - @author Fadi Shawki - 11-8-2017
*/
public class ArrowRunnable extends PlayerRunnable {

    public ArrowRunnable() {
        super(OMRunnable.TimeUnit.SECOND, 1);
    }

    @Override
    public void run(OMPlayer player) {
        KitPvPPlayer omp = (KitPvPPlayer) player;

        KitHandler handler = omp.getKitActive();
        if (handler == null || !(handler instanceof Shooter))
            return;

        Shooter shooter = (Shooter) handler;

        Player p = omp.getPlayer();
        int arrows = PlayerUtils.getAmount(p, new ItemSet(Material.ARROW));

        if(arrows != shooter.getMaxProjectiles()) {
            if (omp.getArrowSeconds() == -1)
                omp.setArrowSeconds(shooter.getReceiveTime().getAmount());

            omp.tickArrowTimer();
            if (omp.getArrowSeconds() == 0) {
                p.getInventory().addItem(new ItemBuilder(Material.ARROW, 1, 0, handler.name() + "§bArrow").build());

                omp.setArrowSeconds(shooter.getReceiveTime().getAmount());
            }
        }
    }
}
