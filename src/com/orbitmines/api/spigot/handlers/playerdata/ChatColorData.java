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
    private ChatColorType chatColorType;
    private List<ChatColorType> chatColorTypes;

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
        String chatColorsString = null;
        if (chatColors.size() != 0){
            StringBuilder stringBuilder = new StringBuilder();
            for (ChatColor chatColor : chatColors) {
                stringBuilder.append(chatColor.toString());
                stringBuilder.append("=");
            }
            chatColorsString = stringBuilder.toString().substring(0, stringBuilder.length() -1);
        }

        String chatColorTypesString = null;
        if (chatColorTypes.size() != 0){
            StringBuilder stringBuilder = new StringBuilder();
            for (ChatColorType chatColorType : chatColorTypes) {
                stringBuilder.append(chatColorType.toString());
                stringBuilder.append("=");
            }
            chatColorTypesString = stringBuilder.toString().substring(0, stringBuilder.length() -1);
        }

        return chatColor.toString() + "-" + chatColorsString + "-" + (chatColorType == null ? "null" : chatColorType.toString()) + "-" + chatColorTypesString;
    }

    @Override
    public void parse(String string) {
        String[] data = string.split("-");

        chatColor = ChatColor.valueOf(data[0]);

        if (!data[1].equals("null")) {
            for (String chatColor : data[1].split("=")) {
                chatColors.add(ChatColor.valueOf(chatColor));
            }
        }

        chatColorType = data[2].equals("null") ? null : ChatColorType.valueOf(data[2]);

        if (!data[3].equals("null")) {
            for (String chatColorType : data[3].split("=")) {
                chatColorTypes.add(ChatColorType.valueOf(chatColorType));
            }
        }
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

        omp.updateStats();
    }

    public boolean hasChatColor(ChatColor chatColor) {
        return getChatColors().contains(chatColor);
    }

    public ChatColorType getChatColorType() {
        return chatColorType;
    }

    public void setChatColorType(ChatColorType chatColorType) {
        omp.getPlayer().playSound(omp.getPlayer().getLocation(), Sound.BLOCK_PISTON_EXTEND, 5, 1);

        if (chatColorType != null)
            omp.sendMessage(new Message("§7Je " + chatColorType.getDisplayName() + "§7 staat nu §a§lAAN§7.", "§a§lENABLED §7your " + chatColorType.getDisplayName() + "§7."));
        else
            omp.sendMessage(new Message("§7Je " + chatColorType.getDisplayName() + "§7 staat nu §c§lUIT§7.", "§c§lDISABLED §7your " + chatColorType.getDisplayName() + "§7."));

        this.chatColorType = chatColorType;
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

        omp.updateStats();
    }

    public boolean hasChatColorType(ChatColorType chatColorType) {
        return getChatColorTypes().contains(chatColorType);
    }

    public String type() {
        return chatColorType == null ? "" : chatColorType.getType();
    }
}
