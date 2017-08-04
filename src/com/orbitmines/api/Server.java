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
    private int onlinePlayers;
    private int maxPlayers;
    private Status status;

    Server(String color, String name) {
        this.color = color;
        this.name = name;
        this.onlinePlayers = 0;
        this.maxPlayers = -1;
        this.status = Status.OFFLINE;
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public int getOnlinePlayers() {
        return onlinePlayers;
    }

    public void setOnlinePlayers(int onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
    }

    public int getMaxPlayers() {
        if (maxPlayers == -1)
            maxPlayers = Database.get().getInt(Database.Table.SERVERS, Database.Column.MAXPLAYERS, new Database.Where(Database.Column.NAME, toString()));

        return maxPlayers;
    }

    public Status getStatus() {
        return status;
    }

    public void updateStatus() {
        status = Status.valueOf(Database.get().getString(Database.Table.SERVERS, Database.Column.STATUS, new Database.Where(Database.Column.NAME, toString())));
    }

    public boolean is(Status status) {
        return this.status == status;
    }

    public enum Status {

        ONLINE("§a", "Online"),
        OFFLINE("§c", "Offline"),
        MAINTENANCE("§d", "Maintenance");

        private final String color;
        private final String name;

        Status(String color, String name) {
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
}
