package com.orbitmines.kitpvp.handlers.inventory;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.handlers.ItemSet;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.Obtainable;
import com.orbitmines.api.spigot.handlers.inventory.ConfirmInventory;
import com.orbitmines.api.spigot.handlers.inventory.OMInventory;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.utils.ItemUtils;
import com.orbitmines.kitpvp.KitPvP;
import com.orbitmines.kitpvp.Mastery;
import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import com.orbitmines.kitpvp.handlers.playerdata.MasteriesData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 11-8-2017
*/
@Deprecated
public class MasteryInventory extends OMInventory {

    private KitPvP kitPvP;

    public MasteryInventory() {
        newInventory(54, "§0§lMasteries");

        kitPvP = KitPvP.getInstance();
    }

    @Override
    protected boolean onOpen(OMPlayer player) {
        KitPvPPlayer omp = (KitPvPPlayer) player;
        Player p = omp.getPlayer();
        MasteriesData m = omp.masteries();

        int index = 0;
        for (Mastery mastery : Mastery.values()) {
            {
                ItemStack item = getUpgradeItem(omp, mastery);

                if (item != null)
                    add(1 + (index * 2), new ItemInstance(item) {
                        @Override
                        public void onClick(InventoryClickEvent event, OMPlayer omp) {
                            switch(item.getItemMeta().getDisplayName()){
                                case "§a§l+2% Melee Damage":
                                    if(m.getPoints() > 0){
                                        m.setPoints(m.getPoints() -1);
                                        m.setMelee(m.getMelee() +1);

                                        new MasteryInventory().open(omp);
                                        p.sendMessage("");
                                        p.sendMessage("§7Item Bought: " + item.getItemMeta().getDisplayName() + "§7.");
                                        p.sendMessage("§7Price: §c§l1 Mastery Point§7.");
                                    }
                                    break;
                                case "§c§l-2% Melee Damage Taken":
                                    if(m.getPoints() > 0){
                                        m.setPoints(m.getPoints() -1);
                                        m.setMeleeProtection(m.getMeleeProtection() +1);

                                        new MasteryInventory().open(omp);
                                        p.sendMessage("");
                                        p.sendMessage("§7Item Bought: " + item.getItemMeta().getDisplayName() + "§7.");
                                        p.sendMessage("§7Price: §c§l1 Mastery Point§7.");
                                    }
                                    break;
                                case "§a§l+4% Range Damage":
                                    if(m.getPoints() > 0){
                                        m.setPoints(m.getPoints() -1);
                                        m.setRange(m.getRange() +1);

                                        new MasteryInventory().open(omp);
                                        p.sendMessage("");
                                        p.sendMessage("§7Item Bought: " + item.getItemMeta().getDisplayName() + "§7.");
                                        p.sendMessage("§7Price: §c§l1 Mastery Point§7.");
                                    }
                                    break;
                                case "§c§l-3% Range Damage Taken":
                                    if(m.getPoints() > 0){
                                        m.setPoints(m.getPoints() -1);
                                        m.setRangeProtection(m.getRangeProtection() +1);

                                        new MasteryInventory().open(omp);
                                        p.sendMessage("");
                                        p.sendMessage("§7Item Bought: " + item.getItemMeta().getDisplayName() + "§7.");
                                        p.sendMessage("§7Price: §c§l1 Mastery Point§7.");
                                    }
                                    break;
                            }
                        }
                    });
            }

            add(19 + (index * 2), new EmptyItemInstance(getItem(omp, mastery)));

            {
                ItemStack item = getDowngradeItem(omp, mastery);

                if (item != null)
                    add(37 + (index * 2), new ItemInstance(item) {
                        @Override
                        public void onClick(InventoryClickEvent event, OMPlayer omp) {
                            if (m.getMasteryLevel(mastery) > 0) {
                                new ConfirmInventory(mastery.getName(), new ItemSet(mastery.getMaterial()), new Obtainable(OrbitMinesApi.VIP_POINTS, 10)) {

                                    @Override
                                    public void onConfirm(InventoryClickEvent event, OMPlayer omp) {
                                        obtainable.purchase(omp);
                                        switch (mastery) {

                                            case MELEE:
                                                m.setMelee(m.getMelee() - 1);
                                                break;
                                            case MELEE_PROTECTION:
                                                m.setMeleeProtection(m.getMeleeProtection() - 1);
                                                break;
                                            case RANGE:
                                                m.setRange(m.getRange() - 1);
                                                break;
                                            case RANGE_PROTECTION:
                                                m.setRangeProtection(m.getRangeProtection() - 1);
                                                break;
                                        }
                                        m.addPoints(1);
                                        new MasteryInventory().open(omp);
                                    }

                                    @Override
                                    public void onCancel(InventoryClickEvent event, OMPlayer omp) {
                                        new MasteryInventory().open(omp);
                                    }
                                };
                            }
                        }
                    });
            }

            index++;
        }

        add(49, new EmptyItemInstance(new ItemBuilder(Material.EXP_BOTTLE, m.getPoints() > 64 ? 64 : (m.getPoints() == 0 ? 1 : m.getPoints()), 0, "§7Mastery Points: §c" + m.getPoints()).build()));

        return true;
    }

