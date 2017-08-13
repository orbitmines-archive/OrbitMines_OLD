package com.orbitmines.survival.commands;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.commands.Command;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.survival.handlers.SurvivalPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandAccept extends Command {

    private final String[] alias = {"/accept"};

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void dispatch(OMPlayer player, String[] a) {
        SurvivalPlayer omp = (SurvivalPlayer) player;

        if (a.length == 1)
            return;

        Player p2 = Bukkit.getPlayer(a[1]);
        if (p2 == null) {
            omp.sendMessage(new Message("§6" + a[1] + "§7 is niet meer §aonline§7!", "§6" + a[1] + "§7 is no longer §aonline§7!"));
            omp.getTpHereRequests().remove(a[1]);
            omp.getTpRequests().remove(a[1]);
            return;
        }

        Player p = omp.getPlayer();
        SurvivalPlayer omp2 = SurvivalPlayer.getPlayer(p2);

        if (omp.hasTpRequestFrom(a[1])) {
            omp2.setBackLocation(p2.getLocation());
            p2.teleport(p);
            omp.sendMessage(new Message(omp2.getName() + " §7is naar jouw geteleporteerd.", omp2.getName() + " §7has been teleporter to you."));
            omp2.sendMessage(new Message(omp.getName() + " §7heeft je verzoek geaccepteerd.", omp.getName() + " §7accepted your request."));
            p2.playSound(p2.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 5, 1);

            omp.getTpRequests().remove(a[1]);
        } else if (omp.hasTpHereRequestFrom(a[1])) {
            omp.setBackLocation(p.getLocation());
            p.teleport(p2);
            omp.sendMessage(new Message("§7Je bent geteleporteerd naar " + omp2.getName() + "§7.", "§7You have been teleported to " + omp2.getName() + "§7."));
            omp2.sendMessage(new Message(omp.getName() + " §7heeft je verzoek geaccepteerd.", omp.getName() + " §7accepted your request."));
            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 5, 1);

            omp.getTpHereRequests().remove(a[1]);
        } else {
            omp.msgUnknownCommand(a[0]);
        }
    }
}
