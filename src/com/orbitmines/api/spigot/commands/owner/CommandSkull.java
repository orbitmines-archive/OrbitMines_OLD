package com.orbitmines.api.spigot.commands.owner;

import com.orbitmines.api.Message;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.spigot.commands.StaffCommand;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.itembuilders.PlayerSkullBuilder;
import org.bukkit.entity.Player;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandSkull extends StaffCommand {

    private final String[] alias = {"/skull"};

    public CommandSkull() {
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
            omp.sendMessage(new Message("§7Je hebt §6" + a[1] + "'s§7 skull ontvangen.", "§7You've been given §6" + a[1] + "'s§7 skull."));

            p.getInventory().addItem(new PlayerSkullBuilder(a[1], 1, "§6" + a[1] + "'s §7Skull").build());
        } else {
            p.sendMessage("§7" + omp.getMessage(new Message("Invalid Command", "Ongeldige Command")) + ". (§6" + a[0].toLowerCase() + " <player>§7)");
        }
    }
}
