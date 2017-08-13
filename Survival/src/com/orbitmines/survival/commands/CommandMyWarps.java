package com.orbitmines.survival.commands;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.commands.Command;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.survival.handlers.SurvivalPlayer;
import com.orbitmines.survival.handlers.Warp;
import org.bukkit.entity.Player;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandMyWarps extends Command {

    private final String[] alias = {"/mywarps"};

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void dispatch(OMPlayer player, String[] a) {
        SurvivalPlayer omp = (SurvivalPlayer) player;
        Player p = omp.getPlayer();

        String warps = "";
        for (Warp warp : omp.getWarps()) {
            if (warps.equals(""))
                warps = "§6" + warp.getName();
            else
                warps += "§7, §6" + warp.getName();
        }

        if (!warps.equals(""))
            p.sendMessage("§7" + omp.getMessage(new Message("Jouw", "Your")) + " Warps: " + warps);
        else
            omp.sendMessage(new Message("§7Geen §3warps§7 om te laten zien. Maak er eentje met §3/setwarp§7.", "§7No §3warps§7 to display. Create one using §3/setwarp§7."));
    }
}
