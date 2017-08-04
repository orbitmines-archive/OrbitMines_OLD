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
public class CommandFeed extends StaffCommand {

    private final String[] alias = {"/feed", "/eat"};

    public CommandFeed() {
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
                    p.setFoodLevel(20);
                    omp.sendMessage(new Message("§7Je hebt geen honger meer.", "§7Restored your §6Hungerbar§7!"));
                } else {
                    omp.sendMessage(new Message(omp2.getName() + "§7 heeft geen honger meer.", "§7Restored " + omp2.getName() + "'s §6Hungerbar§7!"));
                    omp2.sendMessage(new Message("§7Je hebt geen honger meer door " + omp.getName() + "§7.", "§7" + omp.getName() + "§7 restored your §6Hungerbar§7!"));
                    p2.setFoodLevel(20);
                }
            } else {
                omp.sendMessage(new Message("§7Player §6" + a[1] + " §7is niet §aOnline§7!", "§7Player §6" + a[1] + " §7isn't §aOnline§7!"));
            }
        } else {
            p.setFoodLevel(20);
            omp.sendMessage(new Message("§7Je hebt geen honger meer.", "§7Restored your §6Hungerbar§7!"));
        }
    }
}
