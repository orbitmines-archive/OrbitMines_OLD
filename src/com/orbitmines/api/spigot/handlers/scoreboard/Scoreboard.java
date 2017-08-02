package com.orbitmines.api.spigot.handlers.scoreboard;

import com.orbitmines.api.spigot.handlers.OMPlayer;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Team;

/**
 * Created by Fadi on 11-5-2016.
 */
public class Scoreboard {

    private Board board;
    private ScoreboardSet set;
    private OMPlayer omp;

    public Scoreboard(OMPlayer omp) {
        omp.setScoreboard(this);
        this.board = new Board(omp.getPlayer().getName());
        this.omp = omp;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public ScoreboardSet getSet() {
        return set;
    }

    public OMPlayer getOMPlayer() {
        return omp;
    }

    public void set(ScoreboardSet set) {
        if (set != null) {
            if (this.set != null) {
                for (int score : this.set.getScores().keySet()) {
                    if (!set.getScores().containsKey(score))
                        board.remove(score, this.set.getScore(score));
                }
            }

            board.setTitle(set.getTitle());

            for (int score : set.getScores().keySet()) {
                board.add(set.getScore(score), score);
            }

            board.update();
            board.send(omp.getPlayer());

            if (this.set != null)
                updateTeams();
        } else {
            this.board = new Board(omp.getPlayer().getName());
            omp.getPlayer().getScoreboard().clearSlot(DisplaySlot.SIDEBAR);

            for (Team team : omp.getPlayer().getScoreboard().getTeams()) {
                for (String entry : team.getEntries()) {
                    team.removeEntry(entry);
                }
            }
        }

        this.set = set;
    }

    public void clear() {
        this.board = new Board(omp.getPlayer().getName());
        set(set);
    }

    public void update() {
        if (set == null)
            return;

        set.clear();
        set.updateTitle();
        set.updateScores();
        set.updateTeams();

        set(set);
    }

    private void updateTeams() {
        for (ScoreboardTeam team : set.getTeams()) {
            team.update(board.getScoreboard());
        }

        if (getSet().useRankTeams())
            Utils.updateRankTeams(getBoard().getScoreboard());
    }
}
