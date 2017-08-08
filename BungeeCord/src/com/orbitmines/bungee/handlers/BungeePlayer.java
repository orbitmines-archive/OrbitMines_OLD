package com.orbitmines.bungee.handlers;

import com.orbitmines.api.*;
import com.orbitmines.bungee.OrbitMinesBungee;
import com.orbitmines.bungee.utils.ServerUtils;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/*
* OrbitMines - @author Fadi Shawki - 6-8-2017
*/
public class BungeePlayer {

    private static List<BungeePlayer> players = new ArrayList<>();

    private ProxiedPlayer player;

    private Language language;
    private StaffRank staffRank;
    private VipRank vipRank;
    private boolean silent;

    private UUID lastMsg;
    private boolean showStaffMsg;
    private boolean allChat;

    private Login login;

    public BungeePlayer(ProxiedPlayer player) {
        players.add(this);

        this.player = player;
        this.language = Language.DUTCH;
        this.staffRank = StaffRank.NONE;
        this.vipRank = VipRank.NONE;
        this.showStaffMsg = false;
        this.allChat = false;
        this.silent = false;
    }

    public void onLogin() {
        /* Staff Protection */
        OrbitMinesBungee bungee = OrbitMinesBungee.getBungee();
        if (bungee.getPasswords().containsKey(getUUID()))
            login = new Login(this, bungee.getPasswords().get(getUUID()));

        if (!Database.get().contains(Database.Table.PLAYERS, Database.Column.UUID, new Database.Where(Database.Column.UUID, getUUID().toString())))
            Database.get().insert(Database.Table.PLAYERS, Database.get().values(getUUID().toString(), player.getName(), StaffRank.NONE.toString(), VipRank.NONE.toString(), Language.DUTCH.toString(), "" + false, "" + false, "" + 0, "null", "null"));

        Map<Database.Column, String> values = Database.get().getValues(Database.Table.PLAYERS, new Database.Where(Database.Column.UUID, getUUID().toString()),
                Database.Column.NAME, Database.Column.STAFFRANK, Database.Column.VIPRANK, Database.Column.LANGUAGE, Database.Column.SILENT);

        staffRank = StaffRank.valueOf(values.get(Database.Column.STAFFRANK));
        vipRank = VipRank.valueOf(values.get(Database.Column.VIPRANK));
        language = Language.valueOf(values.get(Database.Column.LANGUAGE));
        silent = Boolean.parseBoolean(values.get(Database.Column.SILENT));

        String name = values.get(Database.Column.NAME);
        if (!name.equals(player.getName()))
            Database.get().update(Database.Table.PLAYERS, new Database.Where(Database.Column.UUID, getUUID().toString()), new Database.Set(Database.Column.NAME, player.getName()));

        updateLastOnline();
    }

    public void onQuit() {
        updateLastOnline();
    }

    public void vote() {
        //TODO VOTE TO SPIGOT
    }

    /* Returns [COLOR][BOLD][RANK] [COLOR][NAME] - NONE; [COLOR][NAME] */
    public String getName() {
        return getChatPrefix() + player.getName();
    }

    /* Returns [COLOR] */
    public String getRankColor() {
        return staffRank == StaffRank.NONE ? vipRank.getColor() : staffRank.getColor();
    }

    /* Returns [COLOR][NAME] */
    public String getColorName() {
        return getRankColor() + player.getName();
    }

    /* Returns [COLOR][BOLD][RANK] [COLOR] - NONE; [COLOR]*/
    public String getChatPrefix() {
        return staffRank == StaffRank.NONE ? vipRank.getPrefix() : staffRank.getPrefix();
    }

    /* Returns [COLOR][BOLD][RANK] [WHITE] - NONE; 'No Rank' */
    public String getScoreboardPrefix() {
        return staffRank == StaffRank.NONE ? vipRank.getScoreboardPrefix() : staffRank.getScoreboardPrefix();
    }

    /* Returns [COLOR][BOLD][RANK] - NONE; 'No Rank' */
    public String getRankString() {
        return staffRank == StaffRank.NONE ? vipRank.getRankString() : staffRank.getRankString();
    }

    /* Getters & Setters */
    public UUID getUUID() {
        return player.getUniqueId();
    }

