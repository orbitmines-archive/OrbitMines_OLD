package com.orbitmines.api;

/*
* OrbitMines - @author Fadi Shawki - 7/20/2017
*/
public enum Server {

    KITPVP("§c", "KitPvP"),
    PRISON("§4", "Prison"),
    CREATIVE("§d", "Creative"),
    HUB("§3", "Hub"),
    SURVIVAL("§a", "Survival"),
    SKYBLOCK("§5", "SkyBlock"),
    FOG("§e", "FoG"),
    MINIGAMES("§f", "MiniGames");

    private final String color;
    private final String name;

    Server(String color, String name) {
        this.color = color;
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }
}
