package com.orbitmines.bungee.handlers;

import com.orbitmines.api.Database;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

/*
* OrbitMines - @author Fadi Shawki - 6-8-2017
*/
public class IP {

    private static List<IP> ips = new ArrayList<>();

    private final UUID uuid;
    private String lastIp;
    private String lastLogin;
    private List<String> allIps;

    public IP(UUID uuid, String lastIp) {
        ips.add(this);

        this.uuid = uuid;
        this.lastIp = lastIp;
        this.lastLogin = getDate();
        this.allIps = new ArrayList<>(Collections.singletonList(lastIp));
    }

    public IP(UUID uuid, String lastIp, String lastLogin, List<String> allIps) {
        ips.add(this);

        this.uuid = uuid;
        this.lastIp = lastIp;
        this.lastLogin = lastLogin;
        this.allIps = allIps;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getLastIp() {
        return lastIp;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public List<String> getAllIps() {
        return allIps;
    }

    public void set(String ip) {
        lastIp = ip;
        lastLogin = getDate();

        if (!allIps.contains(ip))
            allIps.add(ip);
    }

    private String getDate() {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date(Calendar.getInstance().getTimeInMillis()));
    }

    private String serializeHistory() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String ip : allIps) {
            stringBuilder.append(ip);
            stringBuilder.append("|");
        }
        return stringBuilder.toString().substring(0, stringBuilder.length() -1);
    }

    public void insert() {
        Database.get().insert(Database.Table.IPS, Database.get().values(uuid.toString(), lastIp, lastLogin, serializeHistory()));
    }

    public void update() {
        Database.get().update(Database.Table.IPS, new Database.Where(Database.Column.UUID, uuid.toString()), new Database.Set(Database.Column.LASTIP, lastIp), new Database.Set(Database.Column.LASTLOGIN, lastLogin), new Database.Set(Database.Column.HISTORY, serializeHistory()));
    }

    public static IP getIp(UUID uuid) {
        for (IP ip : ips) {
            if (ip.getUuid().toString().equals(uuid.toString()))
                return ip;
        }
        return null;
    }

    public static List<IP> getIpInfo(String ipString) {
        List<IP> ipList = new ArrayList<>();
        for (IP ip : ips) {
            for (String address : ip.getAllIps()) {
                if (address.equals(ipString)) {
                    ipList.add(ip);
                    break;
                }
            }
        }
        return ipList;
    }

    public static List<IP> getIps() {
        return ips;
    }
}
