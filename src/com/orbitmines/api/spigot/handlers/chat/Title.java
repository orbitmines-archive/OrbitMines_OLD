package com.orbitmines.api.spigot.handlers.chat;

import com.orbitmines.api.spigot.OrbitMinesApi;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

/**
 * Created by Fadi on 30-4-2016.
 */
public class Title {

    private String title;
    private String subTitle;
    private int fadeIn;
    private int stay;
    private int fadeOut;

    public Title(String title, String subTitle, int fadeIn, int stay, int fadeOut){
        this.title = title;
        this.subTitle = subTitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    public void send(Player player){
        OrbitMinesApi.getApi().getNms().title().send(player, this);
    }

    public void send(Player... players){
        for(Player player : players){
            send(player);
        }
    }

    public void send(List<Player> players){
        for(Player player : players){
            send(player);
        }
    }

    public void send(Collection<? extends Player> players){
        for(Player player : players){
            send(player);
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public int getFadeIn() {
        return fadeIn;
    }

    public void setFadeIn(int fadeIn) {
        this.fadeIn = fadeIn;
    }

    public int getStay() {
        return stay;
    }

    public void setStay(int stay) {
        this.stay = stay;
    }

    public int getFadeOut() {
        return fadeOut;
    }

    public void setFadeOut(int fadeOut) {
        this.fadeOut = fadeOut;
    }

    public Title copy(){
        return new Title(title, subTitle, fadeIn, stay, fadeOut);
    }
}
