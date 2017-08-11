package com.orbitmines.hub.handlers;

import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.scoreboard.ScoreboardSet;
import com.orbitmines.hub.Hub;

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
        HubPlayer omp = (HubPlayer) this.omp;
        if(!omp.hub().hasScoreboardEnabled())
            return;

        setTitle(hub.getApi().getScoreboardTitles().get());
    }

    @Override
    public void updateScores() {
        HubPlayer omp = (HubPlayer) getOMPlayer();
        if(!omp.hub().hasScoreboardEnabled())
            return;

        addScore(9, "");
        addScore(8, "§e§lOrbitMines Tokens");
        addScore(7, " " + omp.general().getTokens() + "  ");
        addScore(6, " ");
        addScore(5, "§b§lVIP Points");
        addScore(4, " " + omp.general().getVipPoints());
        addScore(3, "  ");
//        addScore(7, "§f§lMiniGame Coins");
//        addScore(6, " " + omp.getMiniGameCoins() + " ");
//        addScore(4, "   ");
//        if(!omp.getPlayer().getWorld().getName().equals(hub.getLobby().getName()) || omp.getPlayer().getLocation().distance(hub.getMiniGameLocation()) > 16) {
        addScore(2, "§c§lRank");
        addScore(1, " " + omp.getRankString());
//        }
//        else{
//            addScore(4, "§f§lTickets");
//            if(omp.isLoaded())
//                addScore(3, " " + omp.getMiniGameTickets() + "   ");
//            else
//                addScore(3, " " + HubMessages.WORD_LOADING.get(omp) + "...   ");
//        }
//        addScore(1, "    ");
        addScore(0, "     ");
    }

    @Override
    public void updateTeams() {

    }

    @Override
    public boolean usePlayerRanks() {
        return true;
    }
}
