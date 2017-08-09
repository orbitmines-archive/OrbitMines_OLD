package com.orbitmines.bungee.events;

import com.orbitmines.api.*;
import com.orbitmines.bungee.OrbitMinesBungee;
import com.orbitmines.bungee.handlers.Ban;
import com.orbitmines.bungee.handlers.BungeePlayer;
import com.orbitmines.bungee.utils.ServerUtils;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/*
* OrbitMines - @author Fadi Shawki - 7-8-2017
*/
public class JoinEvent implements Listener {

    private OrbitMinesBungee bungee;

    public JoinEvent() {
        bungee = OrbitMinesBungee.getBungee();
    }

    @EventHandler
    public void onLogin(LoginEvent event) {
        PendingConnection connection = event.getConnection();
        UUID uuid = connection.getUniqueId();
        String ip = connection.getAddress().getHostString();

        Ban ban = Ban.getBan(uuid, ip);

        if (ban == null)
            return;

        Language language = getLanguage(uuid);

        if (ban.hasExpired()) {
            //TODO send message no longer banned
            return;
        }

        event.setCancelled(true);
        event.setCancelReason(new TextComponent(ban.getMessage(language)));
    }

    @EventHandler
    public void onLogin(ServerConnectEvent event) {
        ProxiedPlayer player = event.getPlayer();

        if (player.getServer() != null)
            return;

        BungeePlayer bungeePlayer = new BungeePlayer(player);

        if (!bungee.isInMaintenance()) {
            bungeePlayer.onLogin();
            event.setTarget(ServerUtils.getServerInfo(Server.HUB));
            return;
        }

        if (!bungeePlayer.isEligible(StaffRank.OWNER)) {
            event.setCancelled(true);
            player.disconnect(new TextComponent(new Message("§d§lMaintenance Mode\n§dWe zijn wat aan het veranderen op de server!\n§d§oDank je, voor je begrip!", "§d§lMaintenance Mode\n§dWe're currently working on something!\n§d§oThank you, for your understanding!").lang(getLanguage(player.getUniqueId()))));
        } else {
            bungeePlayer.onLogin();
            event.setTarget(ServerUtils.getServerInfo(Server.HUB));
        }
    }

    private Language getLanguage(UUID uuid) {
        if (bungee.getCachedLanguage().containsKey(uuid))
            return bungee.getCachedLanguage().get(uuid);

        Language language;
        try {
            language = Language.valueOf(Database.get().getString(Database.Table.PLAYERS, Database.Column.LANGUAGE, new Database.Where(Database.Column.UUID, uuid.toString())));
        } catch (NullPointerException | IllegalArgumentException ex) {
            language = Language.DUTCH;
        }

        bungee.getCachedLanguage().put(uuid, language);

        /* Cleanup */
        bungee.getProxy().getScheduler().schedule(bungee, () -> bungee.getCachedLanguage().remove(uuid), 15, TimeUnit.MINUTES);

        return language;
    }
}
