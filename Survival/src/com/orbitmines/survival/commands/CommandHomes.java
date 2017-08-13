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
public class CommandHomes extends Command {

    private final String[] alias = {"/homes"};

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void dispatch(OMPlayer player, String[] a) {
        SurvivalPlayer omp = (SurvivalPlayer) player;
        Player p = omp.getPlayer();

        String homes = "";
        for(Home home : omp.getHomes()){
            if(homes.equals(""))
                homes = "§6" + home.getName();
            else
                homes += "§7, §6" + home.getName();
        }

        if(!homes.equals(""))
            p.sendMessage("§7" + omp.getMessage(new Message("Jouw", "Your")) + " Homes: " + homes);
        else
            omp.sendMessage(new Message("§7Je hebt nog geen §6home§7 neergezet!", "§7You haven't set a §6home§7 yet!"));
    }
}
