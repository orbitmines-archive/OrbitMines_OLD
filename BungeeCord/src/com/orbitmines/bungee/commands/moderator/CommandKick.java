package com.orbitmines.bungee.commands.moderator;

import com.orbitmines.api.Message;
import com.orbitmines.api.StaffRank;
import com.orbitmines.bungee.commands.StaffCommand;
import com.orbitmines.bungee.handlers.BungeePlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandKick extends StaffCommand {

    private final String[] alias = {"/kick"};

    public CommandKick() {
        super(StaffRank.MODERATOR);
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void onDispatch(BungeePlayer omp, String[] a) {
        ProxiedPlayer p = omp.getPlayer();

        if (a.length >= 2) {
            ProxiedPlayer p2 = ProxyServer.getInstance().getPlayer(a[1]);

            if (p2 != null) {
                omp.sendMessage(new Message("§7Je hebt §7" + p2.getName() + " §4§lGEKICKT§7!", "§7You §4§lKICKED §7" + p2.getName() + "!"));

                String message = "";
                if (a.length != 2) {
                    for (int i = 2; i < a.length; i++) {
                        message += a[i] + " ";
                    }
                    message = ChatColor.translateAlternateColorCodes('&', message.substring(0, message.length() - 1));
                }

                if (!message.equals("")) {
                    p2.disconnect(new TextComponent(omp.getMessage(new Message("§cJe bent §4§lGEKICKT§c! (Door " + omp.getName() + "§c)\n§cReden: §6§l" + message, "§cYou've been §4§lKICKED§c! (By " + omp.getName() + "§c)\n§cReason: §6§l" + message))));
                } else {
                    p2.disconnect(new TextComponent(omp.getMessage(new Message("§cJe bent §4§lGEKICKT§c! (Door " + omp.getName() + "§c)", "§cYou've been §4§lKICKED§c! (By " + omp.getName() + "§c)"))));
                }
            } else {
                omp.sendMessage(new Message("§7Player §6" + a[1] + " §7is niet §aOnline§7!", "§7Player §6" + a[1] + " §7isn't §aOnline§7!"));
            }
        } else {
            p.sendMessage(new TextComponent("§7" + omp.getMessage(new Message("Gebruik", "Use"))+ " §d" + a[0].toLowerCase() + " <player> <reason>§7."));
        }
    }
}
