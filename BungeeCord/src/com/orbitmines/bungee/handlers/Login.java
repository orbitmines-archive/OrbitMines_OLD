package com.orbitmines.bungee.handlers;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 6-8-2017
*/
public class Login {

    private static List<Login> logins = new ArrayList<>();

    private final BungeePlayer player;
    private final String password;
    private int loginTime;

    public Login(BungeePlayer player, String password) {
        logins.add(this);

        this.player = player;
        this.password = password;
        this.loginTime = 20;
    }

    public BungeePlayer getPlayer() {
        return player;
    }

    public String getPassword() {
        return password;
    }

    public int getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(int loginTime) {
        this.loginTime = loginTime;
    }

    public boolean hasPassword(){
        return password != null;
    }

    public void tickLoginTimer(){
        this.loginTime -= 1;
    }

    public void remove() {
        logins.remove(this);
        player.setLogin(null);
    }

    public static List<Login> getLogins() {
        return logins;
    }
}
