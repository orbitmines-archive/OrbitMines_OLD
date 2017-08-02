package com.orbitmines.api.spigot.handlers.playerdata;

import com.orbitmines.api.Data;
import com.orbitmines.api.spigot.Mob;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.perks.Disguise;
import org.bukkit.Bukkit;
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

    }

    @Override
    public String serialize() {
        return null;
    }

    @Override
    public void parse(String string) {

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
        showPlayers(Bukkit.getOnlinePlayers());
    }

    public void showPlayers(Collection<? extends Player> players){
        for(Player player : players){
            this.omp.getPlayer().showPlayer(player);

            OMPlayer omplayer = OMPlayer.getPlayer(player);
            DisguiseData data = omplayer.disguises();
            if(data.isDisguised()){
                if(data.getDisguisedAs() != null)
                    data.disguiseAsMob(data.getDisguisedAs(), isDisguisedBaby(), data.getDisguise().getCustomName(), getPlayer());
                else
                    data.disguiseAsBlock(data.getDisguiseBlockId(), getPlayer());
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
