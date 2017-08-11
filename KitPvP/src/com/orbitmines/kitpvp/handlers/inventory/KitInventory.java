package com.orbitmines.kitpvp.handlers.inventory;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.ItemSet;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.inventory.ConfirmInventory;
import com.orbitmines.api.spigot.handlers.inventory.OMInventory;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.kit.KitInteractive;
import com.orbitmines.api.spigot.utils.ItemUtils;
import com.orbitmines.api.utils.Roman;
import com.orbitmines.kitpvp.KitPvP;
import com.orbitmines.kitpvp.handlers.KitHandler;
import com.orbitmines.kitpvp.handlers.KitPvPKit;
import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import com.orbitmines.kitpvp.handlers.kits.Shooter;
import com.orbitmines.kitpvp.handlers.playerdata.KitPvPData;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 11-8-2017
*/
@Deprecated
public class KitInventory extends OMInventory {

    private final KitPvP kitPvP;
    private final KitPvPKit kitPvPKit;
    private final KitHandler handler;
    private final KitInteractive kit;
    private final int level;

    public KitInventory(KitPvPKit.Type type, int level) {
        kitPvP = KitPvP.getInstance();
        kitPvPKit = type.getKit();
        this.level = level;
        handler = kitPvPKit.getHandlers()[level -1];
        kit = handler.getKit();

        newInventory(54, "§0§l" + type.getName() + " (Level " + level + ")");
    }

    @Override
    protected boolean onOpen(OMPlayer player) {
        KitPvPPlayer omp = (KitPvPPlayer) player;
        KitPvPData data = omp.kitPvP();

        add(45, new ItemInstance(new ItemBuilder(Material.REDSTONE_BLOCK, 1, 0, "§4§l§o<< " + omp.getMessage(new Message("Terug", "Back"))).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                new KitSelectorInventory().open(omp);
            }
        });

        for (int level = 1; level <= 3; level++) {
            int l = level;

            ItemStack item = getItem(omp, level);
            if (item.getType() == Material.NETHER_STAR)
                add(48 + (l - 1), new ItemInstance(item) {
                    @Override
                    public void onClick(InventoryClickEvent event, OMPlayer omp) {
                        new KitInventory(kitPvPKit.getType(), l).open(omp);
                    }
                });
            else
                add(48 + (l - 1), new EmptyItemInstance(item));
        }

        add(4, new EmptyItemInstance(kit.getHelmet()));
        add(13, new EmptyItemInstance(kit.getChestplate()));
        add(22, new EmptyItemInstance(kit.getLeggings()));
        add(31, new EmptyItemInstance(kit.getBoots()));

