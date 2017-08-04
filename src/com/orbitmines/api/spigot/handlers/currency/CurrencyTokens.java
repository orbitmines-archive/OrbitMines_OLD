package com.orbitmines.api.spigot.handlers.currency;

import com.orbitmines.api.spigot.Color;
import com.orbitmines.api.spigot.handlers.ItemSet;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import org.bukkit.Material;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CurrencyTokens extends Currency {

    public CurrencyTokens() {
        super("OMT", "OMT", Color.YELLOW, new ItemSet(Material.GOLD_NUGGET));
    }

    @Override
    public void addCurrency(OMPlayer omp, int amount) {
        omp.general().addTokens(amount);
    }

    @Override
    public void removeCurrency(OMPlayer omp, int amount) {
        omp.general().removeTokens(amount);
    }

    @Override
    public boolean hasCurrency(OMPlayer omp, int amount) {
        return omp.general().hasTokens(amount);
    }

    @Override
    public void msgNotEnough(OMPlayer omp, int amount) {
        omp.general().msgRequiredTokens(amount);
    }
}
