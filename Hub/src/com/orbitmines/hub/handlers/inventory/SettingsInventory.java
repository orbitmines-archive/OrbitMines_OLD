package com.orbitmines.hub.handlers.inventory;

import com.orbitmines.api.Language;
import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.inventory.OMInventory;
import com.orbitmines.api.spigot.handlers.itembuilders.BannerBuilder;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.playerdata.GadgetData;
import com.orbitmines.api.spigot.utils.ItemUtils;
import com.orbitmines.hub.handlers.HubPlayer;
import com.orbitmines.hub.handlers.playerdata.HubData;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Arrays;

/*
* OrbitMines - @author Fadi Shawki - 9-8-2017
*/
public class SettingsInventory extends OMInventory {

    @Override
    protected boolean onOpen(OMPlayer player) {
        HubPlayer omp = (HubPlayer) player;
        HubData data = omp.hub();

        newInventory(27, "§0§l" + omp.getMessage(new Message("Instellingen", "Settings")));

        {
            ItemBuilder builder = new ItemBuilder(Material.EYE_OF_ENDER, 1, 0, "§3§l" + omp.getMessage(new Message("Spelers", "Players")) + " " + omp.statusString(data.hasPlayersEnabled()));
            if (data.hasPlayersEnabled())
                builder.glow();

            add(10, new ItemInstance(builder.build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer player) {
                    Player p = omp.getPlayer();

                    p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 5, 1);
                    data.setPlayersEnabled(!data.hasPlayersEnabled());
                    p.sendMessage(omp.getMessage(new Message("§3§lSpelers §7staan nu", "§3§lPlayers")) + " " + omp.statusString(data.hasPlayersEnabled()) + "§7.");

                    p.closeInventory();
                }
            });
        }
        {
            ItemBuilder builder = new ItemBuilder(Material.PAPER, 1, 0, "§f§lScoreboard " + omp.statusString(data.hasScoreboardEnabled()));
            if (data.hasScoreboardEnabled())
                builder.glow();

            add(12, new ItemInstance(builder.build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer player) {
                    Player p = omp.getPlayer();

                    p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 5, 1);
                    data.setScoreboardEnabled(!data.hasScoreboardEnabled());
                    p.sendMessage(omp.getMessage(new Message("§f§lScoreboard §7staat nu", "§f§lScoreboard")) + " " + omp.statusString(data.hasScoreboardEnabled()) + "§7.");

                    p.closeInventory();
                }
            });
        }
        {
            GadgetData gadgetData = omp.gadgets();

            ItemBuilder builder = new ItemBuilder(Material.LEASH, 1, 0, "§6§lStacker " + omp.statusString(gadgetData.hasStackerEnabled()));
            if (gadgetData.hasStackerEnabled())
                builder.glow();

            add(14, new ItemInstance(builder.build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer player) {
                    Player p = omp.getPlayer();

                    p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 5, 1);
                    gadgetData.setStackerEnabled(!gadgetData.hasStackerEnabled());
                    p.sendMessage(omp.getMessage(new Message("§6§lStacker §7staat nu", "§6§lStacker")) + " " + omp.statusString(gadgetData.hasStackerEnabled()) + "§7.");

                    p.closeInventory();
                }
            });
        }
        {

            BannerBuilder builder = getBanner(omp.getLanguage());

            add(16, new ItemInstance(OrbitMinesApi.getApi().getNms().customItem().hideFlags(builder.build(), ItemUtils.FLAG_POTIONS)) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer player) {
                    Player p = omp.getPlayer();

                    p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 5, 1);
                    omp.setLanguage(omp.getLanguage().next());
                    omp.sendMessage(new Message("§c§lTaal§7 veranderd in §c§lNederlands§7.", "§7Changed §c§lLanguage§7 to §c§lEnglish§7."));

                    p.closeInventory();
                }
            });
        }
        return true;
    }

    @Override
    protected void onClick(InventoryClickEvent event, OMPlayer omp) {

    }
    
    private BannerBuilder getBanner(Language language) {
        BannerBuilder builder;
        switch (language) {

            case DUTCH:
                builder = new BannerBuilder(DyeColor.WHITE);
                builder.addPattern(DyeColor.BLUE, PatternType.STRIPE_BOTTOM);
                builder.addPattern(DyeColor.RED, PatternType.STRIPE_TOP);
                break;
            case ENGLISH:
                builder = new BannerBuilder(DyeColor.BLUE);
                builder.addPattern(DyeColor.WHITE, PatternType.STRIPE_DOWNLEFT);
                builder.addPattern(DyeColor.WHITE, PatternType.STRIPE_DOWNRIGHT);
                builder.addPattern(DyeColor.RED, PatternType.CROSS);
                builder.addPattern(DyeColor.WHITE, PatternType.STRIPE_CENTER);
                builder.addPattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE);
                builder.addPattern(DyeColor.RED, PatternType.STRAIGHT_CROSS);
                break;
            default:
                return null;
        }

        builder.setDisplayName(new Message("§7§lTaal: §c§lNederlands", "§7§lLanguage: §c§lEnglish").lang(language));
        builder.setLore(Arrays.asList(new Message(
                "§7Verander taal naar §c§lEngels§7.", "§7Change language to §c§lDutch§7.").lang(language),
                "",
                new Message("§7§lLanguage: §c§lDutch", "§7§lTaal: §c§lEngels").lang(language),
                new Message("§7Change language to §c§lEnglish§7.", "§7Verander taal naar §c§lNederlands§7.").lang(language)));

        return builder;
    }
}
