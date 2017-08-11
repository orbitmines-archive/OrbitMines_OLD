package com.orbitmines.bungee.events;

import com.orbitmines.api.Database;
import com.orbitmines.api.Server;
import com.orbitmines.bungee.OrbitMinesBungee;
import com.orbitmines.bungee.handlers.BungeePlayer;
import com.orbitmines.bungee.utils.ConsoleUtils;
import com.vexsoftware.votifier.bungee.events.VotifierEvent;
import com.vexsoftware.votifier.model.Vote;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/*
* OrbitMines - @author Fadi Shawki - 7-8-2017
*/
public class VoteEvent implements Listener {

    private OrbitMinesBungee bungee;

    public VoteEvent() {
        bungee = OrbitMinesBungee.getBungee();
    }

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

        Map<Database.Column, String> values = Database.get().getValues(Database.Table.PLAYERS, new Database.Where(Database.Column.UUID, uuid.toString()),
                Database.Column.VOTES, Database.Column.CACHEDVOTES);

        Database.get().update(Database.Table.PLAYERS, new Database.Where(Database.Column.UUID, uuid.toString()), new Database.Set(Database.Column.VOTES, "" + (Integer.parseInt(values.get(Database.Column.VOTES)) + 1)));

        /* SERVER~10|SERVER~10 */
        String cachedVotesString = values.get(Database.Column.CACHEDVOTES);
        Map<Server, Integer> cachedVotes = new HashMap<>();
        if (!cachedVotesString.equals("null")) {
            for (String voteData : cachedVotesString.split("\\|")) {
                String[] data = voteData.split("~");

                cachedVotes.put(Server.valueOf(data[0]), Integer.parseInt(data[1]));
            }
        }

        for (Server server : new Server[]{Server.HUB, Server.KITPVP, Server.SURVIVAL}) {//TODO TEMPORAIRLY
            if (!cachedVotes.containsKey(server))
                cachedVotes.put(server, 1);
            else
                cachedVotes.put(server, cachedVotes.get(server) + 1);
        }

        updateCachedVotes(uuid, cachedVotes);

        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
        if (player != null) {
            BungeePlayer bungeePlayer = BungeePlayer.getBungeePlayer(player);
            bungeePlayer.vote();
        }
    }

    private void updateCachedVotes(UUID uuid, Map<Server, Integer> cachedVotes) {
        String value;

        if (cachedVotes != null && cachedVotes.size() != 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Server server : cachedVotes.keySet()) {
                stringBuilder.append(server.toString());
                stringBuilder.append("~");
                stringBuilder.append(cachedVotes.get(server));
                stringBuilder.append("|");
            }

            value = stringBuilder.toString().substring(0, stringBuilder.length() - 1);
        } else {
            value = "null";
        }

        Database.get().update(Database.Table.PLAYERS, new Database.Where(Database.Column.UUID, uuid.toString()), new Database.Set(Database.Column.CACHEDVOTES, value));
    }
}
