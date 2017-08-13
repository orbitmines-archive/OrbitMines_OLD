package com.orbitmines.survival.commands;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.commands.Command;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.inventory.InventoryViewer;
import com.orbitmines.survival.handlers.SurvivalPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandInvAccept extends Command {

    private final String[] alias = {"/invaccept"};

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void dispatch(OMPlayer player, String[] a) {
        SurvivalPlayer omp = (SurvivalPlayer) player;

        if (a.length == 1) {
            omp.msgUnknownCommand(a[0]);
            return;
        }

        Player p2 = Bukkit.getPlayer(a[1]);
        if (p2 == null) {
            omp.sendMessage(new Message("§6" + a[1] + "§7 is niet meer §aonline§7!", "§6" + a[1] + "§7 is no longer §aonline§7!"));
            omp.getInvSeeRequests().remove(a[1]);
            return;
        }

        Player p = omp.getPlayer();
        SurvivalPlayer omp2 = SurvivalPlayer.getPlayer(p2);

        if (omp.hasInvseeRequestFrom(a[1])) {
            new InventoryViewer(p).open(omp2);
            omp.sendMessage(new Message(omp2.getName() + " §7is nu jouw inventory aan het bekijken.", omp2.getName() + " §7is now viewing your inventory."));
            omp2.sendMessage(new Message(omp.getName() + " §7heeft je verzoek geaccepteerd.", omp.getName() + " §7accepted your request."));
            p2.playSound(p2.getLocation(), Sound.BLOCK_CHEST_OPEN, 5, 1);

            omp.getInvSeeRequests().remove(a[1]);
        } else {
            omp.msgUnknownCommand(a[0]);
        }
    }
}
