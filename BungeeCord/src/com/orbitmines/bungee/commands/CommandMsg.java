package com.orbitmines.bungee.commands;

import com.orbitmines.api.Message;
import com.orbitmines.bungee.handlers.BungeePlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandMsg extends Command {

    private final String[] alias = {"/msg","/m","/t","/tell","/w","/whisper"};

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void dispatch(BungeePlayer omp, String[] a) {
        ProxiedPlayer p = omp.getPlayer();

        if (a.length >= 3) {
            ProxiedPlayer p2 = ProxyServer.getInstance().getPlayer(a[1]);

            if (p2 != null) {
                String message = "";
                for (int i = 2; i < a.length; i++) {
                    message += a[i] + " ";
                }
                message = message.substring(0, message.length() - 1);

                BungeePlayer omp2 = BungeePlayer.getBungeePlayer(p2);

                p2.sendMessage(new TextComponent("§9§lMSG §8»§9§l " + p.getName() + "§7 §7§l" + omp2.getMessage(new Message("Jij", "You")) + "§7: §f" + message));
                p.sendMessage(new TextComponent("§9§lMSG §8» §7§l" + omp.getMessage(new Message("Jij", "You")) + "§7 §9§l " + p2.getName() + "§7: §f" + message));
                if (omp2.getLastMsg() == null)
                    p2.sendMessage(new TextComponent("§9§lMSG §8»§7 " + omp2.getMessage(new Message("Gebruik", "Use")) + " §9/r <message>§7 " + omp2.getMessage(new Message("om te reageren", "to reply")) + "."));

                omp.setLastMsg(omp2.getPlayer().getUniqueId());
                omp2.setLastMsg(omp.getPlayer().getUniqueId());
            } else {
                omp.sendMessage(new Message("§7Player §6" + a[1] + " §7is niet §aOnline§7!", "§7Player §6" + a[1] + " §7isn't §aOnline§7!"));
            }
        } else {
            p.sendMessage(new TextComponent("§9§lMSG §8» §7" + omp.getMessage(new Message("Gebruik", "Use")) + " §9" + a[0].toLowerCase() + " <player> <message>§7."));
        }
    }
}
