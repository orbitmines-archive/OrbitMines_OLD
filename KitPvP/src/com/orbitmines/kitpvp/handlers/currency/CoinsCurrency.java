package com.orbitmines.kitpvp.handlers.currency;

import com.orbitmines.api.spigot.Color;
import com.orbitmines.api.spigot.handlers.ItemSet;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.currency.Currency;
import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import org.bukkit.Material;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public class CoinsCurrency extends Currency {

    public CoinsCurrency() {
        super("Coin", "Coins", Color.ORANGE, new ItemSet(Material.GOLD_INGOT));
    }

    @Override
    public void addCurrency(OMPlayer omp, int amount) {
        ((KitPvPPlayer) omp).kitPvP().addCoins(amount);
    }

    @Override
    public void removeCurrency(OMPlayer omp, int amount) {
        ((KitPvPPlayer) omp).kitPvP().removeCoins(amount);
    }

    @Override
    public boolean hasCurrency(OMPlayer omp, int amount) {
        return ((KitPvPPlayer) omp).kitPvP().hasCoins(amount);
    }

    @Override
    public void msgNotEnough(OMPlayer omp, int amount) {
        ((KitPvPPlayer) omp).kitPvP().msgRequiredCoins(amount);
    }
}
