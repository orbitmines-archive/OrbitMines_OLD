package com.orbitmines.api.spigot.handlers.inventory.perks;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.ItemSet;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.inventory.OMInventory;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.playerdata.ChatColorData;
import com.orbitmines.api.spigot.perks.ChatColor;
import com.orbitmines.api.spigot.perks.ChatColorType;
import org.bukkit.event.inventory.InventoryClickEvent;

/*
* OrbitMines - @author Fadi Shawki - 30-7-2017
*/
public class ChatColorInventory extends PerkInventory {

    public ChatColorInventory() {
        super(45, "§0§lChatColors", 39);
    }

    @Override
    protected void setPerkItems(OMPlayer omp) {
        ChatColorData data = omp.chatcolors();

        int slot = 9;
        for (ChatColor chatColor : ChatColor.values()) {
            if (slot == 13 || slot == 22)
                slot++;

            add(slot, new ConfirmItemInstance(toItemBuilder(chatColor, omp).build()) {

                @Override
                protected void onConfirm(InventoryClickEvent event, OMPlayer omp) {
                    data.addChatColor(chatColor);
                }

                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    if (!chatColor.hasAccess(omp)) {
                        if (chatColor.obtainable().isPurchasable() && chatColor.obtainable().canPurchase(omp)) {
                            confirmPurchase(omp, chatColor, this);
                        } else {
                            chatColor.obtainable().msgNoAccess(omp);
                        }
                    } else {
                        omp.getPlayer().closeInventory();
                        data.setChatColor(chatColor);
                    }
                }
            });

            slot++;
        }

        slot = 13;
        for (ChatColorType chatColorType : ChatColorType.values()) {
            add(slot, new ConfirmItemInstance(new ItemBuilder(chatColorType.item().getMaterial(), chatColorType.item().getAmount(), data.getChatColorType() == chatColorType ? 5 : 14, chatColorType.getDisplayName() + ": " + omp.statusString(data.getChatColorType() == chatColorType),
                    "", chatColorType.hasAccess(omp) ? "§a§l" + omp.getMessage(new Message("Ontgrendeld", "Unlocked")) : chatColorType.obtainable().getRequiredLore(omp), "").build()) {
                @Override
                protected void onConfirm(InventoryClickEvent event, OMPlayer omp) {
                    data.addChatColorType(chatColorType);
                }

                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    if (!chatColorType.hasAccess(omp)) {
                        if (chatColorType.obtainable().isPurchasable() && chatColorType.obtainable().canPurchase(omp)) {
                            confirmPurchase(omp, chatColorType, this);
                        } else {
                            chatColorType.obtainable().msgNoAccess(omp);
                        }
                    } else {
                        omp.getPlayer().closeInventory();

                        if (data.getChatColorType() == chatColorType)
                            data.setChatColorType(null);
                        else
                            data.setChatColorType(chatColorType);
                    }
                }
            });

            slot += 9;
        }

        ItemSet set = data.getChatColor().item();
        add(41, new EmptyItemInstance(new ItemBuilder(set.getMaterial(), set.getAmount(), set.getDurability(), data.getChatColor().color().getDisplayName()).build()));
    }

    @Override
    protected OMInventory returnInventory() {
        return new ChatColorInventory();
    }

    @Override
    protected boolean isDisabled() {
        return false;
    }
}
