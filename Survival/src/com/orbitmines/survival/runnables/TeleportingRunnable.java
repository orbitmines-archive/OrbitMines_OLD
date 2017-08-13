package com.orbitmines.survival.runnables;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.Cooldown;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.chat.Title;
import com.orbitmines.api.spigot.runnable.OMRunnable;
import com.orbitmines.api.spigot.runnable.PlayerRunnable;
import com.orbitmines.survival.handlers.SurvivalPlayer;
import com.orbitmines.survival.handlers.Teleportable;

/*
* OrbitMines - @author Fadi Shawki - 12-8-2017
*/
public class TeleportingRunnable extends PlayerRunnable {

    public TeleportingRunnable() {
        super(OMRunnable.TimeUnit.SECOND, 1);
    }

    @Override
    public void run(OMPlayer player) {
        if (!player.getCooldowns().containsKey(Cooldown.TELEPORTING))
            return;

        SurvivalPlayer omp = (SurvivalPlayer) player;
        Teleportable teleportable = omp.getTeleportingTo();
        String color = teleportable.getColor().getChatColor();
        String name = teleportable.getName();

        if (omp.onCooldown(Cooldown.TELEPORTING)) {
            int seconds = (int) ((Cooldown.TELEPORTING.getCooldown(omp) / 1000) - ((System.currentTimeMillis() - omp.getCooldown(Cooldown.TELEPORTING)) / 1000));

            Title t = new Title("", "§7" + omp.getMessage(new Message("Teleporten naar", "Teleporting to")) + " " + color + name + "§7 in " + color + seconds + "§7...", 0, 40, 0);
            t.send(omp.getPlayer());
        } else {
            omp.removeCooldown(Cooldown.TELEPORTING);

            teleportable.teleportInstantly(omp);
            omp.setTeleportingTo(null);


            Title t = new Title("", "§7" + omp.getMessage(new Message("Geteleporteerd naar", "Teleported to")) + " " + color + name + "§7!", 0, 40, 20);
            t.send(omp.getPlayer());
        }
    }
}
