package com.orbitmines.api.spigot.handlers.playerdata;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.orbitmines.api.Data;
import com.orbitmines.api.Language;
import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.chat.Title;
import org.bukkit.Sound;

/*
* OrbitMines - @author Fadi Shawki - 30-7-2017
*/
public class GeneralData extends PlayerData {

    private Language language;

    private boolean silent;

    private int vipPoints;
    private int orbitMinesTokens;

    private String nickName;

    public GeneralData(OMPlayer omp) {
        super(Data.GENERAL, omp);

        /* Load Defaults */
        language = Language.DUTCH;
        silent = false;
    }

    @Override
    public void onLogin() {

    }

    @Override
    public void onLogout() {

    }

    @Override
    public String serialize() {
        return null;
    }

    @Override
    public void parse(String string) {

    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;

        /* Also update on Bungeecord */
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("SetLanguage");
        out.writeUTF(language.toString());
        out.writeUTF(omp.getPlayer().getName());

        omp.getPlayer().sendPluginMessage(api, "OrbitMinesApi", out.toByteArray());
    }

    public boolean isSilent() {
        return silent;
    }

    public void setSilent(boolean silent) {
        this.silent = silent;
    }

    public int getVipPoints() {
        return vipPoints;
    }

    public void setVipPoints(int vipPoints) {
        this.vipPoints = vipPoints;
    }

    public void addVipPoints(int vipPoints) {
        this.vipPoints += vipPoints;

        new Title("", "§b+" + vipPoints + " VIP Points", 20, 40, 20).send(omp.getPlayer());
    }

    public void removeVipPoints(int vipPoints) {
        this.vipPoints -= vipPoints;
    }

    public boolean hasVipPoints(int vipPoints) {
        return this.vipPoints >= vipPoints;
    }

    public int getTokens() {
        return orbitMinesTokens;
    }

    public void setTokens(int orbitMinesTokens) {
        this.orbitMinesTokens = orbitMinesTokens;
    }

    public void addTokens(int orbitMinesTokens) {
        this.orbitMinesTokens += orbitMinesTokens;
    }

    public void removeTokens(int orbitMinesTokens) {
        this.orbitMinesTokens -= orbitMinesTokens;
    }

    public boolean hasTokens(int orbitMinesTokens) {
        return this.orbitMinesTokens >= orbitMinesTokens;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void msgRequiredVipPoints(int price) {
        omp.getPlayer().playSound(omp.getPlayer().getLocation(), Sound.ENTITY_ENDERMEN_SCREAM, 5, 1);
        int needed = price - vipPoints;
        String multiple = needed == 1 ? "VIP Point" : "VIP Points";

        omp.sendMessage(new Message("§7Je hebt nog §b§l" + needed + " " + multiple + "§7 nodig!", "§7You need §b§l" + needed + "§7 more §b§l" + multiple + "§7!"));
    }

    public void msgRequiredTokens(int price) {
        omp.getPlayer().playSound(omp.getPlayer().getLocation(), Sound.ENTITY_ENDERMEN_SCREAM, 5, 1);
        int needed = price - orbitMinesTokens;
        String multiple = needed == 1 ? "OrbitMines Token" : "OrbitMines Tokens";

        omp.sendMessage(new Message("§7Je hebt nog §e§l" + needed + " " + multiple + "§7 nodig!", "§7You need §e§l" + needed + "§7 more §e§l" + multiple + "§7!"));
    }
}
