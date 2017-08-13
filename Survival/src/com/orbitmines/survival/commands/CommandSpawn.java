
package com.orbitmines.survival.commands;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.commands.Command;
import com.orbitmines.api.spigot.handlers.Cooldown;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.survival.handlers.Spawn;
import com.orbitmines.survival.handlers.SurvivalPlayer;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandSpawn extends Command {

    private final String[] alias = {"/spawn"};

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void dispatch(OMPlayer player, String[] a) {
        SurvivalPlayer omp = (SurvivalPlayer) player;
        omp.resetCooldown(Cooldown.TELEPORTING);
        omp.setTeleportingTo(Spawn.SPAWN);
        omp.getPlayer().sendMessage("§7" + omp.getMessage(new Message("Teleporteren naar", "Teleporting to")) + " " + Spawn.SPAWN.getColor().getChatColor() + Spawn.SPAWN.getName() + "§7...");
    }
}
