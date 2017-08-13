package com.orbitmines.survival.handlers.inventory;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.ItemSet;
import com.orbitmines.api.spigot.nms.anvilgui.AnvilNms;
import com.orbitmines.survival.Survival;
import com.orbitmines.survival.handlers.SurvivalPlayer;
import com.orbitmines.survival.handlers.Warp;
import com.orbitmines.survival.utils.BiomeUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 12-8-2017
*/
public class WarpRenameGui {

    private Survival survival;
    private AnvilNms anvilNms;

    public WarpRenameGui(SurvivalPlayer omp, Warp warp) {
        this.survival = Survival.getInstance();

        this.anvilNms = survival.getApi().getNms().anvilGui(omp.getPlayer(), new AnvilNms.AnvilClickEventHandler() {

            @Override
            public void onAnvilClick(AnvilNms.AnvilClickEvent e) {
                if (e.getSlot() == AnvilNms.AnvilSlot.OUTPUT) {
                    String warpName = e.getName();
                    Player p = omp.getPlayer();
                    List<Warp> warps = Warp.getWarps();

                    if (Warp.getWarp(warpName) == null) {
                        if (warpName.length() <= 20) {
                            boolean canCreate = true;
                            for (int i = 0; i < warpName.length(); i++) {
                                int c = warpName.charAt(i);
                                if (!Character.isAlphabetic(c) && !Character.isDigit(c)) {
                                    canCreate = false;
                                    break;
                                }
                            }

                            e.setWillClose(canCreate);
                            e.setWillDestroy(canCreate);
                            if (canCreate) {
                                if (warp == null) {
                                    Biome biome = p.getWorld().getBiome(p.getLocation().getBlockX(), p.getLocation().getBlockZ());
                                    ItemSet set = BiomeUtils.item(biome);

                                    Warp warp = new Warp(warps.size() + 1, p.getUniqueId(), warpName, p.getLocation(), true, set.getMaterial(), set.getDurability());
                                    omp.getWarps().add(warp);

                                    omp.sendMessage(new Message("§7Warp Gemaakt! (§6" + warpName + "§7)", "§7Created a Warp! (§6" + warpName + "§7)"));
                                } else {
                                    omp.sendMessage(new Message("§7Je hebt de naam van '§6" + warp.getName() + "§7' veranderd in '§6" + warpName + "§7'.", "§7Changed the name of '§6" + warp.getName() + "§7' to '§6" + warpName + "§7'."));
                                    warp.setName(warpName);
                                    warp.updateItemStack(warp.getItemStack().getType(), warp.getItemStack().getDurability());
                                }

                                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 5, 1);
                                Warp.saveToConfig();
                            } else {
                                omp.sendMessage(new Message("§7Alleen §6letters§7 en §6cijfers§7 zijn toegestaan!", "§7Only §6alphabetic§7 and §6numeric§7 characters are allowed!"));
                            }
                        } else {
                            e.setWillClose(false);
                            e.setWillDestroy(false);

                            omp.sendMessage(new Message("§7Je kan maximaal §620 karakters§7 gebruiken in de naam van een warp!", "§7You may only use §620 characters§7 in the name of a warp!"));
                        }
                    } else {
                        e.setWillClose(false);
                        e.setWillDestroy(false);

                        omp.sendMessage(new Message("§7Er bestaat al een warp genaamd '§6" + warpName + "§7'.", "§7There already is a warp named '§6" + warpName + "§7'."));
                    }
                } else {
                    e.setWillClose(false);
                    e.setWillDestroy(false);
                }
            }
        });

        ItemStack item = new ItemStack(Material.NAME_TAG, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(omp.getMessage(new Message("Warp naam hier", "Insert Warp name")));
        item.setItemMeta(meta);

        this.anvilNms.getItems().put(AnvilNms.AnvilSlot.INPUT_LEFT, item);
    }

    public AnvilNms getAnvilNms() {
        return anvilNms;
    }

    public void open() {
        anvilNms.open();
    }
}
