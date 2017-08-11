package com.orbitmines.kitpvp.handlers;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.scoreboard.ScoreboardSet;
import com.orbitmines.kitpvp.KitPvP;
import com.orbitmines.kitpvp.handlers.playerdata.KitPvPData;

/**
 * Created by Fadi on 10-9-2016.
 */
public class KitPvPScoreboard extends ScoreboardSet {

    private KitPvP kitPvP;

    public KitPvPScoreboard(OMPlayer omp) {
        super(omp);

        kitPvP = KitPvP.getInstance();
    }

    @Override
    public void updateTitle() {
        setTitle(kitPvP.getApi().getScoreboardTitles().get());
    }

    @Override
    public void updateScores() {
        KitPvPPlayer omp = (KitPvPPlayer) this.omp;

        addScore(14, "");
        addScore(13, "§f§l" + omp.getMessage(new Message("Huidige", "Current")) + " Streak: §6§l" + omp.getCurrentStreak());
        addScore(12, " ");
        addScore(11, "§7§lKit");

        KitHandler kit = omp.getKitActive();
        KitPvPData data = omp.kitPvP();

        addScore(10, " " + (kit != null ? "§b§l" + kit.getType().getName() + " §aLvL " + kit.getLevel() : omp.getMessage(new Message("Kit Selecteren...", "Selecting Kit..."))));
        addScore(9, "  ");
        addScore(8, "§6§lCoins");
        addScore(7, " " + data.getCoins() + "");
        addScore(6, "   ");
        addScore(5, "§c§lKills");
        addScore(4, " " + data.getKills() + " ");
        addScore(3, "    ");
        addScore(2, "§4§lDeaths");
        addScore(1, " " + data.getDeaths() + "  ");
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
