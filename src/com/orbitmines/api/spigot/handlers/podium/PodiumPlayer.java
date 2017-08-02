package com.orbitmines.api.spigot.handlers.podium;

import com.orbitmines.api.Database;

import java.util.UUID;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class PodiumPlayer {

    private final UUID uuid;
    private final int votes;
    private String playerName;

    public PodiumPlayer(UUID uuid, int votes) {
        this.uuid = uuid;
        this.votes = votes;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getVotes() {
        return votes;
    }

    public String getPlayerName() {
        if (playerName == null)
            playerName = Database.get().getString(Database.Table.PLAYERS, Database.Column.NAME, new Database.Where(Database.Column.UUID, uuid.toString()));

        return playerName;
    }
}
