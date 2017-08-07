package com.orbitmines.bungee.commands.moderator;

import com.orbitmines.api.Message;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.utils.uuid.UUIDUtils;
import com.orbitmines.bungee.commands.StaffCommand;
import com.orbitmines.bungee.handlers.Ban;
import com.orbitmines.bungee.handlers.BungeePlayer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandUnBan extends StaffCommand {

    private final String[] alias = {"/unban"};

    public CommandUnBan() {
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
                Ban ban = Ban.getBan(a[1]);
                handle(omp, a, ban);
            } else {
                UUID uuid = UUIDUtils.getUUID(a[1]);

                if (uuid != null) {
                    Ban ban = Ban.getBan(uuid);
                    handle(omp, a, ban);
                } else {
                    omp.sendMessage(new Message("§d" + a[1] + "§7 is nog niet §4§lGEBANNED§7!", "§d" + a[1] + "§7 hasn't been §4§lBANNED§7!"));
                }
            }
        } else {
            p.sendMessage(new TextComponent("§7" + omp.getMessage(new Message("Gebruik", "Use")) + " §d" + a[0].toLowerCase() + " <player>|<ip>§7."));
        }
    }

    private void handle(BungeePlayer omp, String[] a, Ban ban) {
        if (ban != null) {
            ban.unBan();

            omp.sendMessage(new Message("§7Je hebt §7" + a[1] + " een §a§lUNBAN§7 gegeven.", "§7You §a§lUNBANNED §7" + a[1] + "§7."));

            for (BungeePlayer bungeePlayer : BungeePlayer.getPlayers()) {
                if (ban.getIp() == null || bungeePlayer.isEligible(StaffRank.MODERATOR)) {
                    ProxiedPlayer player = bungeePlayer.getPlayer();
                    player.sendMessage(new TextComponent("§4§l§m--------------------------------------------"));
                    player.sendMessage(new TextComponent("§6§lOrbitMines§4§lNetwork §7- §a§l§oUnban §c(" + omp.getName() + "§c)"));
                    player.sendMessage(new TextComponent(""));
                    player.sendMessage(new TextComponent(omp.getMessage(new Message("  §c" + a[1] + "§7 heeft een §a§lUNBAN§7 gekregen!", "  §c" + a[1] + "§7 has been §a§lUNBANNED§7!"))));
                    player.sendMessage(new TextComponent(""));
                    player.sendMessage(new TextComponent("§4§l§m--------------------------------------------"));
                }
            }
        } else {
            omp.sendMessage(new Message("§d" + a[1] + "§7 is nog niet §4§lGEBANNED§7!", "§d" + a[1] + "§7 hasn't been §4§lBANNED§7!"));
        }
    }
}
