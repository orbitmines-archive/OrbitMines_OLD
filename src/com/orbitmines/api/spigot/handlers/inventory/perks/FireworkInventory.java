package com.orbitmines.api.spigot.handlers.inventory.perks;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.Color;
import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.handlers.ItemSet;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.Obtainable;
import com.orbitmines.api.spigot.handlers.firework.FireworkSettings;
import com.orbitmines.api.spigot.handlers.inventory.OMInventory;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.playerdata.GadgetData;
import com.orbitmines.api.spigot.perks.Gadget;
import com.orbitmines.api.spigot.perks.Perk;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class FireworkInventory extends PerkInventory {

    public FireworkInventory() {
        super(54, "§0§lFireworks", 48);
    }

    @Override
    protected void setPerkItems(OMPlayer omp) {
        GadgetData data = omp.gadgets();
        FireworkSettings settings = data.getFireworkSettings();

        add(10, new ItemInstance(getItemStack(settings.getColor1(), omp.getMessage(new Message("Kleur 1", "Color 1")))) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                settings.nextColor1();
                open(omp);
            }
        });

        add(28, new ItemInstance(getItemStack(settings.getColor2(), omp.getMessage(new Message("Kleur 1", "Color 1")))) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                settings.nextColor2();
                open(omp);
            }
        });

        add(12, new ItemInstance(getItemStack(settings.getFade1(), "Fade 1")) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                settings.nextFade1();
                open(omp);
            }
        });

        add(30, new ItemInstance(getItemStack(settings.getFade2(), "Fade 2")) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                settings.nextFade2();
                open(omp);
            }
        });

        add(14, new ItemInstance(new ItemBuilder(Material.STAINED_GLASS_PANE, 1, settings.hasTrail() ? 5 : 14, "§7Trail: " + omp.statusString(settings.hasTrail())).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                settings.nextTrail();
                open(omp);
            }
        });

        add(25, new ItemInstance(new ItemBuilder(getMaterial(settings.getType()), 1, getDurability(settings.getType()), "§7Type: " + getName(settings.getType())).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                settings.nextType();
                open(omp);
            }
        });

        add(32, new ItemInstance(new ItemBuilder(Material.STAINED_GLASS_PANE, 1, settings.hasFlicker() ? 5 : 14, "§7Flicker: " + omp.statusString(settings.hasTrail())).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                settings.nextFlicker();
                open(omp);
            }
        });

        add(49, new ItemInstance(new ItemBuilder(Material.ANVIL, 1, 0, omp.getMessage(new Message("§e§nMaak Firework", "§e§nCreate Firework"))).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                data.enableGadget(Gadget.FIREWORK_GUN);
            }
        });

        add(50, new EmptyItemInstance(new ItemBuilder(Material.EMPTY_MAP, data.getFireworkPasses() > 64 ? 64 : (data.getFireworkPasses() == 0 ? 1 : data.getFireworkPasses()), 0, "§c§nFirework Passes:§r §6§n" + data.getFireworkPasses()).build()));

        int slot = 52;
        for (Passes passes : Passes.values()) {
            add(slot, new ConfirmItemInstance(toItemBuilder(passes, omp).build()) {

                @Override
                protected void onConfirm(InventoryClickEvent event, OMPlayer omp) {
                    data.addFireworkPasses(passes.getAmount());
                }

                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    if (passes.obtainable().isPurchasable() && passes.obtainable().canPurchase(omp)) {
                        confirmPurchase(omp, passes, this);
                    } else {
                        passes.obtainable().msgNoAccess(omp);
                    }
                }
            });
            slot++;
        }
    }

    @Override
    protected OMInventory returnInventory() {
        return new FireworkInventory();
    }

    @Override
    protected boolean isDisabled() {
        return !api.isGadgetEnabled();
    }

    private ItemStack getItemStack(Color color, String name) {
        return new ItemBuilder(color.item().getMaterial(), 1, color.item().getDurability(), "§7" + name + ": " + color.getChatColor() + "§l" + color.getName()).build();
    }

    private Material getMaterial(FireworkEffect.Type type) {
        switch (type) {
            case BALL:
                return Material.FIREWORK_CHARGE;
            case BALL_LARGE:
                return Material.FIREBALL;
            case BURST:
                return Material.TNT;
            case CREEPER:
                return Material.SKULL_ITEM;
            case STAR:
                return Material.NETHER_STAR;
            default:
                return Material.FIREWORK_CHARGE;
        }
    }

    private short getDurability(FireworkEffect.Type type) {
        switch (type) {
            case CREEPER:
                return 4;
            default:
                return 0;
        }
    }

    private String getName(FireworkEffect.Type type) {
        switch (type) {
            case BALL:
                return "§e§lSmall";
            case BALL_LARGE:
                return "§6§lLarge";
            case BURST:
                return "§c§lSpecial";
            case CREEPER:
                return "§a§lCreeper";
            case STAR:
                return "§f§lStar";
            default:
                return "§e§lSmall";
        }
    }

    public enum Passes implements Perk {

        FIREWORK_PASSES_5(5, "Firework Passes", Color.ORANGE, new ItemSet(Material.EMPTY_MAP, 5), new Obtainable(OrbitMinesApi.VIP_POINTS, 2)),
        FIREWORK_PASSES_25(25, "Firework Passes", Color.ORANGE, new ItemSet(Material.EMPTY_MAP, 25), new Obtainable(OrbitMinesApi.VIP_POINTS, 10));

        private final int amount;
        private final String name;

        private final Color color;

        private final ItemSet item;

        private final Obtainable obtainable;

        Passes(int amount, String name, Color color, ItemSet item, Obtainable obtainable) {
            this.amount = amount;
            this.name = name;
            this.color = color;
            this.item = item;
            this.obtainable = obtainable;
        }

        public int getAmount() {
            return amount;
        }

        public String getName() {
            return name;
        }

        public Color color() {
            return color;
        }

        @Override
        public ItemSet item() {
            return item;
        }

        @Override
        public Obtainable obtainable() {
            return obtainable;
        }

        @Override
        public String getDisplayName() {
            return color.getChatColor() + amount + " " + name;
        }

        @Override
        public boolean hasAccess(OMPlayer omp) {
            return false;
        }
    }
}
