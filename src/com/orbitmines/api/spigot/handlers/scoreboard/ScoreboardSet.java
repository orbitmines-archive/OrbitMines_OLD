package com.orbitmines.api.spigot.handlers.scoreboard;

import com.orbitmines.api.spigot.handlers.OMPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Fadi on 11-5-2016.
 */
public abstract class ScoreboardSet {

    protected OMPlayer omp;
    private String title;
    private Map<Integer, String> scores;
    private List<ScoreboardTeam> teams;

    public ScoreboardSet(OMPlayer omp) {
        this.omp = omp;
        this.scores = new HashMap<>();
        this.teams = new ArrayList<>();
    }

    public abstract void updateTitle();

    public abstract void updateScores();

    public abstract void updateTeams();

    public abstract boolean usePlayerRanks();

    public void clear() {
        this.title = null;
        this.scores.clear();
    }

    public OMPlayer getOMPlayer() {
        return omp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addScore(int score, String text) {
        this.scores.put(score, text);
    }

    public Map<Integer, String> getScores() {
        return scores;
    }

    public String getScore(int score) {
        return scores.get(score);
    }

    public List<ScoreboardTeam> getTeams() {
        return teams;
    }

    public void addTeam(ScoreboardTeam team) {
        this.teams.add(team);
    }
}