    @Override
    protected void onClick(InventoryClickEvent event, OMPlayer omp) {

    }

    private ItemStack getItem(KitPvPPlayer omp, Mastery mastery) {
        MasteriesData masteries = omp.masteries();

        int level = masteries.getMasteryLevel(mastery);

        ItemStack item = new ItemStack(mastery.getMaterial(), level == 0 ? 1 : level);
        ItemMeta itemmeta = item.getItemMeta();
        itemmeta.setDisplayName(mastery.getName());
        List<String> itemLore = new ArrayList<>();
        itemLore.add(" §7Level: §c" + level);
        itemLore.add(" §7Effect: §c" + (int) ((masteries.getMasteryEffect(mastery) + 1) * 100) + "% " + mastery.getEffectName());
        itemmeta.setLore(itemLore);
        item.setItemMeta(itemmeta);
        item = kitPvP.getApi().getNms().customItem().hideFlags(item, ItemUtils.FLAG_ATTRIBUTES_MODIFIERS);

        return item;
    }

    private ItemStack getUpgradeItem(KitPvPPlayer omp, Mastery mastery) {
        MasteriesData masteries = omp.masteries();

        if (masteries.getPoints() > 0) {
            int nextLevel = masteries.getMasteryLevel(mastery) + 1;
            if (nextLevel > 64)
                nextLevel = 64;

            ItemStack item = new ItemStack(Material.EMERALD, nextLevel);
            ItemMeta itemmeta = item.getItemMeta();
            itemmeta.setDisplayName(mastery.getColor() + (int) (mastery.getMultiplier() * 100) + "% " + mastery.getEffectName());
            List<String> itemLore = new ArrayList<>();
            itemLore.add(" §7" + omp.getMessage(new Message("Prijs", "Price")) + ": §c§l1 Mastery Point");
            itemmeta.setLore(itemLore);
            item.setItemMeta(itemmeta);

            return item;
        }
        return null;
    }

    private ItemStack getDowngradeItem(KitPvPPlayer omp, Mastery mastery) {
        MasteriesData masteries = omp.masteries();

        if (masteries.getMasteryLevel(mastery) > 0) {
            int nextLevel = masteries.getMasteryLevel(mastery) - 1;
            if (nextLevel > 64)
                nextLevel = 64;

            ItemStack item = new ItemStack(Material.BLAZE_POWDER, nextLevel);
            ItemMeta itemmeta = item.getItemMeta();
            itemmeta.setDisplayName(mastery.getOppositeColor() + (int) (mastery.getMultiplier() * 100) + "% " + mastery.getEffectName());
            List<String> itemLore = new ArrayList<>();
            itemLore.add(" §7Price: §b§l20 VIP Points");
            itemLore.add(" §7(+ 1 Mastery Point)");
            itemmeta.setLore(itemLore);
            item.setItemMeta(itemmeta);

            return item;
        }
        return null;
    }
}
