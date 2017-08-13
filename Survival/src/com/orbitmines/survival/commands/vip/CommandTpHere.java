package com.orbitmines.survival.commands.vip;

import com.orbitmines.api.Message;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.spigot.commands.VipCommand;
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
public class CommandTpHere extends VipCommand {

    private final String[] alias = {"/tphere"};

    public CommandTpHere() {
        super(VipRank.EMERALD);
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void onDispatch(OMPlayer player, String[] a) {
        SurvivalPlayer omp = (SurvivalPlayer) player;
        Player p = omp.getPlayer();

        if (a.length == 2) {
            Player p2 = Bukkit.getPlayer(a[1]);

            if (p2 != null) {
                if (p2 != p) {
                    SurvivalPlayer omp2 = SurvivalPlayer.getPlayer(p2);

                    omp2.getTpHereRequests().add(p.getName());

                    omp.sendMessage(new Message("§7Teleport verzoek verzonden naar §6" + omp2.getName() + "§7!", "§7Teleport request sent to §6" + omp2.getName() + "§7!"));
                    omp2.sendMessage(new Message(omp.getName() + "§7 wilt jouw naar hem/haar teleporteren.", omp.getName() + "§7 wants you to teleport to them."));

                    ComponentMessage cm = new ComponentMessage();
                    cm.addPart(omp2.getMessage(new Message("  §7Klik hier om te §aAccepteren§7.", "  §7Click here to §aAccept§7.")), ClickEvent.Action.RUN_COMMAND, "/accept " + p.getName(), HoverEvent.Action.SHOW_TEXT, omp2.getMessage(new Message("§7Teleporteren naar " + omp.getName() + "§7.", "§7Teleport to " + omp.getName() + "§7.")));
                    cm.send(p2);
                } else {
                    omp.sendMessage(new Message("§7Je kan niet naar jezelf toe §6teleporten§7!", "§7You can't §6teleport§7 to yourself!"));
                }
            } else {
                omp.sendMessage(new Message("§6" + a[1] + "§7 is niet §aonline§7!", "§6" + a[1] + "§7 is not §aonline§7!"));
            }
        } else {
            p.sendMessage("§7" + omp.getMessage(new Message("Gebruik", "Use")) + " §6/tphere <player>§7.");
        }
    }
}
