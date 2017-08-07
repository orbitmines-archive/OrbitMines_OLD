package com.orbitmines.bungee.commands.moderator;

import com.orbitmines.api.Message;
import com.orbitmines.api.StaffRank;
import com.orbitmines.bungee.commands.StaffCommand;
import com.orbitmines.bungee.handlers.BungeePlayer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandSay extends StaffCommand {

    private final String[] alias = {"/say"};

    public CommandSay() {
        super(StaffRank.MODERATOR);
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void onDispatch(BungeePlayer omp, String[] a) {
        if (a.length >= 2) {
            String message = "";
            for (int i = 1; i < a.length; i++) {
                message += a[i] + " ";
            }
            message = message.substring(0, message.length() -1);

            for (ProxiedPlayer player : omp.getServerInfo().getPlayers()) {
                player.sendMessage(new TextComponent("§7\n§d§lSay §8» " + omp.getName() + " §d" + message + "\n§7"));
            }
        } else {
            omp.getPlayer().sendMessage(new TextComponent("§7" + omp.getMessage(new Message("Gebruik", "Use")) + " §d/say <message>§7."));
        }
    }
}
