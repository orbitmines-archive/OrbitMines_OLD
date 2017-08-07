package com.orbitmines.bungee.handlers;

import com.orbitmines.api.Database;
import com.orbitmines.api.Language;
import com.orbitmines.api.Message;
import com.orbitmines.api.utils.uuid.UUIDUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/*
* OrbitMines - @author Fadi Shawki - 7-8-2017
*/
public class Ban {

    private static List<Ban> bans = new ArrayList<>();
    private static List<Ban> expiredBans = new ArrayList<>();

    private final UUID uuid;
    private final String ip;

    private final UUID bannedBy;
    private final String bannedOn;
    private final String bannedUntil;
    private final String reason;

    public Ban(UUID uuid, boolean active, UUID bannedBy, String bannedOn, String bannedUntil, String reason) {
        this(uuid, null, active, bannedBy, bannedOn, bannedUntil, reason);
    }

    public Ban(String ip, boolean active, UUID bannedBy, String bannedOn, String bannedUntil, String reason) {
        this(null, ip, active, bannedBy, bannedOn, bannedUntil, reason);
    }

    public Ban(UUID uuid, String ip, boolean active, UUID bannedBy, String bannedOn, String bannedUntil, String reason) {
        if (active && !hasExpired())
            bans.add(this);
        else
            expiredBans.add(this);

        this.uuid = uuid;
        this.ip = ip;
        this.bannedBy = bannedBy;
        this.bannedOn = bannedOn;
        this.bannedUntil = bannedUntil;
        this.reason = reason;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getIp() {
        return ip;
    }

    public UUID getBannedBy() {
        return bannedBy;
    }

    public String getBannedOn() {
        return bannedOn;
    }

    public String getBannedUntil() {
        return bannedUntil;
    }

    public String getReason() {
        return reason;
    }

    public String getMessage(Language language) {
        String playerName = UUIDUtils.getName(bannedBy);
        return new Message("§cJe bent §4§lGEBANNED§c! (Door §b" + playerName + "§c)\n§cUnban op: §6§l" + bannedUntil + "\n§cReden: §6§l" + reason, "§cYou've been §4§lBANNED§c! (By §b" + playerName + "§c)\n§cUnban on: §6§l" + bannedUntil + "\n§cReason: §6§l" + reason).lang(language);
    }

    public boolean hasExpired() {
        boolean expired;
        try {
            expired = new Date(Calendar.getInstance().getTimeInMillis()).compareTo(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(bannedUntil)) >= 0;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        if (expired)
            unBan();

        return expired;
    }

    public void unBan() {
        Database.get().update(Database.Table.BANS, new Database.Where(Database.Column.BANNED, uuid != null ? uuid.toString() : ip), new Database.Set(Database.Column.ACTIVE, "" + false));

        if (bans.contains(this)) {
            bans.remove(this);
            expiredBans.add(this);
        }
    }

    public static Ban getBan(UUID uuid) {
        return getBan(uuid, null);
    }

    public static Ban getBan(String ip) {
        return getBan(null, ip);
    }

    public static Ban getBan(UUID uuid, String ip) {
        for (Ban ban : bans) {
            if (uuid != null && ban.getUuid().toString().equals(uuid.toString()) || ip != null && ban.getIp().equals(ip))
                return ban;
        }
        return null;
    }

    public static List<Ban> getExpiredBans(UUID uuid) {
        return getExpiredBans(uuid, null);
    }

    public static List<Ban> getExpiredBans(String ip) {
        return getExpiredBans(null, ip);
    }

    public static List<Ban> getExpiredBans(UUID uuid, String ip) {
        List<Ban> bans = new ArrayList<>();
        for (Ban ban : expiredBans) {
            if (uuid != null && ban.getUuid().toString().equals(uuid.toString()) || ip != null && ban.getIp().equals(ip))
                bans.add(ban);
        }
        return null;
    }
}
