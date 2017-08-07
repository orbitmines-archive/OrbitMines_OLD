package com.orbitmines.bungee.handlers;

import com.orbitmines.api.Database;
import com.orbitmines.api.BungeeDatabaseType;
import com.orbitmines.bungee.OrbitMinesBungee;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 6-8-2017
*/
public class MotdHandler {

    private OrbitMinesBungee bungee;
    private String firstLine;
    private List<String> secondLines;

    public MotdHandler() {
        bungee = OrbitMinesBungee.getBungee();
        secondLines = new ArrayList<>();

        if (!Database.get().contains(Database.Table.BUNGEE, Database.Column.DATA, new Database.Where(Database.Column.TYPE, BungeeDatabaseType.MOTD.toString()))) {
            firstLine = "§6§lOrbitMines§4§lNetwork §7- Message of the day!";
            secondLines.add("§7Hello world!");
            return;
        }

        String[] motdData = Database.get().getString(Database.Table.BUNGEE, Database.Column.DATA, new Database.Where(Database.Column.TYPE, BungeeDatabaseType.MOTD.toString())).split("|");

        /* FirstLine|SecondLine~SecondLine~SecondLine */
        firstLine = ChatColor.translateAlternateColorCodes('&', motdData[0]).replace("`", "'");

        for (String secondLine : motdData[1].split("~")) {
            secondLines.add(ChatColor.translateAlternateColorCodes('&', secondLine).replace("`", "'"));
        }
    }

    public String getFirstLine() {
        return firstLine;
    }

    public String getColor() {
        return bungee.isInMaintenance() ? "§d" : "§e";
    }

    public List<String> getSecondLines() {
        if (bungee.isInMaintenance())
            return Collections.singletonList("§dWe zijn wat aan het veranderen op de server!");
        else if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY)
            return secondLines;

        List<String> secondLines = new ArrayList<>(this.secondLines);
        secondLines.add("'§cFree Kit Saturday§e' op §cKitPvP§e! (Gratis Kits)");
        return secondLines;
    }

    public void addLine(String line) {
        secondLines.add(line);

        update();
    }

    public void removeLine(int index) {
        secondLines.remove(index);

        update();
    }

    public void update() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(firstLine.replace("§", "&"));
        stringBuilder.append("|");
        for (String secondLine : secondLines) {
            stringBuilder.append(secondLine.replace("§", "&").replace("'", "`"));
        }

        Database.get().update(Database.Table.BUNGEE, new Database.Where(Database.Column.TYPE, BungeeDatabaseType.MOTD.toString()), new Database.Set(Database.Column.DATA, stringBuilder.toString().substring(0, stringBuilder.length() -1)));
    }
}
