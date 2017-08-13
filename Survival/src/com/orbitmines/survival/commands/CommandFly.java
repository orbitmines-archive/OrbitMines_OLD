package com.orbitmines.survival.commands;

import com.orbitmines.api.Message;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.spigot.commands.Command;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandFly extends Command {

    private final String[] alias = {"/fly"};

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void dispatch(OMPlayer omp, String[] a) {
        Player p = omp.getPlayer();

        if (omp.isEligible(StaffRank.MODERATOR) && a.length == 2) {
            Player p2 = Bukkit.getPlayer(a[1]);
            OMPlayer omp2 = OMPlayer.getPlayer(p2);

            if (p2 != null) {
                if (p2 == p) {
                    p.setAllowFlight(!p.getAllowFlight());
                    p.setFlying(p.getAllowFlight());
                    omp.sendMessage(new Message("§7Je §fFly§7 mode staat nu " + omp.statusString(p.getAllowFlight()) + "§7.", omp.statusString(p.getAllowFlight()) + " §7your §fFly§7 mode!"));
                } else {
                    p2.setAllowFlight(!p2.getAllowFlight());
                    p2.setFlying(p2.getAllowFlight());
                    omp.sendMessage(new Message(omp2.getName() + "'s §fFly§7 mode staat nu " + omp.statusString(p2.getAllowFlight()) + "§7.", omp.statusString(p2.getAllowFlight()) + " " + omp2.getName() + "'s §fFly§7 mode!"));
                    omp2.sendMessage(new Message("§7Je §fFly§7 mode staat nu " + omp2.statusString(p2.getAllowFlight()) + "§7 door " + omp.getName() + "§7.", omp.getName() + " " + omp2.statusString(p2.getAllowFlight()) + " §7your §fFly§7 mode!"));
                }
            } else {
                omp.sendMessage(new Message("§7Player §6" + a[1] + " §7is niet §aOnline§7!", "§7Player §6" + a[1] + " §7isn't §aOnline§7!"));
            }
        } else if (omp.isEligible(VipRank.DIAMOND) || omp.isEligible(StaffRank.MODERATOR)) {
            p.setAllowFlight(!p.getAllowFlight());
            p.setFlying(p.getAllowFlight());
            omp.sendMessage(new Message("§7Je §fFly§7 mode staat nu " + omp.statusString(p.getAllowFlight()) + "§7.", omp.statusString(p.getAllowFlight()) + " §7your §fFly§7 mode!"));
        } else {
            omp.msgRequiredVipRank(VipRank.DIAMOND);
        }
    }
}
