package com.orbitmines.hub.handlers;

import com.orbitmines.api.spigot.handlers.OMPlayer;
import org.bukkit.entity.Player;

/*
* OrbitMines - @author Fadi Shawki - 8-8-2017
*/
public class HubPlayer extends OMPlayer {

    public HubPlayer(Player player) {
        super(player);
    }

    @Override
    public String getPrefix() {
        return null;
    }

    @Override
    public void onVote(int votes) {
        general().addTokens(1);
    }

    @Override
    protected void onLogin() {
        clearInventory();
    }

    @Override
    protected void onLogout() {

    }

    @Override
    public boolean canReceiveVelocity() {
        return false;
    }
}
