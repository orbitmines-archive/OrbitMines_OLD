package com.orbitmines.hub.handlers;

import com.orbitmines.api.ScrollerList;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.scoreboard.ScoreboardSet;
import com.orbitmines.hub.Hub;

import java.util.Arrays;

/**
 * Created by Fadi on 10-9-2016.
 */
public class HubScoreboard extends ScoreboardSet {

    private Hub hub;

    public HubScoreboard(OMPlayer omp) {
        super(omp);

        hub = Hub.getInstance();
    }

    @Override
    public void updateTitle() {
        HubPlayer omp = (HubPlayer) getOMPlayer();
        if(!omp.hasScoreboardEnabled())
            return;

        setTitle(hub.getApi().getScoreboardTitles().get());
    }

    @Override
    public void updateScores() {
        HubPlayer omp = (HubPlayer) getOMPlayer();
        if(!omp.hasScoreboardEnabled())
            return;

        addScore(14, "");
        addScore(13, "§e§lOrbitMines Tokens");
        addScore(12, " " + omp.general().getTokens() + "  ");
        addScore(11, " ");
        addScore(10, "§b§lVIP Points");
        addScore(9, " " + omp.general().getVipPoints());
        addScore(8, "  ");
//        addScore(7, "§f§lMiniGame Coins");
//        addScore(6, " " + omp.getMiniGameCoins() + " ");
        addScore(5, "   ");
//        if(!omp.getPlayer().getWorld().getName().equals(hub.getLobby().getName()) || omp.getPlayer().getLocation().distance(hub.getMiniGameLocation()) > 16) {
        addScore(4, "§c§lRank");
        addScore(3, " " + omp.getRankString());
//        }
//        else{
//            addScore(4, "§f§lTickets");
//            if(omp.isLoaded())
//                addScore(3, " " + omp.getMiniGameTickets() + "   ");
//            else
//                addScore(3, " " + HubMessages.WORD_LOADING.get(omp) + "...   ");
//        }
        addScore(2, "    ");
        addScore(1, "§d§l" + HubMessages.WORD_ALL_PLAYERS.get(omp) + ": §f#" + hub.getPlayerCounter());
        addScore(0, "     ");
    }

    @Override
    public void updateTeams() {

    }
}
