
package com.orbitmines.survival.commands;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.commands.Command;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.survival.handlers.SurvivalPlayer;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandSetHome extends Command {

    private final String[] alias = {"/sethome", "/seth"};

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void dispatch(OMPlayer player, String[] a) {
        SurvivalPlayer omp = (SurvivalPlayer) player;
        int homes = omp.survival().getHomesAllowed();

        if (a.length != 2) {
            omp.getPlayer().sendMessage("§7" + omp.getMessage(new Message("Gebruik", "Use")) + " §6" + a[0].toLowerCase() + " <name>§7.");
            return;
        }

        if (omp.getHomes().size() < homes) {
            boolean canCreate = true;

            for (int i = 0; i < a[1].length(); i++) {
                if (!Character.isAlphabetic(a[1].charAt(i)) && !Character.isDigit(a[1].charAt(i)))
                    canCreate = false;
            }

            if (canCreate)
                omp.setHome(a[1]);
            else
                omp.sendMessage(new Message("§7Je §6home naam§7 kan alleen maar bestaan uit §6letters§7 en §6nummers§7.", "§7Your §6home name§7 can only contain §6alphabetic§7 and §6numeric§7 characters."));
        } else {
            omp.sendMessage(new Message("§7Je hebt al de maximale hoeveelheid homes! (§6" + homes + "§7)", "§7You already reached the maximum amount of homes! (§6" + homes + "§7)"));
        }
    }
}
