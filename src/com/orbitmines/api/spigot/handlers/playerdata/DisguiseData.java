package com.orbitmines.api.spigot.handlers.playerdata;

import com.orbitmines.api.Data;
import com.orbitmines.api.spigot.Mob;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.perks.Disguise;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 30-7-2017
*/
public class DisguiseData extends PlayerData {

    private List<Disguise> disguises;

    /* Not saved in database */
    private boolean disguised;
    private int disguiseBlockId;
    private Mob disguisedAs;
    private boolean disguisedBaby;
    private Entity disguise;

    public DisguiseData(OMPlayer omp) {
        super(Data.DISGUISES, omp);

        /* Load Defaults */
        disguises = new ArrayList<>();
        disguised = false;
    }

    @Override
    public void onLogin() {

    }

    @Override
    public void onLogout() {
        if (isDisguised())
            undisguise();
    }

    @Override
    public String serialize() {
        String disguisesString = null;
        if (disguises.size() != 0){
            StringBuilder stringBuilder = new StringBuilder();
            for (Disguise disguise : disguises) {
                stringBuilder.append(disguise.toString());
                stringBuilder.append("\\=");
            }
            disguisesString = stringBuilder.toString().substring(0, stringBuilder.length() -1);
        }
        return disguisesString;
    }

    @Override
    public void parse(String string) {
        String[] data = string.split("-");

        if (!data[0].equals("null")) {
            for (String disguise : data[0].split("=")) {
                disguises.add(Disguise.valueOf(disguise));
            }
        }
    }

    public List<Disguise> getDisguises() {
        List<Disguise> list = new ArrayList<>(this.disguises);
        for (Disguise value : Disguise.values()) {
            if (value.obtainable().getVipRank() != null && omp.isEligible(value.obtainable().getVipRank()))
                list.add(value);
        }

        return list;
    }

    public void addDisguise(Disguise disguise) {
        disguises.add(disguise);

        omp.updateStats();
    }

    public boolean hasDisguise(Disguise disguise){
        return getDisguises().contains(disguise);
    }

    public boolean isDisguised() {
        return disguised;
    }

    public void setDisguised(boolean disguised) {
        this.disguised = disguised;
    }

    public int getDisguiseBlockId() {
        return disguiseBlockId;
    }

    public Mob getDisguisedAs() {
        return disguisedAs;
    }

    public boolean isDisguisedBaby() {
        return disguisedBaby;
    }

    public Entity getDisguise() {
        return disguise;
    }

    public void showPlayers() {
        showPlayers(getPlayer().getPlayer().getWorld().getPlayers());
    }

    public void showPlayers(Collection<? extends Player> players){
        for(Player player : players){
            OMPlayer omplayer = OMPlayer.getPlayer(player);
            this.omp.showPlayers(omplayer);

            DisguiseData data = omplayer.disguises();
            if(data.isDisguised()){
                if(data.getDisguisedAs() != null)
                    data.disguiseAsMob(data.getDisguisedAs(), data.getDisguise().getCustomName(), data.isDisguisedBaby(), getPlayer().getPlayer());
                else
                    data.disguiseAsBlock(data.getDisguiseBlockId(), getPlayer().getPlayer());
            }
        }
    }

    public void disguiseAsBlock(int id) {
        disguiseAsBlock(id, omp.getPlayer().getWorld().getPlayers());
    }

    public void disguiseAsBlock(int id, Player... players){
        disguiseAsBlock(id, Arrays.asList(players));
    }

    public void disguiseAsBlock(int id, Collection<? extends Player> players){
        api.getNms().entity().disguiseAsBlock(omp.getPlayer(), id, players);

        disguised = true;
        disguiseBlockId = id;
    }

    public void disguiseAsMob(Mob mob) {
        disguiseAsMob(mob, omp.getPlayer().getWorld().getPlayers());
    }

    public void disguiseAsMob(Mob mob, Player... players) {
        disguiseAsMob(mob, Arrays.asList(players));
    }

    public void disguiseAsMob(Mob mob, Collection<? extends Player> players) {
        disguiseAsMob(mob, null, false, players);
    }

    public void disguiseAsMob(Mob mob, String displayName) {
        disguiseAsMob(mob, displayName, omp.getPlayer().getWorld().getPlayers());
    }

    public void disguiseAsMob(Mob mob, String displayName, Player... players) {
        disguiseAsMob(mob, displayName, Arrays.asList(players));
    }

    public void disguiseAsMob(Mob mob, String displayName, Collection<? extends Player> players) {
        disguiseAsMob(mob, displayName, false, players);
    }

    public void disguiseAsMob(Mob mob, String displayName, boolean baby) {
        disguiseAsMob(mob, displayName, baby, omp.getPlayer().getWorld().getPlayers());
    }

    public void disguiseAsMob(Mob mob, String displayName, boolean baby, Player... players) {
        disguiseAsMob(mob, displayName, baby, Arrays.asList(players));
    }

    public void disguiseAsMob(Mob mob, String displayName, boolean baby, Collection<? extends Player> players) {
        disguise = api.getNms().entity().disguiseAsMob(omp.getPlayer(), mob, baby, displayName, players);

        disguised = true;
        disguisedAs = mob;
        disguisedBaby = baby;
    }

    public void undisguise() {
        disguised = false;

        for (OMPlayer player : OMPlayer.getPlayers()) {
            if (!player.getHiddenPlayers().contains(omp.getPlayer())) {
                player.hidePlayers(omp);
                player.showPlayers(omp);
            }
        }
    }
}
