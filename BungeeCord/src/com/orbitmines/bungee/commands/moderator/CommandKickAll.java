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
public class CommandKickAll extends StaffCommand {

    private final String[] alias = {"/kickall"};

    public CommandKickAll() {
        super(StaffRank.MODERATOR);
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void onDispatch(BungeePlayer omp, String[] a) {
        String message = "";
        if (a.length != 1) {
            for (int i = 1; i < a.length; i++) {
                message += a[i] + " ";
            }
            message = ChatColor.translateAlternateColorCodes('&', message.substring(0, message.length() - 1));
        }

        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            if (!message.equals("")) {
                player.disconnect(new TextComponent(omp.getMessage(new Message("§cJe bent §4§lGEKICKT§c! (Door " + omp.getName() + "§c)\n§cReden: §6§l" + message, "§cYou've been §4§lKICKED§c! (By " + omp.getName() + "§c)\n§cReason: §6§l" + message))));
            } else {
                player.disconnect(new TextComponent(omp.getMessage(new Message("§cJe bent §4§lGEKICKT§c! (Door " + omp.getName() + "§c)", "§cYou've been §4§lKICKED§c! (By " + omp.getName() + "§c)"))));
            }
        }
    }
}
