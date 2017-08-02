package com.orbitmines.api.spigot;

import com.orbitmines.api.spigot.handlers.podium.PodiumPlayer;
import com.orbitmines.api.spigot.nms.Nms;
import org.bukkit.plugin.java.JavaPlugin;

/*
* OrbitMines - @author Fadi Shawki - 26-6-2017
*/
public class OrbitMinesApi extends JavaPlugin {

    private static OrbitMinesApi api;

    private Nms nms;

    private PodiumPlayer[] topVoters;

    @Override
    public void onEnable() {
        api = this;

        this.nms = new Nms();
        this.topVoters = new PodiumPlayer[5];
    }

    @Override
    public void onDisable() {

    }

    public static OrbitMinesApi getApi() {
        return api;
    }

    public Nms getNms() {
        return nms;
    }

    public PodiumPlayer[] getTopVoters() {
        return topVoters;
    }
}
