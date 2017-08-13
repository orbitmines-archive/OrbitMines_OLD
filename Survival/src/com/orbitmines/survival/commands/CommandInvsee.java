package com.orbitmines.survival.commands;

import com.orbitmines.api.Message;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.spigot.commands.Command;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.chat.ComponentMessage;
import com.orbitmines.survival.handlers.SurvivalPlayer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandInvsee extends Command {

    private final String[] alias = {"/invsee"};

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void dispatch(OMPlayer omp, String[] a) {
        Player p = omp.getPlayer();

        if (omp.isEligible(VipRank.DIAMOND) || omp.isEligible(StaffRank.MODERATOR)) {
            if (a.length == 2) {
                Player p2 = Bukkit.getPlayer(a[1]);

                if (p2 != null) {
                    SurvivalPlayer omp2 = SurvivalPlayer.getPlayer(p2);

                    omp2.getInvSeeRequests().add(p.getName());
                    omp.sendMessage(new Message("§7Invsee verzoek verzonden naar §6" + omp2.getName() + "§7!", "§7Invsee request sent to §6" + omp2.getName() + "§7!"));
                    omp2.sendMessage(new Message(omp.getName() + "§7 wilt jouw inventory bekijken.", omp.getName() + "§7 wants to view your inventory."));

                    ComponentMessage cm = new ComponentMessage();
                    cm.addPart(omp2.getMessage(new Message("  §7Klik hier om te §aAccepteren§7.", "  §7Click here to §aAccept§7.")), ClickEvent.Action.RUN_COMMAND, "/invaccept " + p.getName(), HoverEvent.Action.SHOW_TEXT, omp2.getMessage(new Message("§7Teleporteren naar " + omp.getName() + "§7.", "§7Teleport to " + omp.getName() + "§7.")));
                    cm.send(p2);
                } else {
                    omp.sendMessage(new Message("§6" + a[1] + "§7 is niet §aonline§7!", "§6" + a[1] + "§7 is not §aonline§7!"));
                }
            } else {
                p.sendMessage("§7" + omp.getMessage(new Message("Gebruik", "Use")) + " §6/invsee <player>§7.");
            }
        } else {
            omp.msgRequiredVipRank(VipRank.DIAMOND);
        }
    }
}
