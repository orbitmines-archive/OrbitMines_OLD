package com.orbitmines.survival.handlers;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.scoreboard.ScoreboardSet;
import com.orbitmines.survival.Survival;

/*
* OrbitMines - @author Fadi Shawki - 12-8-2017
*/
public class SurvivalScoreboard extends ScoreboardSet {

    private Survival survival;

    public SurvivalScoreboard(OMPlayer omp) {
        super(omp);

        survival = Survival.getInstance();
    }

    @Override
    public void updateTitle() {
        setTitle(survival.getApi().getScoreboardTitles().get());
    }

    @Override
    public void updateScores() {
        SurvivalPlayer omp = (SurvivalPlayer) this.omp;

        addScore(3, " ");
        addScore(2, "§2§l" + omp.getMessage(new Message("Geld", "Money")));
        addScore(1, " " + omp.survival().getMoney() + " ");
        addScore(0, "  ");
    }

    @Override
    public void updateTeams() {

    }

    @Override
    public boolean usePlayerRanks() {
        return true;
    }
}
