package com.orbitmines.api.spigot.commands.owner;

import com.orbitmines.api.Message;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.spigot.commands.StaffCommand;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandHeal extends StaffCommand {

    private final String[] alias = {"/heal"};

    public CommandHeal() {
        super(StaffRank.OWNER);
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void onDispatch(OMPlayer omp, String[] a) {
        Player p = omp.getPlayer();

        if (a.length == 2) {
            Player p2 = Bukkit.getPlayer(a[1]);
            OMPlayer omp2 = OMPlayer.getPlayer(p2);

            if (p2 != null) {
                if (p2 == p) {
                    p.setHealth(20);
                    p.setFoodLevel(20);
                    omp.sendMessage(new Message("§7Je hebt jezelf §6geheeld§7.", "§7You've been §6healed§7."));
                } else {
                    p2.setHealth(20);
                    p.setFoodLevel(20);
                    omp.sendMessage(new Message("§7Je hebt " + omp2.getName() + " §6geheeld§7.", omp2.getName() + " §7has been §6healed§7."));
                    omp2.sendMessage(new Message("§7Je bent §6geheeld§7 door " + omp.getName() + "§7.", "§7You were §6healed§7 by " + omp.getName() + "§7."));
                }
            } else {
                omp.sendMessage(new Message("§7Player §6" + a[1] + " §7is niet §aOnline§7!", "§7Player §6" + a[1] + " §7isn't §aOnline§7!"));
            }
        } else {
            p.setHealth(20);
            p.setFoodLevel(20);
            omp.sendMessage(new Message("§7Je hebt jezelf §6geheeld§7.", "§7You've been §6healed§7."));
        }
    }
}
