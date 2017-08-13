package com.orbitmines.survival.handlers;

import com.orbitmines.api.Data;
import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.chat.Title;
import com.orbitmines.api.spigot.utils.Serializer;
import com.orbitmines.survival.Survival;
import com.orbitmines.survival.handlers.playerdata.SurvivalData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 12-8-2017
*/
public class SurvivalPlayer extends OMPlayer {

    private Survival survival;
    private static List<SurvivalPlayer> players = new ArrayList<>();

    private Location backLocation;
    private List<Home> homes;
    private List<Warp> warps;
    private List<Warp> favoriteWarps;

    private Teleportable teleportingTo;
    private List<String> tpRequests;
    private List<String> tpHereRequests;
    private List<String> invSeeRequests;

    public SurvivalPlayer(Player player) {
        super(player);

        players.add(this);
        survival = Survival.getInstance();

        this.homes = new ArrayList<>();
        this.warps = new ArrayList<>();
        this.tpRequests = new ArrayList<>();
        this.favoriteWarps = new ArrayList<>();
        this.tpHereRequests = new ArrayList<>();
        this.invSeeRequests = new ArrayList<>();
    }

    @Override
    public String getPrefix() {
        return "";
    }

    @Override
    public void onVote(int votes) {
        addClaimBlocks(100 * votes);
        survival().addMoney(25 * votes);

        Title t = new Title("§b§lVote", "§2+" + (25 * votes) + "$§7, §8+" + (100 * votes) + " Claimblocks", 20, 40, 20);
        t.send(getPlayer());
    }

    @Override
    protected void onLogin() {
        setScoreboard(new SurvivalScoreboard(this));

        FileConfiguration playerdata = survival.getConfigHandler().get("playerdata");
        if (playerdata.contains("players." + getUUID().toString() + ".FavoriteWarps")) {
            String[] warpIds = playerdata.getString("players." + getUUID().toString() + ".FavoriteWarps").split("\\|");

            for (String warpId : warpIds) {
                this.favoriteWarps.add(Warp.getWarp(Integer.parseInt(warpId)));
            }
        }

        if (playerdata.contains("players." + getUUID().toString() + ".LastLocation")) {
            this.backLocation = Serializer.parseLocation(playerdata.getString("players." + getUUID().toString() + ".LastLocation"));
        } else {
            playerdata.set("players." + getUUID().toString() + ".LastLocation", Serializer.serialize(player.getLocation()));
        }

        if (playerdata.contains("players." + getUUID().toString() + ".Homes")) {
            for (String homeName : playerdata.getConfigurationSection("players." + getUUID().toString() + ".Homes").getKeys(false)) {
                this.homes.add(new Home(this, homeName, Serializer.parseLocation(playerdata.getString("players." + getUUID().toString() + ".Homes." + homeName))));
            }
        }

        msgJoin();
        msgJoinTitle();
        broadcastJoinMessage();
    }

    @Override
    protected void onLogout() {
        broadcastQuitMessage();

        players.remove(this);
    }

    @Override
    public boolean canReceiveVelocity() {
        return true;
    }

    public Location getBackLocation() {
        return backLocation;
    }

    public boolean hasBackLocation() {
        return this.backLocation != null;
    }

    public void setBackLocation(Location backLocation) {
        this.backLocation = backLocation;

        survival.getConfigHandler().get("playerdata").set("players." + getUUID().toString() + ".LastLocation", Serializer.serialize(backLocation));
        survival.getConfigHandler().save("playerdata");
    }

    public List<Home> getHomes() {
        return homes;
    }

    public Home getHome(String homeName) {
        for (Home home : this.homes) {
            if (home.getName().equals(homeName)) {
                return home;
            }
        }
        return null;
    }

    public void removeHome(Home home) {
        this.homes.remove(home);

        survival.getConfigHandler().get("playerdata").set("players." + getUUID().toString() + ".Homes." + home.getName(), null);
        survival.getConfigHandler().save("playerdata");
    }

    public void setHome(String homeName) {
        Home home = getHome(homeName);

        survival.getConfigHandler().get("playerdata").set("players." + getUUID().toString() + ".Homes." + homeName, Serializer.serialize(player.getLocation()));
        survival.getConfigHandler().save("playerdata");

        if (home == null) {
            this.homes.add(new Home(this, homeName, getPlayer().getLocation()));
            sendMessage(new Message("§7Nieuwe home §6neergezet§7! (§6" + homeName + "§7)", "§7New home §6set§7! (§6" + homeName + "§7)"));
        } else {
            home.setLocation(getPlayer().getLocation());
            sendMessage(new Message("§7Home §6neergezet§7! (§6" + homeName + "§7)", "§7Home §6set§7! (§6" + homeName + "§7)"));
        }
    }

    public List<Warp> getWarps() {
        return warps;
    }

    public List<Warp> getFavoriteWarps() {
        return favoriteWarps;
    }

    public void setFavoriteWarps(List<Warp> favoriteWarps) {
        this.favoriteWarps = favoriteWarps;

        saveFavoriteWarps();
    }

    public void addFavoriteWarp(Warp favoriteWarp) {
        this.favoriteWarps.add(favoriteWarp);

        saveFavoriteWarps();
    }

    public void removeFavoriteWarp(Warp favoriteWarp) {
        this.favoriteWarps.remove(favoriteWarp);

        saveFavoriteWarps();
    }

    private void saveFavoriteWarps() {
        String warps = "";
        for (Warp warp : this.favoriteWarps) {
            if (warps.equals(""))
                warps = warp.getId() + "";
            else
                warps += "|" + warp.getId();
        }

        if (!warps.equals(""))
            survival.getConfigHandler().get("playerdata").set("players." + getUUID().toString() + ".FavoriteWarps", warps);
        else
            survival.getConfigHandler().get("playerdata").set("players." + getUUID().toString() + ".FavoriteWarps", null);
        survival.getConfigHandler().save("playerdata");
    }

    public Teleportable getTeleportingTo() {
        return teleportingTo;
    }

    public void setTeleportingTo(Teleportable teleportingTo) {
        this.teleportingTo = teleportingTo;
    }

    public List<String> getTpRequests() {
        return tpRequests;
    }

    public boolean hasTpRequestFrom(String name) {
        return tpRequests.contains(name);
    }

    public List<String> getTpHereRequests() {
        return tpHereRequests;
    }

    public boolean hasTpHereRequestFrom(String name) {
        return tpHereRequests.contains(name);
    }

    public List<String> getInvSeeRequests() {
        return invSeeRequests;
    }

    public boolean hasInvseeRequestFrom(String name) {
        return invSeeRequests.contains(name);
    }

    public SurvivalData survival() {
        return (SurvivalData) data(Data.SURVIVAL);
    }

    public void addClaimBlocks(int claimBlocks) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "adjustbonusclaimblocks " + player.getName() + " " + claimBlocks);
    }

    public static SurvivalPlayer getPlayer(Player player) {
        for (SurvivalPlayer omp : players) {
            if (omp.getPlayer() == player)
                return omp;
        }
        return null;
    }

    public static List<SurvivalPlayer> getSurvivalPlayers() {
        return players;
    }
}
