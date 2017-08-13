package com.orbitmines.survival.handlers.playerdata;

import com.orbitmines.api.Data;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.chat.Title;
import com.orbitmines.api.spigot.handlers.playerdata.PlayerData;

/*
* OrbitMines - @author Fadi Shawki - 12-8-2017
*/
public class SurvivalData extends PlayerData {

    private int money;
    private int extraHomes;
    private int extraWarps;

    public SurvivalData(OMPlayer omp) {
        super(Data.SURVIVAL, omp);

        this.money = 50;
        this.extraHomes = 0;
        this.extraWarps = 0;
    }

    @Override
    public void onLogin() {

    }

    @Override
    public void onLogout() {

    }

    @Override
    public String serialize() {
        return money + "-" + extraHomes + "-" + extraWarps;
    }

    @Override
    public void parse(String string) {
        String[] data = string.split("-");

        money = Integer.parseInt(data[0]);
        extraHomes = Integer.parseInt(data[1]);
        extraWarps = Integer.parseInt(data[2]);
    }

    public int getMoney() {
        return money;
    }

    public boolean hasMoney(int money) {
        return this.money >= money;
    }

    public void addMoney(int money) {
        this.money += money;

        Title t = new Title("", "§2+" + money + "$", 20, 40, 20);
        t.send(omp.getPlayer());

        omp.updateStats();
    }

    public void removeMoney(int money) {
        this.money -= money;

        omp.updateStats();
    }

    public int getExtraHomes() {
        return extraHomes;
    }

    public void setExtraHomes(int extraHomes) {
        this.extraHomes = extraHomes;

        omp.updateStats();
    }

    public int getExtraWarps() {
        return extraWarps;
    }

    public void setExtraWarps(int extraWarps) {
        this.extraWarps = extraWarps;

        omp.updateStats();
    }

    public int getWarpsAllowed() {
        if (omp.isEligible(VipRank.EMERALD))
            return 1 + this.extraWarps;
        else
            return this.extraWarps;
    }

    public int getHomesAllowed() {
        if (omp.isEligible(VipRank.EMERALD))
            return 100 + this.extraHomes;
        else if (omp.isEligible(VipRank.DIAMOND))
            return 50 + this.extraHomes;
        else if (omp.isEligible(VipRank.GOLD))
            return 25 + this.extraHomes;
        else if (omp.isEligible(VipRank.IRON))
            return 10 + this.extraHomes;
        else
            return 3 + this.extraHomes;
    }
}
