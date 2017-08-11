package com.orbitmines.kitpvp.handlers.playerdata;

import com.orbitmines.api.Data;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.playerdata.PlayerData;
import com.orbitmines.kitpvp.Mastery;

/*
* OrbitMines - @author Fadi Shawki - 11-8-2017
*/
@Deprecated
public class MasteriesData extends PlayerData {

    private int points;
    private int melee;
    private int meleeProtection;
    private int range;
    private int rangeProtection;

    public MasteriesData(OMPlayer omp) {
        super(Data.MASTERIES, omp);
    }

    @Override
    public void onLogin() {

    }

    @Override
    public void onLogout() {

    }

    @Override
    public String serialize() {
        return points + "-" + melee + "-" + meleeProtection + "-" + range + "-" + rangeProtection;
    }

    @Override
    public void parse(String string) {
        String[] data = string.split("-");

        points = Integer.parseInt(data[0]);
        melee = Integer.parseInt(data[1]);
        meleeProtection = Integer.parseInt(data[2]);
        range = Integer.parseInt(data[3]);
        rangeProtection = Integer.parseInt(data[4]);
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public int getMelee() {
        return melee;
    }

    public void setMelee(int melee) {
        this.melee = melee;
    }

    public int getMeleeProtection() {
        return meleeProtection;
    }

    public void setMeleeProtection(int meleeProtection) {
        this.meleeProtection = meleeProtection;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getRangeProtection() {
        return rangeProtection;
    }

    public void setRangeProtection(int rangeProtection) {
        this.rangeProtection = rangeProtection;
    }

    public int getMasteryLevel(Mastery mastery) {
        switch (mastery) {
            case MELEE:
                return melee;
            case MELEE_PROTECTION:
                return meleeProtection;
            case RANGE:
                return range;
            case RANGE_PROTECTION:
                return rangeProtection;
            default:
                return 0;
        }
    }

    public double getMasteryEffect(Mastery mastery) {
        switch (mastery) {
            case MELEE:
                return melee * 0.02;
            case MELEE_PROTECTION:
                return -meleeProtection * 0.02;
            case RANGE:
                return range * 0.04;
            case RANGE_PROTECTION:
                return -rangeProtection * 0.03;
            default:
                return 0.0;
        }
    }
}
