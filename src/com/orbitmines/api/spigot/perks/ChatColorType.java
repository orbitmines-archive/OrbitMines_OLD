package com.orbitmines.api.spigot.perks;

import com.orbitmines.api.VipRank;
import com.orbitmines.api.spigot.handlers.ItemSet;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.Obtainable;
import org.bukkit.Material;

/*
* OrbitMines - @author Fadi Shawki - 7/28/2017
*/
public enum ChatColorType implements Perk {

    BOLD("§l", "Bold", new ItemSet(Material.STAINED_GLASS_PANE), new Obtainable(VipRank.NONE)),
    CURSIVE("§o", "Cursive", new ItemSet(Material.STAINED_GLASS_PANE), new Obtainable(VipRank.DIAMOND)),;

    private final String type;
    private final String name;

    private final ItemSet item;

    private final Obtainable obtainable;

    ChatColorType(String type, String name, ItemSet item, Obtainable obtainable) {
        this.type = type;
        this.name = name;
        this.item = item;
        this.obtainable = obtainable;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public ItemSet item() {
        return item;
    }

    @Override
    public Obtainable obtainable() {
        return obtainable;
    }

    @Override
    public String getDisplayName() {
        return "§7" + type + name + " Chat Color";
    }

    @Override
    public boolean hasAccess(OMPlayer omp) {
        return omp.chatcolors().hasChatColorType(this);
    }
}
