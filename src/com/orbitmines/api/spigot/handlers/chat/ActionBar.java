package com.orbitmines.api.spigot.handlers.chat;

import com.orbitmines.api.spigot.OrbitMinesApi;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class ActionBar {

    private Map<Player, ActionBar> actionBars = new HashMap<>();

    private OrbitMinesApi api;
    private String message;
    private Player player;
    private int stay;

    public ActionBar(Player player, String message, int stay) {
        this.api = OrbitMinesApi.getApi();
        this.player = player;
        this.message = message;
        this.stay = stay;
    }

    public String getMessage() {
        return message;
    }

    public Player getPlayer() {
        return player;
    }

    public int getStay() {
        return stay;
    }

    public void setStay(int stay) {
        this.stay = stay;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void send() {
        api.getNms().actionBar().send(player, this);
        start();
    }

    public ActionBar copy() {
        return new ActionBar(player, message, stay);
    }

    public ActionBar copy(Player player) {
        return new ActionBar(player, message, stay);
    }

    private void start() {
        actionBars.put(player, this);

        new BukkitRunnable() {
            public void run() {
                stop();
            }
        }.runTaskLater(api, getStay());
    }

    public void stop() {
        if (actionBars.get(player) == this)
            actionBars.remove(player);
    }
}
