package com.orbitmines.hub.handlers;

import com.orbitmines.api.Data;
import com.orbitmines.api.Language;
import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.chat.Title;
import com.orbitmines.api.spigot.handlers.kit.Kit;
import com.orbitmines.api.spigot.perks.Gadget;
import com.orbitmines.hub.handlers.playerdata.HubData;
import net.firefang.ip2c.IpUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 8-8-2017
*/
public class HubPlayer extends OMPlayer {

    private static List<HubPlayer> players = new ArrayList<>();

    private boolean canChat;

    public HubPlayer(Player player) {
        super(player);
        players.add(this);

        canChat = false;
    }

    @Override
    public String getPrefix() {
        return "";
    }

    @Override
    public void onVote(int votes) {
        general().addTokens(1);

        if (votes != 1)
            return;

        Title title = new Title("§b§lVote", "§e+1 OrbitMines Token", 20, 40, 20);
        title.send(player);
    }

    @Override
    protected void onLogin() {
        if (hub().hasScoreboardEnabled())
            setScoreboard(new HubScoreboard(this));

        msgJoin();
        msgJoinTitle();
        broadcastJoinMessage();

        giveLobbyKit();

        if (language == Language.DUTCH) {
            String country = IpUtils.getCountry(getPlayer());
            if (country == null || (!country.equals("Netherlands") && !country.equals("Belgium") && !country.equals("Luxembourg"))) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.sendMessage("");
                        player.sendMessage("§c§lIMPORTANT:");
                        player.sendMessage("§7Welcome to OrbitMines, " + getColorName() + "§7!");
                        player.sendMessage("§7The default language on OrbitMines is §c§lDutch§7. It seems like you're not from the Benelux. We have added an option for players like you to change the language of all messages on our server. Click on the §c§lRedstone Torch§7, then click on the §c§lBanner§7 in order to switch to §c§lEnglish§7.");
                    }
                }.runTaskLater(api, 30);
            }
        }
    }

    @Override
    protected void onLogout() {
        broadcastQuitMessage();

        players.remove(this);
    }

    @Override
    public boolean canReceiveVelocity() {
        return true;
    }

    /* Getters & Setters */

    public boolean canChat() {
        return canChat;
    }

    public void setCanChat() {
        this.canChat = true;

        sendMessage(new Message("§7Chat staat nu §a§lAAN§7!", "§7Chat §a§lENABLED§7!"));
    }

    @Override
    public void setLanguage(Language language) {
        super.setLanguage(language);

        giveLobbyKit();
    }

    private void giveLobbyKit() {
        player.getInventory().clear();

        Kit.getKit(language.toString()).setItems(player);

        Gadget gadget = gadgets().getGadgetEnabled();
        if (gadget != null)
            gadget.getHandler().giveItem(this);
    }

    public HubData hub() {
        return (HubData) data(Data.HUB);
    }

    public static HubPlayer getPlayer(Player player) {
        for (HubPlayer omp : players) {
            if (omp.getPlayer() == player)
                return omp;
        }
        return null;
    }

    public static List<HubPlayer> getHubPlayers() {
        return players;
    }
}
