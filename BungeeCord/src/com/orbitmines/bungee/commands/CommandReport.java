package com.orbitmines.bungee.commands;

import com.orbitmines.api.Database;
import com.orbitmines.api.Message;
import com.orbitmines.api.Server;
import com.orbitmines.bungee.handlers.BungeePlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.text.SimpleDateFormat;
import java.util.*;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandReport extends Command {

    private final String[] alias = {"/report"};

    private Map<UUID, Long> reportCooldown = new HashMap<>();

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void dispatch(BungeePlayer omp, String[] a) {
        ProxiedPlayer p = omp.getPlayer();

        if (a.length >= 3) {
            if (!(reportCooldown.containsKey(omp.getUUID()) && System.currentTimeMillis() - reportCooldown.get(omp.getUUID()) < 1000 * 60 * 30)) {
                ProxiedPlayer p2 = ProxyServer.getInstance().getPlayer(a[1]);

                if (p2 != null) {
                    if (p2 != p) {
                        BungeePlayer omp2 = BungeePlayer.getBungeePlayer(p2);
                        String reason = "";
                        for (int i = 2; i < a.length; i++) {
                            reason += a[i] + " ";
                        }
                        reason = reason.substring(0, reason.length() -1);

                        omp.sendMessage(new Message("§7Je hebt §c" + p2.getName() + " §c§lGEREPORT §7voor §c" + reason + "§7.", "§7You §c§lREPORTED §c" + p2.getName() + " §7for §c" + reason + "§7."));

                        Server server = omp.getServer();
                        String serverName = server.getColor() + "§l" + server.getName();
                        for (BungeePlayer bungeePlayer : BungeePlayer.getPlayers()) {
                            bungeePlayer.getPlayer().sendMessage(new TextComponent("§7(" + serverName + "§7) §7§o" + p.getName() + " §7» §c§o" + p2.getName() + "§7. (§c§o" + reason + "§7)"));
                        }

                        String reportedOn = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date(Calendar.getInstance().getTimeInMillis()));
                        Database.get().insert(Database.Table.REPORTS, Database.get().values(omp2.getUUID().toString(), omp.getUUID().toString(), reportedOn, omp.getServer().getName(), reason.replace("'", "`")));
                    } else {
                        omp.sendMessage(new Message("§7Je kan jezelf niet §creporten§7!", "§7Your can't §creport §7yourself!"));
                    }
                } else {
                   omp.sendMessage(new Message("§7Player §6" + a[1] + " §7is niet §aOnline§7!", "§7Player §6" + a[1] + " §7isn't §aOnline§7!"));
                }

                reportCooldown.put(omp.getUUID(), System.currentTimeMillis());
            } else {
                omp.sendMessage(new Message("§7Je kan een speler alleen elke §c30 minuten§7 reporten!", "§7You can only §creport§7 a player once every §c30 minutes§7!"));
            }
        } else {
            p.sendMessage(new TextComponent("§7" + omp.getMessage(new Message("Gebruik", "Use")) + " §c" + a[0].toLowerCase() + " <player> <reason>§7."));
        }
    }
}
