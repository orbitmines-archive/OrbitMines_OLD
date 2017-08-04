package com.orbitmines.api.spigot.handlers.playerdata;

import com.orbitmines.api.Data;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.firework.FireworkSettings;
import com.orbitmines.api.spigot.perks.Gadget;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 30-7-2017
*/
public class GadgetData extends PlayerData {

    private List<Gadget> gadgets;

    private FireworkSettings fireworkSettings;
    private int fireworkPasses;

    private boolean stackerEnabled;

    public GadgetData(OMPlayer omp) {
        super(Data.GADGETS, omp);

        /* Load Defaults */
        stackerEnabled = true;
        gadgets = new ArrayList<>();
    }

    @Override
    public void onLogin() {

    }

    @Override
    public void onLogout() {

    }

    @Override
    public String serialize() {
        return stackerEnabled + "";
    }

    @Override
    public void parse(String string) {
        String[] data = string.split("-");

        stackerEnabled = Boolean.parseBoolean(data[0]);
    }

    public List<Gadget> getGadgets() {
        List<Gadget> list = new ArrayList<>(this.gadgets);
        for (Gadget value : Gadget.values()) {
            if (value.obtainable().getVipRank() != null && omp.isEligible(value.obtainable().getVipRank()))
                list.add(value);
        }

        return list;
    }

    public void addGadget(Gadget gadget) {
        this.gadgets.add(gadget);
    }

    public boolean hasGadget(Gadget gadget){
        return getGadgets().contains(gadget);
    }

    public FireworkSettings getFireworkSettings() {
        return fireworkSettings;
    }

    public int getFireworkPasses() {
        return fireworkPasses;
    }

    public void addFireworkPasses(int fireworkPasses){
        this.fireworkPasses += fireworkPasses;
    }

    public void removeFireworkPass(){
        this.fireworkPasses--;
    }

    public boolean hasStackerEnabled() {
        return stackerEnabled;
    }

    public void setStackerEnabled(boolean stackerEnabled) {
        this.stackerEnabled = stackerEnabled;
    }

    public Gadget getGadgetEnabled() {

        //TODO
    }
}
