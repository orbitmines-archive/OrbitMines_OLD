package com.orbitmines.hub.handlers.playerdata;

import com.orbitmines.api.Data;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.playerdata.PlayerData;
import com.orbitmines.hub.handlers.HubPlayer;
import com.orbitmines.hub.handlers.HubScoreboard;

/*
* OrbitMines - @author Fadi Shawki - 30-7-2017
*/
public class HubData extends PlayerData {

    private boolean scoreboardEnabled;
    private boolean playersEnabled;

    public HubData(OMPlayer omp) {
        super(Data.HUB, omp);

        /* Load Defaults */
        scoreboardEnabled = true;
        playersEnabled = true;
    }

    @Override
    public void onLogin() {
        setPlayersEnabled(playersEnabled);

        for (HubPlayer omp : HubPlayer.getHubPlayers()) {
            if (!omp.hub().hasPlayersEnabled())
                omp.hidePlayers(this.omp);
        }
    }

    @Override
    public void onLogout() {

    }

    @Override
    public String serialize() {
        return scoreboardEnabled + "";
    }

    @Override
    public void parse(String string) {
        String[] data = string.split("-");

        scoreboardEnabled = Boolean.parseBoolean(data[0]);
    }

    public boolean hasScoreboardEnabled() {
        return scoreboardEnabled;
    }

    public void setScoreboardEnabled(boolean scoreboardEnabled) {
        this.scoreboardEnabled = scoreboardEnabled;

        if (!scoreboardEnabled)
            omp.resetScoreboard();
        else
            omp.setScoreboard(new HubScoreboard(omp));
    }

    public boolean hasPlayersEnabled() {
        return playersEnabled;
    }

    public void setPlayersEnabled(boolean playersEnabled) {
        this.playersEnabled = playersEnabled;

        if (playersEnabled)
            omp.showPlayers();
        else
            omp.hidePlayers();
    }
}
