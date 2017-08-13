package com.orbitmines.survival.commands;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.commands.Command;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.survival.handlers.Home;
import com.orbitmines.survival.handlers.SurvivalPlayer;
import org.bukkit.entity.Player;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandHome extends Command {

    private final String[] alias = {"/home", "/h"};

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void dispatch(OMPlayer player, String[] a) {
        SurvivalPlayer omp = (SurvivalPlayer) player;
        Player p = omp.getPlayer();

        if (omp.getHomes().size() > 0) {
            if (a.length == 1) {
                omp.getHomes().get(0).teleport();
            } else if (a.length == 2) {
                Home home = omp.getHome(a[1]);

                if (home != null)
                    home.teleport();
                else
                    omp.sendMessage(new Message("§7Je hebt geen §6home§7 genaamd '§6" + a[1] + "§7'!", "§7You don't have a §6home§7 named '§6" + a[1] + "§7'!"));
            } else {
                p.sendMessage("§7" + omp.getMessage(new Message("Gebruik", "Use")) + " §6/" + a[0].toLowerCase() + " <name>§7.");
            }
        } else {
            omp.sendMessage(new Message("§7Je hebt nog geen §6home§7 neergezet!", "§7You haven't set a §6home§7 yet!"));
        }
    }
}