    public ProxiedPlayer getPlayer() {
        return player;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public StaffRank getStaffRank() {
        return staffRank;
    }

    public void setStaffRank(StaffRank staffRank) {
        this.staffRank = staffRank;
    }

    public VipRank getVipRank() {
        return vipRank;
    }

    public void setVipRank(VipRank vipRank) {
        this.vipRank = vipRank;
    }

    public boolean isSilent() {
        return silent;
    }

    public void setSilent(boolean silent) {
        this.silent = silent;

        Database.get().update(Database.Table.PLAYERS, new Database.Where(Database.Column.UUID, getUUID().toString()), new Database.Set(Database.Column.SILENT, silent + ""));

        //TODO UPDATE ON SPIGOT
    }

    public UUID getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(UUID lastMsg) {
        this.lastMsg = lastMsg;
    }

    public boolean hasLastMsg() {
        return lastMsg != null;
    }

    public boolean showStaffMsg() {
        return showStaffMsg;
    }

    public void setShowStaffMsg(boolean showStaffMsg) {
        this.showStaffMsg = showStaffMsg;
    }

    public boolean allChat() {
        return allChat;
    }

    public void setAllChat(boolean allChat) {
        this.allChat = allChat;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public Login getLogin() {
        return login;
    }

    public boolean isLoggingIn() {
        return login != null;
    }

    /* Other */
    public Server getServer(){
        return ServerUtils.getServer(getServerInfo());
    }

    public ServerInfo getServerInfo() {
        return player.getServer().getInfo();
    }

    public boolean isEligible(StaffRank... staffRanks) {
        if (staffRanks == null || staffRanks.length == 0)
            return true;

        for (StaffRank staffRank : staffRanks) {
            if (this.staffRank == staffRank)
                return true;
        }
        return false;
    }

    public boolean isEligible(VipRank... vipRanks) {
        if (vipRanks == null || vipRanks.length == 0 || isEligible(StaffRank.OWNER))
            return true;

        for (VipRank vipRank : vipRanks) {
            if (this.vipRank == vipRank)
                return true;
        }
        return false;
    }

    public void sendMessage(Message message) {
        player.sendMessage(message.lang(language));
    }

    public void broadcastMessage(Message message) {
        for (BungeePlayer player : players) {
            player.sendMessage(message);
        }
    }

    public void broadcastMessage(StaffRank staffRank, Message message) {
        for (BungeePlayer player : players) {
            if (!player.isEligible(staffRank))
                continue;

            player.sendMessage(message);
        }
    }

    public String getMessage(Message message) {
        return message.lang(language);
    }

    public String statusString(boolean bl) {
        return bl ? getMessage(new Message("§a§lAAN", "§a§lENABLED")) : getMessage(new Message("§c§lUIT", "§c§lDISABLED"));
    }

    public void msgUnknownCommand(String cmd) {
        sendMessage(new Message("§7Die command bestaat niet! (§6" + cmd + "§7). Gebruik §6/help§7 voor hulp.", "§7Unknown command (§6" + cmd + "§7). Use §6/help§7 for help."));
    }

    public void msgRequiredVipRank(VipRank vipRank) {
        sendMessage(new Message("§7Je moet een " + vipRank.getRankString() + " VIP§7 zijn om dit te doen!", "§7You have to be " + (vipRank == VipRank.IRON || vipRank == VipRank.EMERALD ? "an" : "a") + " " + vipRank.getRankString() + " VIP§7 to do this!"));
    }

    public void connect(Server server) {
        switch (server.getStatus()) {

            case ONLINE:
                sendMessage(new Message("§7Verbinden met " + server.getColor() + server.getName() + "§7...", "§7Connecting to " + server.getColor() + server.getName() + "§7..."));
                player.connect(ServerUtils.getServerInfo(server));
                break;
            case OFFLINE:
                sendMessage(new Message("§7De " + server.getColor() + server.getName() + "§7 Server is §4§lOffline§7!", "§7The " + server.getColor() + server.getName() + "§7 Server is §4§lOffline§7!"));
                break;
            case MAINTENANCE:
                if (isEligible(StaffRank.OWNER)) {
                    sendMessage(new Message(server.getColor() + server.getName() + " is in §d§lMaintenance Mode§7! Verbinden forceren...", server.getColor() + server.getName() + " is in §d§lMaintenance Mode§7! Forcing connection..."));
                    player.connect(ServerUtils.getServerInfo(server));
                } else {
                    sendMessage(new Message(server.getColor() + server.getName() + " is in §d§lMaintenance Mode§7!", server.getColor() + server.getName() + " is in §d§lMaintenance Mode§7!"));
                }
                break;
        }
    }

    public void updateLastOnline() {
        IP ip = IP.getIp(getUUID());
        if (ip == null) {
            new IP(getUUID(), player.getAddress().getHostString()).insert();
            return;
        }

        ip.set(player.getAddress().getHostString());
        ip.update();
    }

    public static BungeePlayer getBungeePlayer(ProxiedPlayer player) {
        for (BungeePlayer omp : players) {
           if (omp.getPlayer() == player)
               return omp;
        }
        return null;
    }

    public static List<BungeePlayer> getPlayers() {
        return players;
    }
}
