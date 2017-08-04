package com.orbitmines.api.spigot.handlers.currency;

import com.orbitmines.api.spigot.Color;
import com.orbitmines.api.spigot.handlers.ItemSet;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import org.bukkit.Material;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CurrencyVipPoints extends Currency {

    public CurrencyVipPoints() {
        super("VIP Point", "VIP Points", Color.AQUA, new ItemSet(Material.DIAMOND));
    }

    @Override
    public void addCurrency(OMPlayer omp, int amount) {
        omp.general().addVipPoints(amount);
    }

    @Override
    public void removeCurrency(OMPlayer omp, int amount) {
        omp.general().removeVipPoints(amount);
    }

    @Override
    public boolean hasCurrency(OMPlayer omp, int amount) {
        return omp.general().hasVipPoints(amount);
    }

    @Override
    public void msgNotEnough(OMPlayer omp, int amount) {
        omp.general().msgRequiredVipPoints(amount);
    }
}
