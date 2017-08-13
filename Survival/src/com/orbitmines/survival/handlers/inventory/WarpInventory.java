package com.orbitmines.survival.handlers.inventory;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.inventory.OMInventory;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.utils.ItemUtils;
import com.orbitmines.survival.Survival;
import com.orbitmines.survival.handlers.SurvivalPlayer;
import com.orbitmines.survival.handlers.Warp;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 12-8-2017
*/
public class WarpInventory extends OMInventory {

    private Survival survival;
    private int page;
    private boolean favorite;

    public WarpInventory(int page, boolean favorite) {
        survival = Survival.getInstance();
        this.page = page;
        this.favorite = favorite;

        newInventory(36, "§0§lWarps");
    }

    @Override
    protected boolean onOpen(OMPlayer player) {
        SurvivalPlayer omp = (SurvivalPlayer) player;

        if (favorite) {
            add(0, new ItemInstance(new ItemBuilder(Material.COMPASS, 1, 0, "§7§l" + omp.getMessage(new Message("Zoek", "Search")) + " Warps").build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    new WarpInventory(0, false);
                }
            });
            add(9, new EmptyItemInstance(new ItemBuilder(Material.DIAMOND, 1, 0, "§a§l" + omp.getMessage(new Message("Favoriete", "Favorite")) + " Warps").glow().build()));
        } else {
            add(0, new EmptyItemInstance(new ItemBuilder(Material.COMPASS, 1, 0, "§a§l" + omp.getMessage(new Message("Zoek", "Search")) + " Warps").glow().build()));
            add(9, new EmptyItemInstance(new ItemBuilder(Material.DIAMOND, 1, 0, "§7§l" + omp.getMessage(new Message("Favoriete", "Favorite")) + " Warps").build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    new WarpInventory(0, true);
                }
            });
        }

        int index = 0;
        List<Warp> warps;
        if (favorite)
            warps = omp.getFavoriteWarps();
        else
            warps = Warp.getWarps();

        for (int i = 2; i < 34; i++) {
            if (i == 7)
                i = 11;
            else if (i == 16)
                i = 20;
            else if (i == 25)
                i = 29;

            Warp warp;
            int warpIndex = index + 5 * page;
            if (warps.size() >= warpIndex + 1 && warps.get(warpIndex) != null)
                warp = warps.get(warpIndex);
            else
                warp = null;

            add(i, new ItemInstance(getWarpItem(omp, warp)) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer player) {
                    SurvivalPlayer omp = (SurvivalPlayer) player;

                    switch (event.getAction()) {
                        case PICKUP_HALF:
                            if (warp.isEnabled()) {
                                omp.getPlayer().closeInventory();
                                warp.teleport(omp);
                            } else {
                                omp.sendMessage(new Message("§7Die warp staat §c§lUIT§7!", "§7That warp has been §c§lDISABLED§7!"));
                            }
                            break;
                        case PICKUP_ALL:
                            if (omp.getFavoriteWarps().contains(warp)) {
                                omp.removeFavoriteWarp(warp);
                                new WarpInventory(event.getInventory().getItem(26).getAmount() - 2, favorite).open(omp);
                            } else {
                                omp.addFavoriteWarp(warp);
                                new WarpInventory(event.getInventory().getItem(26).getAmount() - 2, favorite).open(omp);
                            }
                            break;
                    }
                }
            });
            index++;
        }

        if (page == 0)
            add(17, new EmptyItemInstance(new ItemBuilder(Material.STAINED_GLASS_PANE, page > 64 ? 64 : page, 7, omp.getMessage(new Message("§8§nScroll Omhoog", "§8§nScroll Up"))).build()));
        else
            add(17, new ItemInstance(new ItemBuilder(Material.ENDER_PEARL, page > 64 ? 64 : page, 7, omp.getMessage(new Message("§3Scroll Omhoog", "§3Scroll Up"))).build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    new WarpInventory(page -1, favorite).open(omp);
                }
            });

        if (inventory.getItem(33).getType() == Material.STAINED_GLASS_PANE)
            add(26, new EmptyItemInstance(new ItemBuilder(Material.STAINED_GLASS_PANE, page + 2 > 64 ? 64 : page + 2, 7, omp.getMessage(new Message("§8§nScroll Omlaag", "§8§nScroll Down"))).build()));
        else
            add(26, new ItemInstance(new ItemBuilder(Material.ENDER_PEARL, page + 2 > 64 ? 64 : page + 2, 7, omp.getMessage(new Message("§3Scroll Omlaag", "§3Scroll Down"))).build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    new WarpInventory(page + 1, favorite).open(omp);
                }
            });

        return true;
    }

    @Override
    protected void onClick(InventoryClickEvent event, OMPlayer player) {

    }

    private ItemStack getWarpItem(SurvivalPlayer omp, Warp warp){
        if(warp != null){
            ItemStack item = new ItemStack(warp.getItemStack());
            ItemMeta meta = item.getItemMeta();
            List<String> lore = meta.getLore();
            lore.add("");
            lore.add(omp.getMessage(new Message(" §3Rechtermuisknop §7- Teleporteren", " §3Right Click §7- Teleport")));
            if(omp.getFavoriteWarps().contains(warp))
                lore.add(omp.getMessage(new Message(" §3Linkermuisknop §7- §cVerwijder van favoriten", " §3Left Click §7- §cRemove from favorites")));
            else
                lore.add(omp.getMessage(new Message(" §3Linkermuisknop §7- §aVoeg toe aan favorieten", " §3Left Click §7- §aAdd to favorites")));

            meta.setLore(lore);
            item.setItemMeta(meta);

            if(omp.getFavoriteWarps().contains(warp)) {
                item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
                return survival.getApi().getNms().customItem().hideFlags(item, ItemUtils.FLAG_ENCHANTMENTS);
            }

            return item;
        }
        return new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 7, "§8" + omp.getMessage(new Message("Leeg", "Empty")) + " Warp Slot").build();
    }
}
