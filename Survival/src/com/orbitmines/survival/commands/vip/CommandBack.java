package com.orbitmines.survival.commands.vip;

import com.orbitmines.api.Message;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.spigot.commands.VipCommand;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.survival.handlers.SurvivalPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandBack extends VipCommand {

    private final String[] alias = {"/back"};

    public CommandBack() {
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

        if (omp.hasBackLocation()) {
            Location l = p.getLocation();
            p.teleport(omp.getBackLocation());
            omp.setBackLocation(l);
            omp.sendMessage(new Message("§7Geteleporteerd naar je vorige locatie.", "§7Teleported to your previous location."));
        } else {
            omp.sendMessage(new Message("§7Je kan niet teleporteren naar je vorige locatie.", "§7You cannot teleport to your previous location."));
        }
    }
}
