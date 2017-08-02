package com.orbitmines.api.spigot.handlers.playerdata;

import com.orbitmines.api.Data;
import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.perks.ChatColor;
import com.orbitmines.api.spigot.perks.ChatColorType;
import org.bukkit.Sound;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 30-7-2017
*/
public class ChatColorData extends PlayerData {

    private ChatColor chatColor;
    private List<ChatColor> chatColors;
    private List<ChatColorType> chatColorTypes;
    private ChatColorType chatColorType;

    public ChatColorData(OMPlayer omp) {
        super(Data.CHATCOLORS, omp);

        /* Load Defaults */
        chatColor = ChatColor.GRAY;
        chatColors = new ArrayList<>();
        this.chatColorTypes = new ArrayList<>();
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

    public ChatColor getChatColor() {
        return chatColor;
    }

    public void setChatColor(ChatColor chatColor) {
        this.chatColor = chatColor;

        omp.getPlayer().playSound(omp.getPlayer().getLocation(), Sound.BLOCK_PISTON_EXTEND, 5, 1);
        omp.sendMessage(new Message("§7Je " + chatColor.getDisplayName() + "§7 staat nu §a§lAAN§7.", "§a§lENABLED §7your " + chatColor.getDisplayName() + "§7."));
    }

    public List<ChatColor> getChatColors() {
        List<ChatColor> list = new ArrayList<>(this.chatColors);
        for (ChatColor value : ChatColor.values()) {
            if (value.obtainable().getVipRank() != null && omp.isEligible(value.obtainable().getVipRank()))
                list.add(value);
        }

        return list;
    }

    public void addChatColor(ChatColor chatColor) {
        chatColors.add(chatColor);
    }

    public boolean hasChatColor(ChatColor chatColor) {
        return getChatColors().contains(chatColor);
    }

    public ChatColorType getChatColorType() {
        return chatColorType;
    }

    public void setChatColorType(ChatColorType chatColorType) {
        this.chatColorType = chatColorType;

        omp.getPlayer().playSound(omp.getPlayer().getLocation(), Sound.BLOCK_PISTON_EXTEND, 5, 1);

        if (chatColorType != null)
            omp.sendMessage(new Message("§7Je " + chatColorType.getDisplayName() + "§7 staat nu §a§lAAN§7.", "§a§lENABLED §7your " + chatColorType.getDisplayName() + "§7."));
        else
            omp.sendMessage(new Message("§7Je " + chatColorType.getDisplayName() + "§7 staat nu §c§lUIT§7.", "§c§lDISABLED §7your " + chatColorType.getDisplayName() + "§7."));
    }

    public List<ChatColorType> getChatColorTypes() {
        List<ChatColorType> list = new ArrayList<>(this.chatColorTypes);
        for (ChatColorType value : ChatColorType.values()) {
            if (value.obtainable().getVipRank() != null && omp.isEligible(value.obtainable().getVipRank()))
                list.add(value);
        }

        return list;
    }

    public void addChatColorType(ChatColorType chatColorType) {
        chatColorTypes.add(chatColorType);
    }

    public boolean hasChatColorType(ChatColorType chatColorType) {
        return getChatColorTypes().contains(chatColorType);
    }
}
