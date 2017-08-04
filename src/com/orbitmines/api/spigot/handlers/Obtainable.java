package com.orbitmines.api.spigot.handlers;

import com.orbitmines.api.Message;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.spigot.handlers.currency.Currency;
import org.bukkit.Material;

/*
* OrbitMines - @author Fadi Shawki - 7/28/2017
*/
public class Obtainable {

    private final Currency currency;
    private final int price;
    private final VipRank vipRank;
    private final Message other;

    public Obtainable() {
        this(null, -1, null, null);
    }

    public Obtainable(Currency currency, int price) {
        this(currency, price, null, null);
    }

    public Obtainable(VipRank vipRank) {
        this(null, 0, vipRank, null);
    }

    public Obtainable(Message other) {
        this(null, 0, null, other);
    }

    private Obtainable(Currency currency, int price, VipRank vipRank, Message other) {
        this.currency = currency;
        this.price = price;
        this.vipRank = vipRank;
        this.other = other;
    }

    public Currency getCurrency() {
        return currency;
    }

    public int getPrice() {
        return price;
    }

    public VipRank getVipRank() {
        return vipRank;
    }

    public Message getOther() {
        return other;
    }

    public boolean isObtainable() {
        return currency != null || other != null || vipRank != null;
    }

    public boolean isPurchasable() {
        return currency != null;
    }

    public void purchase(OMPlayer omp) {
        currency.removeCurrency(omp, price);
    }

    public boolean canPurchase(OMPlayer omp) {
        return currency.hasCurrency(omp, price);
    }

    public void msgNoAccess(OMPlayer omp) {
        if (currency != null) {
            currency.msgNotEnough(omp, price);
        } else if (vipRank != null) {
            omp.msgRequiredVipRank(vipRank);
        }
    }

    public String getRequiredLore(OMPlayer omp) {
        if (currency != null) {
            return "§7Price: " + currency.color().getChatColor() + "§l" + (price == 1 ? currency.getSingleName() : currency.getMultipleName());
        } else if (vipRank != null) {
            return "§7Required: " + vipRank.getRankString() + " VIP";
        } else if (other != null) {
            return omp.getMessage(other);
        } else {
            return null;
        }
    }

    public ItemSet item() {
        if (currency != null) {
            return currency.item();
        } else if (vipRank != null) {
            Material material = null;
            switch (vipRank) {

                case IRON:
                    material = Material.IRON_INGOT;
                    break;
                case GOLD:
                    material = Material.GOLD_INGOT;
                    break;
                case DIAMOND:
                    material = Material.DIAMOND;
                    break;
                case EMERALD:
                    material = Material.EMERALD;
                    break;
            }
            return new ItemSet(material, 0, 1, vipRank.getRankString() + " VIP");
        } else {
            return null;
        }
    }
}
