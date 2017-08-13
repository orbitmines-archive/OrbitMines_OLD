package com.orbitmines.survival.commands.vip;

import com.orbitmines.api.VipRank;
import com.orbitmines.api.spigot.commands.VipCommand;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import org.bukkit.entity.Player;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandEnderchest extends VipCommand {

    private final String[] alias = {"/enderchest"};

    public CommandEnderchest() {
        super(VipRank.EMERALD);
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void onDispatch(OMPlayer omp, String[] a) {
        Player p = omp.getPlayer();
        p.openInventory(p.getEnderChest());
    }
}