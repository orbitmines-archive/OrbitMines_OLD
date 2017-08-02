package com.orbitmines.api;

/*
* OrbitMines - @author Fadi Shawki - 7/28/2017
*/
public enum VipRank {

    NONE("§8", ""),
    IRON("§7", "Iron"),
    GOLD("§6", "Gold"),
    DIAMOND("§b", "Diamond"),
    EMERALD("§a", "Emerald");

    private final String color;
    private final String name;

    VipRank(String color, String name) {
        this.color = color;
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        if (this == NONE)
            return color;
        return color + "§l" + name + " " + color;
    }

    public String getScoreboardPrefix() {
        if (this == NONE)
            return "§f";
        return color + "§l" + name + " §f";
    }

    public String getRankString() {
        if (this == NONE)
            return "§fNone";
        return color + "§l" + name;
    }
}
