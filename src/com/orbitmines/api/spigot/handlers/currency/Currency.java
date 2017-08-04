package com.orbitmines.api.spigot.handlers.currency;

import com.orbitmines.api.spigot.Color;
import com.orbitmines.api.spigot.handlers.ItemSet;
import com.orbitmines.api.spigot.handlers.OMPlayer;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 30-7-2017
*/
public abstract class Currency {

    private static List<Currency> currencies = new ArrayList<>();

    private final String singleName;
    private final String multipleName;
    private final Color color;
    private final ItemSet item;

    public Currency(String singleName, String multipleName, Color color, ItemSet item) {
        this.singleName = singleName;
        this.multipleName = multipleName;
        this.color = color;
        this.item = item;
    }

    public abstract void addCurrency(OMPlayer omp, int amount);

    public abstract void removeCurrency(OMPlayer omp, int amount);

    public abstract boolean hasCurrency(OMPlayer omp, int amount);

    public abstract void msgNotEnough(OMPlayer omp, int amount);

    public String getSingleName() {
        return singleName;
    }

    public String getMultipleName() {
        return multipleName;
    }

    public Color color() {
        return color;
    }

    public ItemSet item() {
        return item;
    }

    public static List<Currency> getCurrencies() {
        return currencies;
    }
}
