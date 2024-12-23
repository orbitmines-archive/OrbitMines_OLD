package com.orbitmines.bungee.commands.moderator;


import com.orbitmines.api.Message;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.utils.uuid.UUIDUtils;
import com.orbitmines.bungee.OrbitMinesBungee;
import com.orbitmines.bungee.commands.StaffCommand;
import com.orbitmines.bungee.handlers.Ban;
import com.orbitmines.bungee.handlers.BungeePlayer;
import com.orbitmines.bungee.handlers.IP;
import com.orbitmines.bungee.handlers.chat.ComponentMessage;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandLookUp extends StaffCommand {

    private final String[] alias = {"/lookup"};

    public CommandLookUp() {
        super(StaffRank.MODERATOR);
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void onDispatch(BungeePlayer omp, String[] a) {
        ProxiedPlayer p = omp.getPlayer();

        if (a.length == 2) {
            if (a[1].contains(".")) {
                p.sendMessage(new TextComponent(""));
                p.sendMessage(new TextComponent("§6§lOrbitMines §7§lAdvanced Lookup"));
                omp.sendMessage(new Message("§6" + a[1] + " §7IP info laden...", "§7Loading §6" + a[1] + " §7IP info..."));

                ProxyServer.getInstance().getScheduler().schedule(OrbitMinesBungee.getBungee(), () -> {
                    send(omp, a[1]);
                }, 1, TimeUnit.SECONDS);
            } else {
                if (a[1].length() > 16) {
                    if (a[1].length() == 36) {
                        UUID uuid = UUID.fromString(a[1]);

                        p.sendMessage(new TextComponent(""));
                        p.sendMessage(new TextComponent("§6§lOrbitMines §7§lAdvanced Lookup"));
                        omp.sendMessage(new Message("§6" + a[1] + "'s §7UUID info laden...", "§7Loading §6" + a[1] + "'s §7UUID info..."));

                        ProxyServer.getInstance().getScheduler().schedule(OrbitMinesBungee.getBungee(), () -> {
                            send(omp, uuid);
                        }, 1, TimeUnit.SECONDS);
                    } else {
                        p.sendMessage(new TextComponent("§7" + omp.getMessage(new Message("Ongeldige", "Invalid")) + " UUID. (§6" + a[0].toLowerCase() + " <uuid>§7)"));
                    }
                } else {
                    UUID uuid = UUIDUtils.getUUID(a[1]);

                    if (uuid != null) {
                        p.sendMessage(new TextComponent(""));
                        p.sendMessage(new TextComponent("§6§lOrbitMines §7§lAdvanced Lookup"));
                        omp.sendMessage(new Message("§6" + a[1] + "§7 info laden...", "§7Loading §6" + a[1] + "§7 info..."));

                        ProxyServer.getInstance().getScheduler().schedule(OrbitMinesBungee.getBungee(), () -> {
                            send(omp, uuid);
                        }, 1, TimeUnit.SECONDS);
                    } else {
                        p.sendMessage(new TextComponent("§7" + omp.getMessage(new Message("Ongeldige", "Invalid")) + " Player. (§6" + a[0].toLowerCase() + " <player>§7)"));
                    }
                }
            }
        } else {
            p.sendMessage(new TextComponent("§7" + omp.getMessage(new Message("Ongeldige Command", "Invalid Command")) + ". (§6" + a[0].toLowerCase() + " <player | uuid | ip>§7)"));
        }
    }

    private synchronized void send(BungeePlayer omp, UUID uuid) {
        ProxiedPlayer p = omp.getPlayer();

        ComponentMessage cm = new ComponentMessage();
        cm.addPart("  §7§lUUID: ");
        cm.addPart("§6" + uuid.toString(), ClickEvent.Action.SUGGEST_COMMAND, uuid.toString(), HoverEvent.Action.SHOW_TEXT, omp.getMessage(new Message("§7Kopieer §6UUID§7.", "§7Copy §6UUID§7.")));
        cm.addPart("§7.");
        cm.send(p);

        omp.sendMessage(new Message("  §7§lNaam Geschiedenis:", "  §7§lName History:"));
        HashMap<String, String> names = UUIDUtils.getNames(uuid);
        for (String name : names.keySet()) {
            if (names.get(name) != null) {
                p.sendMessage(new TextComponent("    §7- §6" + name + " " + names.get(name)));
            } else {
                p.sendMessage(new TextComponent("    §7- §6" + name));
            }
        }

        IP ip = IP.getIp(uuid);
        if (ip != null) {
            omp.sendMessage(new Message("  §7§lLaatst gezien:", " §7§lLast seen:"));
            p.sendMessage(new TextComponent("    §7IP: §6" + ip.getLastIp()));
            p.sendMessage(new TextComponent("    §7Date: §6" + ip.getLastLogin()));
            omp.sendMessage(new Message("  §7§lAlle IPs geregistreerd onder UUID:", "  §7§lAll IPs registered under UUID:"));
            for (String ipString : ip.getAllIps()) {
                p.sendMessage(new TextComponent("    §7- §6" + ipString));
            }

            p.sendMessage(new TextComponent("  §7§lBan:"));

            Ban currentBan = Ban.getBan(uuid, ip.getLastIp());

            if (currentBan != null && !currentBan.hasExpired()) {
                if (currentBan.getUuid() == null)
                    p.sendMessage(new TextComponent("    §7Ip Ban: §6" + currentBan.getIp()));
                else
                    p.sendMessage(new TextComponent("    §7UUID Ban: "));

                p.sendMessage(new TextComponent("      §7" + omp.getMessage(new Message("Gebant door", "Banned by")) + ": §6" + UUIDUtils.getName(currentBan.getBannedBy())));
                p.sendMessage(new TextComponent("      §7" + omp.getMessage(new Message("Sinds", "Since")) + ": §6" + currentBan.getBannedOn()));
                p.sendMessage(new TextComponent("      §7" + omp.getMessage(new Message("Loopt af op", "Banned until")) + ": §6" + currentBan.getBannedUntil()));
                p.sendMessage(new TextComponent("      §7" + omp.getMessage(new Message("Reden", "Reason")) + ": §6" + currentBan.getReason()));
            } else {
                omp.sendMessage(new Message("    §7§oSpeler is niet gebant op dit moment.", " §7§oPlayer is currently not banned."));
            }

            omp.sendMessage(new Message("  §7§lBans die niet meer actief zijn:", "  §7§lBans that have expired:"));

            List<Ban> previousBans = Ban.getExpiredBans(uuid);
            if (previousBans != null && previousBans.size() != 0) {
                for (Ban ban : previousBans) {
                    p.sendMessage(new TextComponent("    §7UUID Ban: §7(§6" + ban.getBannedOn() + "§7 - §6" + ban.getBannedUntil() + "§7)"));
                    p.sendMessage(new TextComponent("      §7" + omp.getMessage(new Message("Gebant door", "Banned by")) + ": §6" + UUIDUtils.getName(ban.getBannedBy())));
                    p.sendMessage(new TextComponent("      §7" + omp.getMessage(new Message("Reden", "Reason")) + ": §6" + ban.getReason()));
                }
            } else {
                omp.sendMessage(new Message("    §7§oEr staan geen bans geregistreerd onder deze UUID.", "   §7§oNo bans registered under UUID."));
            }

            boolean noBans = true;
            for (String ipString : ip.getAllIps()) {
                previousBans = Ban.getExpiredBans(ipString);

                if (previousBans != null && previousBans.size() != 0) {
                    for (Ban ban : previousBans) {
                        p.sendMessage(new TextComponent("    §7IP Ban: §6" + ipString + " §7(§6" + ban.getBannedOn() + "§7 - §6" + ban.getBannedUntil() + "§7)"));
                        p.sendMessage(new TextComponent("      §7" + omp.getMessage(new Message("Gebant door", "Banned by")) + ": §6" + UUIDUtils.getName(ban.getBannedBy())));
                        p.sendMessage(new TextComponent("      §7" + omp.getMessage(new Message("Reden", "Reason")) + ": §6" + ban.getReason()));
                    }
                    noBans = false;
                }
            }

            if (noBans)
                omp.sendMessage(new Message("  §7§oEr staan geen bans geregistreerd onder de IPs van deze UUID.", "  §7§oNo bans registered under the IPs of this UUID."));
        } else {
            omp.sendMessage(new Message("  §7§lIs nog nooit op OrbitMines geweest.", "  §7§lHas never logged in on OrbitMines."));
        }
    }

    private synchronized void send(BungeePlayer omp, String ipString) {
        ProxiedPlayer p = omp.getPlayer();
        List<IP> ipList = IP.getIpInfo(ipString);

        if (ipList.size() != 0) {
            omp.sendMessage(new Message("  §6" + ipList.size() + "§7 spelers gevonden met IP §6" + ipString + "§7."));

            ProxyServer.getInstance().getScheduler().schedule(OrbitMinesBungee.getBungee(), () -> {
                for (IP ip : ipList) {
                    p.sendMessage(new TextComponent(""));
                    send(omp, ip.getUuid());
                }
            }, 2, TimeUnit.SECONDS);
        } else {
            omp.sendMessage(new Message("  §7Geen informatie gevonden voor IP §6" + ipString + "§7.", "  §7No information found for IP §6" + ipString + "§7."));
        }
    }
}
