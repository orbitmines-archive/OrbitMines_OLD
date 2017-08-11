package com.orbitmines.bungee.events;

import com.orbitmines.api.Message;
import com.orbitmines.api.Server;
import com.orbitmines.api.StaffRank;
import com.orbitmines.bungee.OrbitMinesBungee;
import com.orbitmines.bungee.commands.Command;
import com.orbitmines.bungee.handlers.BungeePlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/*
* OrbitMines - @author Fadi Shawki - 7-8-2017
*/
public class PlayerChatEvent implements Listener {

    private OrbitMinesBungee bungee;

    public PlayerChatEvent() {
        bungee = OrbitMinesBungee.getBungee();
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        if (!(event.getSender() instanceof ProxiedPlayer))
            return;

        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        BungeePlayer omp = BungeePlayer.getBungeePlayer(player);
        String[] a = event.getMessage().split(" ");

        if (!a[0].startsWith("/")) {
            /* StaffChat */
            if (a[0].startsWith("@") && omp.isEligible(StaffRank.MODERATOR)) {
                event.setCancelled(true);

                omp.broadcastMessage(StaffRank.MODERATOR, new Message("§d§lStaffChat §8» " + omp.getName() + "§d: §f" + event.getMessage().substring(1)));
            } else { /* AllChat */
                Server server = omp.getServer();
                Message message = new Message(server.getColor() + "§l" + server.getName() + " §8| §8" + omp.getName() + " §8» §7" + event.getMessage());

                for (BungeePlayer bungeePlayer : BungeePlayer.getPlayers()) {
                    if (bungeePlayer.allChat() && bungeePlayer.getPlayer() != player && !omp.getServerInfo().getName().equals(bungeePlayer.getServerInfo().getName()))
                        bungeePlayer.sendMessage(message);
                }
            }
        } else {
            if (omp.isLoggingIn()) {
                event.setCancelled(true);

                if (a[0].equalsIgnoreCase("/l") || a[0].equalsIgnoreCase("/login")) {
                    if (a.length == 1) {
                        player.sendMessage(new TextComponent("§4§lSTAFF PROTECTION §8» §c§l/l <pass>"));
                    } else if (a.length == 2) {
                        if (omp.getLogin().getPassword().equals(a[1])) {
                            omp.setLogin(null);

                            Title t = ProxyServer.getInstance().createTitle();
                            t.fadeIn(20);
                            t.fadeOut(20);
                            t.title(new TextComponent("§4§lSTAFF PROTECTION"));
                            t.subTitle(new TextComponent(omp.getMessage(new Message("§7Welkom terug!", "§7Welcome back!"))));
                            t.send(player);
                        } else {
                            player.disconnect(new TextComponent("§4§lSTAFF PROTECTION\n" + new TextComponent(omp.getMessage(new Message("§7Gekickt!", "§7Kicked!")))));
                        }
                    } else {
                        player.sendMessage(new TextComponent("§4§lSTAFF PROTECTION §8» §c§l/l <pass>"));
                    }
                } else {
                    player.sendMessage(new TextComponent("§4§lSTAFF PROTECTION §8» §c§l/l <pass>"));
                }
                return;
            }

            /* Staff Message */
            Server server = omp.getServer();
            Message message = new Message("§e§lCMD §8» " + server.getColor() + server.getName() + " §8» §e§o" + player.getName() + "§7§o: " + event.getMessage());

            for (BungeePlayer bungeePlayer : BungeePlayer.getPlayers()) {
                if (bungeePlayer.showStaffMsg())
                    bungeePlayer.sendMessage(message);
            }

            Command command = Command.getCommand(a[0]);

            if (command == null) {
                if (bungee.getServerCommands().containsKey(server) && bungee.getServerCommands().get(server).contains(a[0]))
                    return;

//                event.setCancelled(true);
//                omp.msgUnknownCommand(a[0]);
                return;
            }

            event.setCancelled(true);
            command.dispatch(omp, a);
        }
    }
}
