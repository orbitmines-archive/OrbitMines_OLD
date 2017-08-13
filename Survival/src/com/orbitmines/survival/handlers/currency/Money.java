package com.orbitmines.survival.handlers.currency;

import com.orbitmines.api.spigot.Color;
import com.orbitmines.api.spigot.handlers.ItemSet;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.currency.Currency;
import com.orbitmines.survival.handlers.SurvivalPlayer;
import org.bukkit.Material;

/*
* OrbitMines - @author Fadi Shawki - 12-8-2017
*/
public class Money extends Currency {

    public Money() {
        super("$", "$", Color.GREEN, new ItemSet(Material.INK_SACK, 5));
    }

    @Override
    public void addCurrency(OMPlayer omp, int amount) {
        ((SurvivalPlayer) omp).survival().addMoney(amount);
    }

    @Override
    public void removeCurrency(OMPlayer omp, int amount) {
        ((SurvivalPlayer) omp).survival().removeMoney(amount);
    }

    @Override
    public boolean hasCurrency(OMPlayer omp, int amount) {
        return ((SurvivalPlayer) omp).survival().hasMoney(amount);
    }

    @Override
    public void msgNotEnough(OMPlayer omp, int amount) {

    }
}
