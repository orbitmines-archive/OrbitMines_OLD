package com.orbitmines.bungee.events;

import com.orbitmines.api.Database;
import com.orbitmines.bungee.handlers.BungeePlayer;
import com.orbitmines.bungee.utils.ConsoleUtils;
import com.vexsoftware.votifier.bungee.events.VotifierEvent;
import com.vexsoftware.votifier.model.Vote;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Map;
import java.util.UUID;

/*
* OrbitMines - @author Fadi Shawki - 7-8-2017
*/
public class VoteEvent implements Listener {

    @EventHandler
    public void onVote(VotifierEvent event) {
        Vote vote = event.getVote();

        /* We use the database here: only register votes for registered players & MySQL is not case sensitive for values */
        if (!Database.get().contains(Database.Table.PLAYERS, Database.Column.NAME, new Database.Where(Database.Column.NAME, vote.getUsername()))) {
            ConsoleUtils.warn("Vote denied for unknown player: '" + vote.getUsername() + "'");
            return;
        }

        if (vote.getUsername().equalsIgnoreCase("O_o_Fadi_o_O"))
            return;

        UUID uuid = UUID.fromString(Database.get().getString(Database.Table.PLAYERS, Database.Column.UUID, new Database.Where(Database.Column.NAME, vote.getUsername())));
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);

        Map<Database.Column, String> values = Database.get().getValues(Database.Table.PLAYERS, new Database.Where(Database.Column.UUID, uuid.toString()),
                Database.Column.VOTES, Database.Column.CACHEDVOTES);

        Database.get().update(Database.Table.PLAYERS, new Database.Where(Database.Column.UUID, uuid.toString()), new Database.Set(Database.Column.VOTES, "" + (Integer.parseInt(values.get(Database.Column.VOTES)) + 1)));

        //TODO CACHED VOTES

        if (player != null) {
            BungeePlayer bungeePlayer = BungeePlayer.getBungeePlayer(player);
            bungeePlayer.vote();
        }
    }
}
