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
public class CommandBroadcast extends StaffCommand {

    private final String[] alias = {"/broadcast"};

    public CommandBroadcast() {
        super(StaffRank.MODERATOR);
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void onDispatch(BungeePlayer omp, String[] a) {
        if (a.length == 1) {
            omp.getPlayer().sendMessage(new TextComponent("§7" + omp.getMessage(new Message("Gebruik", "Use")) + " §c/broadcast <message>§7."));
        } else {
            String message = "";
            for (int i = 1; i < a.length; i++) {
                message += a[i] + " ";
            }
            message = ChatColor.translateAlternateColorCodes('&', message.substring(0, message.length() - 1));

            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                player.sendMessage(new TextComponent("§4§l§m--------------------------------------------"));
                player.sendMessage(new TextComponent("§6§lOrbitMines§4§lNetwork §7- §c§l§oBroadcast"));
                player.sendMessage(new TextComponent(""));
                player.sendMessage(new TextComponent("  " + omp.getName() + "§c:"));
                player.sendMessage(new TextComponent("   §7" + message));
                player.sendMessage(new TextComponent(""));
                player.sendMessage(new TextComponent("§4§l§m--------------------------------------------"));
            }
        }
    }
}
