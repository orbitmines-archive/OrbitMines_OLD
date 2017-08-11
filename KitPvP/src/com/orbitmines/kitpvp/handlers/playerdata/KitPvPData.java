package com.orbitmines.kitpvp.handlers.playerdata;

import com.orbitmines.api.Data;
import com.orbitmines.api.Database;
import com.orbitmines.api.Message;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.playerdata.PlayerData;
import com.orbitmines.kitpvp.handlers.KitHandler;
import com.orbitmines.kitpvp.handlers.KitPvPKit;
import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import org.bukkit.Sound;

import java.util.HashMap;
import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public class KitPvPData extends PlayerData {

    private KitPvPPlayer omp;

    private int coins;

    private int kills;
    private int deaths;

    private int level;
    private int exp;

    private int bestStreak;

    private KitPvPKit.Type lastSelected;
    private int lastSelectedLevel;

    private Map<KitPvPKit.Type, Integer> unlockedKits;

    public KitPvPData(OMPlayer omp) {
        super(Data.KITPVP, omp);

        this.omp = (KitPvPPlayer) omp;

        unlockedKits = new HashMap<>();
        unlockedKits.put(KitPvPKit.Type.KNIGHT, 1);
        unlockedKits.put(KitPvPKit.Type.ARCHER, 1);
        unlockedKits.put(KitPvPKit.Type.SOLDIER, 1);
        lastSelected = KitPvPKit.Type.KNIGHT;
        lastSelectedLevel = 1;
    }

    @Override
    public void onLogin() {
        updateLevel();
    }

    @Override
    public void onLogout() {

    }

    @Override
    public String serialize() {
        String kitsString = null;
        if (unlockedKits.size() != 0){
            StringBuilder stringBuilder = new StringBuilder();
            for (KitPvPKit.Type type : unlockedKits.keySet()) {
                stringBuilder.append(type.toString());
                stringBuilder.append(">");
                stringBuilder.append(unlockedKits.get(type));
                stringBuilder.append("=");
            }
            kitsString = stringBuilder.toString().substring(0, stringBuilder.length() -1);
        }

        return coins + "-" + deaths + "-" + level + "-" + exp + "-" + bestStreak + "-" + lastSelected.toString() + "-" + lastSelectedLevel + "-" + kitsString;
    }

    @Override
    public void parse(String string) {
        String[] data = string.split("-");

        kills = Database.get().getInt(Database.Table.PLAYERS, Database.Column.KITPVPKILLS, new Database.Where(Database.Column.UUID, omp.getUUID().toString()));

        coins = Integer.parseInt(data[0]);
        deaths = Integer.parseInt(data[1]);
        level = Integer.parseInt(data[2]);
        exp = Integer.parseInt(data[3]);
        bestStreak = Integer.parseInt(data[4]);
        lastSelected = KitPvPKit.Type.valueOf(data[5]);
        lastSelectedLevel = Integer.parseInt(data[6]);

        if (!data[7].equals("null")) {
            for (String kitData : data[7].split("=")) {
                String[] kit = kitData.split(">");

                unlockedKits.put(KitPvPKit.Type.valueOf(kit[0]), Integer.parseInt(kit[1]));
            }
        }
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills){
        this.kills = kills;

        Database.get().update(Database.Table.PLAYERS, new Database.Where(Database.Column.UUID, omp.getUUID().toString()), new Database.Set(Database.Column.KITPVPKILLS, kills + ""));
    }

    public void addKill(){
        this.kills += 1;

        Database.get().update(Database.Table.PLAYERS, new Database.Where(Database.Column.UUID, omp.getUUID().toString()), new Database.Set(Database.Column.KITPVPKILLS, kills + ""));
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths){
        this.deaths = deaths;
    }

    public void addDeath(){
        this.deaths += 1;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level){
        this.level = level;
    }

    public void addLevel(){
        this.level += 1;

        omp.getPlayer().playSound(omp.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 5, 1);
        omp.getPlayer().sendMessage("§a§lLevel Up! §7(§c§l+ 1 Mastery Point§7)");

        MasteriesData data = omp.masteries();
        data.addPoints(1);
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp){
        this.exp = exp;
    }

    public void addExp(int exp){
        this.exp += exp * omp.getExpBooster();
    }

    public int getCoins() {
        return coins;
    }

    public boolean hasCoins(int coins) {
        return this.coins >= coins;
    }

    public void setCoins(int coins){
        this.coins = coins;

        omp.updateStats();
    }

    public void addCoins(int coins){
        this.coins += coins;

        omp.updateStats();
    }

    public void removeCoins(int coins){
        this.coins -= coins;

        omp.updateStats();
    }

    public int getBestStreak() {
        return bestStreak;
    }

    public void setBestStreak(int bestStreak) {
        this.bestStreak = bestStreak;
    }

    public KitPvPKit.Type getLastSelected() {
        return lastSelected;
    }

    public void setLastSelected(KitPvPKit.Type lastSelected) {
        this.lastSelected = lastSelected;
    }

    public int getLastSelectedLevel() {
        return lastSelectedLevel;
    }

    public void setLastSelectedLevel(int lastSelectedLevel) {
        this.lastSelectedLevel = lastSelectedLevel;
    }

    public KitHandler getLastKitHandler() {
        return lastSelected.getKit().getHandlers()[lastSelectedLevel -1];
    }

    public Map<KitPvPKit.Type, Integer> getUnlockedKits() {
        return unlockedKits;
    }

    public void setUnlockedKitLevel(KitPvPKit.Type type, int level) {
        this.unlockedKits.put(type, level);

        omp.updateStats();
    }

    public int getUnlockedLevel(KitPvPKit.Type type) {
        int level =  unlockedKits.containsKey(type) ? unlockedKits.get(type) : 0;

        if (level != 0)
            return level;

        VipRank vipRank = type.getKit().getHandlers()[0].obtainable().getVipRank();
        if (vipRank == null)
            return level;

        return omp.isEligible(vipRank) ? 1 : level;
    }

    public double getExpRequired() {
        int level = this.level + 1;
        return ((level * 10) / 3 + level * level) * 3;
    }

    public boolean isLevelUp() {
        return getExp() >= getExpRequired();
    }

    public void updateLevel() {
        omp.getPlayer().setLevel(this.level);
        omp.getPlayer().setExp((float) getExp() / (float) getExpRequired());
    }

    public void msgRequiredCoins(int price) {
        omp.getPlayer().playSound(omp.getPlayer().getLocation(), Sound.ENTITY_ENDERMEN_SCREAM, 5, 1);
        int needed = price - coins;
        String multiple = needed == 1 ? "Coin" : "Coins";

        omp.sendMessage(new Message("§7Je hebt nog §6§l" + needed + " " + multiple + "§7 nodig!", "§7You need §6§l" + needed + "§7 more §6§l" + multiple + "§7!"));
    }
}
