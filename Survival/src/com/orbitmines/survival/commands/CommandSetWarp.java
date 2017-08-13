
package com.orbitmines.survival.commands;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.commands.Command;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.survival.Survival;
import com.orbitmines.survival.handlers.SurvivalPlayer;
import com.orbitmines.survival.handlers.inventory.WarpRenameGui;
import org.bukkit.entity.Player;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandSetWarp extends Command {

    private final String[] alias = {"/setwarp"};

    private Survival survival;

    public CommandSetWarp() {
        survival = Survival.getInstance();
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void dispatch(OMPlayer player, String[] a) {
        SurvivalPlayer omp = (SurvivalPlayer) player;
        Player p = omp.getPlayer();
        int warps = omp.survival().getWarpsAllowed();

        if (omp.getWarps().size() < warps) {
            if (p.getWorld().getName().equals(survival.getWorld().getName())) {
                new WarpRenameGui(omp, null).open();
            } else {
                omp.sendMessage(new Message("§7Je kan alleen maar warps maken in de overworld!", "§7You're only allowed to create warps in the overworld!"));
            }
        } else {
            if (omp.getWarps().size() == 0) {
                omp.sendMessage(new Message("§7Je kan geen warps neerzetten! Type §3/donate§7 om erachter te komen hoe je er eentje krijgt!", "§7You cannot set any warps! Type §3/donate§7 to discover how to create one!"));
            } else {
                omp.sendMessage(new Message("§7Je hebt al het maximum aantal warps! (§6" + warps + "§7)", "§7You already reached the maximum amount of warps! (§6" + warps + "§7)"));
            }
        }
    }
}