        if (level == data.getUnlockedLevel(kitPvPKit.getType()) + 1) {
            add(53, new ItemInstance(new ItemBuilder(Material.EMERALD_BLOCK, 1, 0, "§2§l§o" + omp.getMessage(new Message("Koop", "Buy")) + " " + kitPvPKit.getType().getName() + " §7§o(§a§oLvL " + level + "§7§o)",
                    handler.obtainable().getRequiredLore(omp)).build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    if (handler.obtainable().isPurchasable() && handler.obtainable().canPurchase(omp)) {
                        new ConfirmInventory(handler.getType().getDisplayName() + " §a§lLvL " + level, new ItemSet(handler.getType().icon().getMaterial(), level, handler.getType().icon().getDurability()), handler.obtainable()) {
                            @Override
                            public void onConfirm(InventoryClickEvent event, OMPlayer omp) {
                                handler.obtainable().purchase(omp);
                                omp.getPlayer().closeInventory();

                                data.setUnlockedKitLevel(kitPvPKit.getType(), level);

                                new KitInventory(kitPvPKit.getType(), level).open(omp);
                            }

                            @Override
                            public void onCancel(InventoryClickEvent event, OMPlayer omp) {
                                new KitInventory(kitPvPKit.getType(), level).open(omp);
                            }
                        }.open(omp);
                    } else {
                        handler.obtainable().msgNoAccess(omp);
                    }
                }
            });
        }

        int items = kit.contentItems();

        if (items == 7 || items == 8) {
            add(2, new EmptyItemInstance(getItem(omp, kit.getItem(0))));
            add(6, new EmptyItemInstance(getItem(omp, kit.getItem(1))));
            add(11, new EmptyItemInstance(getItem(omp, kit.getItem(2))));
            add(15, new EmptyItemInstance(getItem(omp, kit.getItem(3))));
            add(20, new EmptyItemInstance(getItem(omp, kit.getItem(4))));
            add(24, new EmptyItemInstance(getItem(omp, kit.getItem(5))));
            add(20, new EmptyItemInstance(getItem(omp, kit.getItem(6))));

            if (items == 8)
                add(33, new EmptyItemInstance(getItem(omp, kit.getItem(7))));
        } else {
            if (items >= 1)
                add(11, new EmptyItemInstance(getItem(omp, kit.getItem(0))));
            if (items >= 2)
                add(15, new EmptyItemInstance(getItem(omp, kit.getItem(1))));
            if (items >= 3)
                add(20, new EmptyItemInstance(getItem(omp, kit.getItem(2))));
            if (items >= 4)
                add(24, new EmptyItemInstance(getItem(omp, kit.getItem(3))));
            if (items >= 5)
                add(29, new EmptyItemInstance(getItem(omp, kit.getItem(4))));
            if (items == 6)
                add(33, new EmptyItemInstance(getItem(omp, kit.getItem(5))));
        }

        List<PotionEffect> effects = kit.getPotionEffects();
        if (effects.size() != 0) {
            if (effects.size() == 1) {
                ItemStack item = getItem(omp, effects.get(0).getType(), effects.get(0).getAmplifier() +1);
                add(18, new EmptyItemInstance(item));
                add(26, new EmptyItemInstance(item));
            } else {
                for (int i = 0; i < 2; i++) {
                    add(18 + i * 8, new EmptyItemInstance(getItem(omp, effects.get(i).getType(), effects.get(i).getAmplifier() +1)));
                }
            }
        }

        return true;
    }

    @Override
    protected void onClick(InventoryClickEvent event, OMPlayer omp) {

    }

    private ItemStack getItem(OMPlayer omp, PotionEffectType type, int level){
        ItemStack item = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        meta.setDisplayName("§b§l§o" + name(type) + " " + Roman.parse(level));
        meta.addCustomEffect(new PotionEffect(type, 1, level -1), true);
        item.setItemMeta(meta);

        return kitPvP.getApi().getNms().customItem().hideFlags(item, ItemUtils.FLAG_ATTRIBUTES_MODIFIERS, ItemUtils.FLAG_POTIONS);
    }

    private ItemStack getItem(OMPlayer omp, ItemStack item){
        if(item != null && item.getType() == Material.ARROW){
            ItemMeta meta = item.getItemMeta();
            List<String> itemLore = new ArrayList<>();

            KitHandler handler = kitPvPKit.getHandlers()[level -1];
            if (handler instanceof Shooter) {
                int seconds = ((Shooter) handler).getReceiveTime().getAmount();

                itemLore.add(omp.getMessage(new Message(" §c+1 Arrow: §6Elke " + seconds + " seconden", " §c+1 Arrow: §6Every " + seconds + " seconds")));
                itemLore.add(" §cMaximum: §6" + item.getAmount() + " Arrows");
            }
            meta.setLore(itemLore);
            item.setItemMeta(meta);
        }

        return item;
    }

    private ItemStack getItem(OMPlayer omp, int level){
        if(kitPvPKit.getMaxLevel() >= level){
            return new ItemBuilder(Material.NETHER_STAR, level, 0, "§b§l" + kitPvPKit.getType().getName() + " §7§o(§a§oLvL " + level + "§7§o)").build();
        }
        return new ItemBuilder(Material.INK_SACK, level, 8, "§4§l§o" + omp.getMessage(new Message("Onbeschikbaar", "Unavailable"))).build();
    }

    private String name(PotionEffectType type){
        if(type == PotionEffectType.ABSORPTION) return "Absorption";
        else if(type == PotionEffectType.BLINDNESS) return "Blindness";
        else if(type == PotionEffectType.CONFUSION) return "Nausea";
        else if(type == PotionEffectType.DAMAGE_RESISTANCE) return "Resistance";
        else if(type == PotionEffectType.FAST_DIGGING) return "Haste";
        else if(type == PotionEffectType.FIRE_RESISTANCE) return "Fire Resistance";
        else if(type == PotionEffectType.HARM) return "Harming";
        else if(type == PotionEffectType.HEAL) return "Health";
        else if(type == PotionEffectType.HEALTH_BOOST) return "Health Boost";
        else if(type == PotionEffectType.HUNGER) return "Hunger";
        else if(type == PotionEffectType.INCREASE_DAMAGE) return "Strength";
        else if(type == PotionEffectType.INVISIBILITY) return "Invisibility";
        else if(type == PotionEffectType.INVISIBILITY) return "Invisibility";
        else if(type == PotionEffectType.JUMP) return "Jump Boost";
        else if(type == PotionEffectType.NIGHT_VISION) return "Night Vision";
        else if(type == PotionEffectType.POISON) return "Poison";
        else if(type == PotionEffectType.REGENERATION) return "Regeneration";
        else if(type == PotionEffectType.SATURATION) return "Saturation";
        else if(type == PotionEffectType.SLOW) return "Slowness";
        else if(type == PotionEffectType.SLOW_DIGGING) return "Mining Fatigue";
        else if(type == PotionEffectType.SPEED) return "Speed";
        else if(type == PotionEffectType.WATER_BREATHING) return "Water Breathing";
        else if(type == PotionEffectType.WEAKNESS) return "Weakness";
        else if(type == PotionEffectType.WITHER) return "Wither";
        else return null;
    }
}
