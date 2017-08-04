package com.orbitmines.api.spigot;

import com.orbitmines.api.Database;
import com.orbitmines.api.spigot.handlers.ConfigHandler;
import com.orbitmines.api.spigot.handlers.currency.Currency;
import com.orbitmines.api.spigot.handlers.currency.CurrencyTokens;
import com.orbitmines.api.spigot.handlers.currency.CurrencyVipPoints;
import com.orbitmines.api.spigot.handlers.podium.PodiumPlayer;
import com.orbitmines.api.spigot.nms.Nms;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 26-6-2017
*/
public class OrbitMinesApi extends JavaPlugin {

    private static OrbitMinesApi api;

    public static Currency VIP_POINTS = new CurrencyVipPoints();
    public static Currency TOKENS = new CurrencyTokens();

    private ConfigHandler configHandler;

    private Nms nms;
    private OrbitMinesServer server;

    private PodiumPlayer[] topVoters;

    private List<Chunk> chunks;

    @Override
    public void onEnable() {
        api = this;

        configHandler = new ConfigHandler(this);
        configHandler.setup("settings");

        nms = new Nms();

        topVoters = new PodiumPlayer[5];

        chunks = new ArrayList<>();

        new Database(configHandler.get("settings").getString("host"), 3306, "OrbitMines", configHandler.get("settings").getString("user"), configHandler.get("settings").getString("password"));
    }

    @Override
    public void onDisable() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.kickPlayer("§6§lOrbitMines§4§lNetwork\n    §7Restarting " + server().getServerType().getName() + "§7 Server...");
        }
    }

    public static OrbitMinesApi getApi() {
        return api;
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    public Nms getNms() {
        return nms;
    }

    public OrbitMinesServer server() {
        if (server == null)
            throw new NullPointerException("OrbitMinesServer is not setup correctly!");

        return server;
    }

    public void setup(OrbitMinesServer server) {
        this.server = server;
    }

    public PodiumPlayer[] getTopVoters() {
        return topVoters;
    }

    public List<Chunk> getChunks() {
        return chunks;
    }
}
