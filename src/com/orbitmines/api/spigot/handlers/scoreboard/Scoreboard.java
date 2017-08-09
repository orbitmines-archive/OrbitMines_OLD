package com.orbitmines.api.spigot.handlers.scoreboard;

import com.orbitmines.api.StaffRank;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Team;

/**
 * Created by Fadi on 11-5-2016.
 */
public class Scoreboard {

    private String serverName;

    private Board board;
    private ScoreboardSet set;
    private OMPlayer omp;

    public Scoreboard(OMPlayer omp) {
        this.board = new Board(omp.getPlayer().getName());
        this.omp = omp;

        serverName = OrbitMinesApi.getApi().server().getServerType().toString();
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

        if (!set.usePlayerRanks())
            return;

        Team iron = getTeam(board.getScoreboard(), "I_" + serverName, VipRank.IRON.getScoreboardPrefix());
        Team gold = getTeam(board.getScoreboard(), "G_" + serverName, VipRank.GOLD.getScoreboardPrefix());
        Team diamond = getTeam(board.getScoreboard(), "D_" + serverName, VipRank.DIAMOND.getScoreboardPrefix());
        Team emerald = getTeam(board.getScoreboard(), "E_" + serverName, VipRank.EMERALD.getScoreboardPrefix());
        Team builder = getTeam(board.getScoreboard(), "B_" + serverName, StaffRank.BUILDER.getScoreboardPrefix());
        Team moderator = getTeam(board.getScoreboard(), "M_" + serverName, StaffRank.MODERATOR.getScoreboardPrefix());
        Team owner = getTeam(board.getScoreboard(), "O_" + serverName, StaffRank.OWNER.getScoreboardPrefix());

        for (OMPlayer omp : OMPlayer.getPlayers()) {
            Player player = omp.getPlayer();

            switch (omp.getStaffRank()) {

                case NONE:
                    switch (omp.getVipRank()) {

                        case IRON:
                            iron.addEntry(player.getName());
                            break;
                        case GOLD:
                            gold.addEntry(player.getName());
                            break;
                        case DIAMOND:
                            diamond.addEntry(player.getName());
                            break;
                        case EMERALD:
                            emerald.addEntry(player.getName());
                            break;
                    }
                    break;
                case BUILDER:
                    builder.addEntry(player.getName());
                    break;
                case MODERATOR:
                    moderator.addEntry(player.getName());
                    break;
                case OWNER:
                    owner.addEntry(player.getName());
                    break;
            }
        }
    }

    private Team getTeam(org.bukkit.scoreboard.Scoreboard scoreboard, String name, String prefix) {
        Team team = scoreboard.getTeam(name);
        if (team != null)
            return team;

        team = scoreboard.registerNewTeam(name);
        team.setPrefix(prefix);

        return team;
    }
}
