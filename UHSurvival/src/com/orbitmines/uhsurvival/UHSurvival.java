package com.orbitmines.uhsurvival;

/*
* OrbitMines - @author Fadi Shawki - 2-8-2017
*/
public class UHSurvival extends JavaPlugin {

    private static UHSurvival instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    public static UHSurvival getInstance() {
        return instance;
    }
}
