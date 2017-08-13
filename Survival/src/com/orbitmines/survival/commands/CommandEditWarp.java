package com.orbitmines.survival.commands;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.commands.Command;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.survival.handlers.SurvivalPlayer;
import com.orbitmines.survival.handlers.Warp;
import com.orbitmines.survival.handlers.inventory.WarpEditorInventory;
import org.bukkit.entity.Player;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandEditWarp extends Command {

    private final String[] alias = {"/editwarp"};

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void dispatch(OMPlayer player, String[] a) {
        SurvivalPlayer omp = (SurvivalPlayer) player;
        Player p = omp.getPlayer();

        if (a.length == 2) {
            Warp warp = Warp.getWarp(a[1]);

            if (warp != null && omp.getWarps().contains(warp)) {
                new WarpEditorInventory(warp).open(omp);
            } else {
                omp.sendMessage(new Message("§7Je hebt geen warp genaamd '§6" + a[1] + "§7'.", "§7You don't possess a warp named '§6" + a[1] + "§7'."));
            }
        } else {
            p.sendMessage("§7" + omp.getMessage(new Message("Gebruik", "Use")) + " §6/editwarp <warp>§7.");
        }
    }
}
