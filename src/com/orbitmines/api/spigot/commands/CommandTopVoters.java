package com.orbitmines.api.spigot.commands;

import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.podium.PodiumPlayer;
import org.bukkit.entity.Player;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandTopVoters extends Command {

    private final String[] alias = {"/topvoters"};

    private OrbitMinesApi api;

    public CommandTopVoters() {
        api = OrbitMinesApi.getApi();
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void dispatch(OMPlayer omp, String[] a) {
        Player p = omp.getPlayer();

        p.sendMessage("");
        p.sendMessage("§b§lTop 5 Voters§7:");

        PodiumPlayer[] voters = api.getTopVoters();
        for (int i = 0; i < voters.length; i++) {
            String color = "§8";
            if (i == 0)
                color = "§6";
            else if (i == 1)
                color = "§7";
            else if (i == 2)
                color = "§c";

            PodiumPlayer voter = voters[i];
            p.sendMessage("  " + color + "§l" + (i + 1) + ". " + color + (voter == null ? "" : voter.getPlayerName() + " §7| §b" + voter.getVotes() + " Vote" + (voter.getVotes() == 1 ? "" : "s")));
        }
    }
}
