package com.orbitmines.bungee.commands.moderator;

import com.orbitmines.api.Database;
import com.orbitmines.api.Message;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.utils.uuid.UUIDUtils;
import com.orbitmines.bungee.commands.StaffCommand;
import com.orbitmines.bungee.handlers.Ban;
import com.orbitmines.bungee.handlers.BungeePlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandBan extends StaffCommand {

    private final String[] alias = {"/ban"};

    public CommandBan() {
        super(StaffRank.MODERATOR);
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void onDispatch(BungeePlayer omp, String[] a) {
        ProxiedPlayer p = omp.getPlayer();

        if (a.length >= 4) {
            if (a[1].contains(".")) {
                if (Ban.getBan(a[1]) == null) {
                    handle(omp, a, null, a[1]);
                } else {
                    omp.sendMessage(new Message("§7IP §d" + a[1] + "§7 is al §4§lGEBANNED§7!", "§7IP §d" + a[1] + "§7 has already been §4§lBANNED§7!"));
                }
            }

            UUID uuid = UUIDUtils.getUUID(a[1]);

            if (uuid != null) {
                if (Ban.getBan(uuid) == null) {
                    handle(omp, a, uuid, null);
                } else {
                    omp.sendMessage(new Message("§7Speler §d" + a[1] + "§7 is al §4§lGEBANNED§7!", "§7Player §d" + a[1] + "§7 has already been §4§lBANNED§7!"));
                }
            } else {
                omp.sendMessage(new Message("§6" + a[1] + "§7 is nog nooit op §6§lOrbitMines§7 geweest!", "§6" + a[1] + "§7 has never been on §6§lOrbitMines§7!"));
            }
        } else {
            p.sendMessage(new TextComponent("§7" + omp.getMessage(new Message("Gebruik", "Use")) + " §d" + a[0].toLowerCase() + " <player>|<ip> <amount>y/m/d/h (reason)§7."));
        }
    }

    private void handle(BungeePlayer omp, String[] a, UUID uuid, String ip) {
        ProxiedPlayer p = omp.getPlayer();

        Calendar until = Calendar.getInstance();
        try {
            if (a[2].contains("y")) {
                until.add(Calendar.YEAR, Integer.parseInt(a[2].replace("y", "")));
            } else if (a[2].contains("m")) {
                until.add(Calendar.MONTH, Integer.parseInt(a[2].replace("m", "")));
            } else if (a[2].contains("d")) {
                until.add(Calendar.DATE, Integer.parseInt(a[2].replace("d", "")));
            } else if (a[2].contains("h")) {
                until.add(Calendar.HOUR_OF_DAY, Integer.parseInt(a[2].replace("h", "")));
            }
        } catch (NumberFormatException ex) {
            p.sendMessage(new TextComponent("§7" + omp.getMessage(new Message("Gebruik", "Use")) + " §d" + a[0].toLowerCase() + " <player>|<ip> <amount>y/m/d/h (reason)§7."));
            return;
        }

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String bannedUntil = df.format(new Date(until.getTimeInMillis()));
        String bannedOn = df.format(new Date(Calendar.getInstance().getTimeInMillis()));

        String reason = "";
        for (int i = 3; i < a.length; i++) {
            reason += a[i] + " ";
        }
        reason = ChatColor.translateAlternateColorCodes('&', reason.substring(0, reason.length() - 1));

        Database.get().insert(Database.Table.BANS, Database.get().values(uuid != null ? uuid.toString() : ip, "" + true, omp.getUUID().toString(), bannedOn, bannedUntil, reason.replace("'", "`")));
        Ban ban = new Ban(uuid, ip, true, omp.getUUID(), bannedOn, bannedUntil, reason);

        omp.sendMessage(new Message("§d§lStaff §8» §7Je hebt §7" + a[1] + " §4§lGEBANNED§7!", "§d§lStaff §8» §7You §4§lBANNED §7" + a[1] + "!"));

        for (BungeePlayer bungeePlayer : BungeePlayer.getPlayers()) {
            if (ip == null || bungeePlayer.isEligible(StaffRank.MODERATOR)) {
                ProxiedPlayer player = bungeePlayer.getPlayer();
                player.sendMessage(new TextComponent("§4§l§m--------------------------------------------"));
                player.sendMessage(new TextComponent("§6§lOrbitMines§4§lNetwork §7- §4§l§oBan §c(" + omp.getName() + "§c)"));
                player.sendMessage(new TextComponent(""));
                player.sendMessage(new TextComponent("  " + omp.getMessage(new Message("§c" + a[1] + "§7 is §4§lGEBANNED§7!", "§c" + a[1] + "§7 has been §4§lBANNED§7!"))));
                player.sendMessage(new TextComponent("   §c" + bungeePlayer.getMessage(new Message("Reden", "Reason")) + ": §7" + reason));
                player.sendMessage(new TextComponent(""));
                player.sendMessage(new TextComponent("§4§l§m--------------------------------------------"));

                if (player.getName().equalsIgnoreCase(a[1]))
                    player.disconnect(new TextComponent(ban.getMessage(bungeePlayer.getLanguage())));
            }
        }
    }
}
