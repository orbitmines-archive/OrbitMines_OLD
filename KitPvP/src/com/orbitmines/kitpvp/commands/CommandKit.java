package com.orbitmines.kitpvp.commands;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.commands.Command;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import com.orbitmines.kitpvp.handlers.inventory.KitSelectorInventory;

/*
* OrbitMines - @author Fadi Shawki - 11-8-2017
*/
public class CommandKit extends Command {

    private String[] alias = {"/kit"};

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void dispatch(OMPlayer player, String[] a) {
        KitPvPPlayer omp = (KitPvPPlayer) player;

        if (omp.getKitActive() == null) {
            if (omp.isPlayer()) {
                new KitSelectorInventory().open(omp);
            } else {
                omp.sendMessage(new Message("§7Je kan geen §6/kit§7 gebruiken terwijl je aan het §espectaten§7 bent!", "§7You can't use §6/kit§7 while §espectating§7!"));
            }
        } else {
            omp.sendMessage(new Message("§7Je kan geen §6/kit§7 gebruiken terwijl je aan het spelen bent!", "§7You can't use §6/kit§7 while playing!"));
        }
    }
}
