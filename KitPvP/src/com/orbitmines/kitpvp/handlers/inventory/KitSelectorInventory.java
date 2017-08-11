package com.orbitmines.kitpvp.handlers.inventory;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.inventory.OMInventory;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.utils.ItemUtils;
import com.orbitmines.kitpvp.KitPvP;
import com.orbitmines.kitpvp.handlers.KitPvPKit;
import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import com.orbitmines.kitpvp.handlers.playerdata.KitPvPData;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/*
* OrbitMines - @author Fadi Shawki - 11-8-2017
*/
@Deprecated
public class KitSelectorInventory extends OMInventory {

    private KitPvP kitPvP;

    public KitSelectorInventory() {
        newInventory(54, "§0§lKit Selector");

        kitPvP = KitPvP.getInstance();
    }

    @Override
    protected boolean onOpen(OMPlayer player) {
        KitPvPPlayer omp = (KitPvPPlayer) player;

        add(omp, 9, KitPvPKit.Type.KNIGHT);
        add(omp, 10, KitPvPKit.Type.ARCHER);
        add(omp, 11, KitPvPKit.Type.SOLDIER);
        add(omp, 12, KitPvPKit.Type.WIZARD);
        add(omp, 13, KitPvPKit.Type.TANK);
        add(omp, 14, KitPvPKit.Type.DRUNK);
        add(omp, 15, KitPvPKit.Type.PYRO);
        add(omp, 16, KitPvPKit.Type.BUNNY);

        KitPvPKit.Type[] values = KitPvPKit.Type.values();
        for (int i = 17; i < 45; i++) {
            add(i, new EmptyItemInstance(new ItemBuilder(Material.INK_SACK, 1, 8, values.length <= (i - 9) + 1 ? "§aComing Soon" : "§b" + values[i - 9].getName() + " §8| §aComing Soon").build()));
        }

        return true;
    }

    @Override
    protected void onClick(InventoryClickEvent event, OMPlayer omp) {

    }

    private void add(KitPvPPlayer omp, int slot, KitPvPKit.Type type) {
        add(slot, new ItemInstance(getItem(omp, type.getKit())) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer player) {
                Player p = omp.getPlayer();
                InventoryAction a = event.getAction();
                ItemStack item = event.getCurrentItem();

                if (item.getType() == type.icon().getMaterial() && item.getItemMeta().getLore().contains("§4§l" + omp.getMessage(new Message("Vergrendeld", "Locked")))) {
                    if (a == InventoryAction.PICKUP_HALF) {
                        omp.sendMessage(new Message("§7Deze Kit is voor jouw §4§lVergrendeld§7! Koop het met §6Linkermuisklik§7!", "§7This kit is §4§lLocked§7 for you! Buy it with §6Left Click§7!"));
                    } else if (a == InventoryAction.PICKUP_ALL) {
                        new KitInventory(type, 1).open(omp);
                    }
                    return;
                }

                for (int level = 1; level <= type.getKit().getMaxLevel(); level++) {
                    if (item.getType() == type.icon().getMaterial() && item.getItemMeta().getLore().contains("§a§l" + omp.getMessage(new Message("Ontgrendeld", "Unlocked")) + " §7§o(§a§oLvL " + level + "§7§o)") || level == 1 && item.getType() == type.icon().getMaterial() && item.getItemMeta().getLore().contains("§d§lFREE Kit " + omp.getMessage(new Message("Zaterdag", "Saturday")) + "!")) {
                        if (a == InventoryAction.PICKUP_HALF) {
                            omp.giveKit(type, level);
                            omp.teleportToMap();
                            p.closeInventory();
                        } else if (a == InventoryAction.PICKUP_ALL) {
                            if (level == type.getKit().getMaxLevel()) {
                                new KitInventory(type, level).open(omp);
                            } else {
                                new KitInventory(type, level + 1).open(omp);
                            }
                        }
                        break;
                    }
                }
            }
        });
    }

    private ItemStack getItem(KitPvPPlayer omp, KitPvPKit kit) {
        KitPvPData data = omp.kitPvP();

        int kitLevel = data.getUnlockedLevel(kit.getType());

        ItemStack item = new ItemStack(kit.getType().icon().getMaterial(), kitLevel == 0 ? 1 : kitLevel);
        ItemMeta itemmeta = item.getItemMeta();
        itemmeta.setDisplayName(kit.getType().getKitName(kitLevel));
        itemmeta.setLore(kit.getType().getKitLore(omp, kitLevel));
        item.setItemMeta(itemmeta);
        item.setDurability(kit.getType().icon().getDurability());
        if (kitPvP.isFreeKitEnabled() || kitLevel != 0)
            item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);

        return kitPvP.getApi().getNms().customItem().hideFlags(item, ItemUtils.FLAG_ENCHANTMENTS, ItemUtils.FLAG_ATTRIBUTES_MODIFIERS, ItemUtils.FLAG_POTIONS);
    }
}
