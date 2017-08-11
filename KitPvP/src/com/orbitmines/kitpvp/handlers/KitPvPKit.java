package com.orbitmines.kitpvp.handlers;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.Color;
import com.orbitmines.api.spigot.handlers.ItemSet;
import com.orbitmines.kitpvp.KitPvP;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public class KitPvPKit {

    protected final Type type;
    protected final KitHandler[] handlers;

    public KitPvPKit(Type type, int maxLevel) {
        this.type = type;
        this.handlers = new KitHandler[maxLevel];

        type.setKit(this);
    }

    public Type getType() {
        return type;
    }

    public KitHandler[] getHandlers() {
        return handlers;
    }

    public int getMaxLevel() {
        return handlers.length;
    }

    public enum Type {

        KNIGHT("Knight", new ItemSet(Material.IRON_SWORD)),
        ARCHER("Archer", new ItemSet(Material.BOW)),
        SOLDIER("Soldier", new ItemSet(Material.IRON_LEGGINGS)),
        WIZARD("Wizard", new ItemSet(Material.POTION)),
        TANK("Tank", new ItemSet(Material.IRON_CHESTPLATE)),
        DRUNK("Drunk", new ItemSet(Material.GLASS_BOTTLE)),
        PYRO("Pyro", new ItemSet(Material.GOLD_HELMET)),
        BUNNY("Bunny", new ItemSet(Material.LEATHER_BOOTS)),
        NECROMANCER("Necromancer", new ItemSet(Material.STONE)),
        KING("King", new ItemSet(Material.STONE)),
        TREE("Tree", new ItemSet(Material.STONE)),
        BLAZE("Blaze", new ItemSet(Material.STONE)),
        TNT("TNT", new ItemSet(Material.STONE)),
        FISHERMAN("Fisherman", new ItemSet(Material.STONE)),
        SNOWGOLEM("SnowGolem", new ItemSet(Material.STONE)),
        LIBRARIAN("Librarian", new ItemSet(Material.STONE)),
        SPIDER("Spider", new ItemSet(Material.STONE)),
        VILLAGER("Villager", new ItemSet(Material.STONE)),
        ASSASSIN("Assassin", new ItemSet(Material.STONE)),
        LORD("Lord", new ItemSet(Material.STONE)),
        VAMPIRE("Vampire", new ItemSet(Material.STONE)),
        DARKMAGE("DarkMage", new ItemSet(Material.STONE)),
        BEAST("Beast", new ItemSet(Material.STONE)),
        FISH("Fish", new ItemSet(Material.STONE)),
        HEAVY("Heavy", new ItemSet(Material.STONE)),
        GRIMREAPER("GrimReaper", new ItemSet(Material.STONE)),
        MINER("Miner", new ItemSet(Material.STONE)),
        FARMER("Farmer", new ItemSet(Material.STONE)),
        UNDEATH_KING("Undeath King", new ItemSet(Material.STONE)),
        ENGINEER("Engineer", new ItemSet(Material.STONE));

        private final String name;
        private final Color color;
        private final ItemSet icon;

        private KitPvPKit kit;

        Type(String name, ItemSet icon) {
            this(name, Color.AQUA, icon);
        }

        Type(String name, Color color, ItemSet icon) {
            this.name = name;
            this.color = color;
            this.icon = icon;
        }

        public String getName() {
            return name;
        }

        public Color color() {
            return color;
        }

        public ItemSet icon() {
            return icon;
        }

        public String getDisplayName() {
            return color.getChatColor() + "§l" + name;
        }

        public KitPvPKit getKit() {
            return kit;
        }

        public void setKit(KitPvPKit kit) {
            this.kit = kit;
        }

        @Deprecated
        public String getSelectedKitName(int level) {
            return "§b§l" + getName() + " §7(§aLvL " + level + "§7)";
        }

        @Deprecated
        public String getKitName(int level) {
            if (level != 0) {
                return "§b§l" + getName() + " §a§lLvL " + level;
            }
            return "§b§l" + getName();
        }

        @Deprecated
        public List<String> getKitLore(KitPvPPlayer omp, int level) {
            List<String> kitLore = new ArrayList<>();
            kitLore.add("");
            if (KitPvP.getInstance().isFreeKitEnabled() && level == 0) {
                kitLore.add("§d§lFREE Kit " + omp.getMessage(new Message("Zaterdag", "Saturday")) + "!");
            } else {
                if (level != 0) {
                    kitLore.add("§a§l" + omp.getMessage(new Message("Ontgrendeld", "Unlocked")) + " §7§o(§a§oLvL " + level + "§7§o)");
                } else {
                    kitLore.add("§4§l" + omp.getMessage(new Message("Vergrendeld", "Locked")));
                }
            }
            kitLore.add("");
            if (level == 0) {
                kitLore.add("§c§o§l§m" + omp.getMessage(new Message("Rechtermuisknop", "Right Click")) + " §c§m(" + omp.getMessage(new Message("Kit Selecteren", "Select Kit")) + ")");
                kitLore.add("§6§l§o" + omp.getMessage(new Message("Linkermuisknop", "Left Click")) + " §7(Details & " + omp.getMessage(new Message("Kit Kopen", "Buy Kit")) + ")");
            } else {
                kitLore.add("§e§l§o" + omp.getMessage(new Message("Rechtermuisknop", "Right Click")) + " §7(" + omp.getMessage(new Message("Kit Selecteren", "Select Kit")) + ")");
                if (level != kit.getMaxLevel()) {
                    kitLore.add("§6§l§o" + omp.getMessage(new Message("Linkermuisknop", "Left Click")) + " §7(Details & Upgrade Kit)");
                } else {
                    kitLore.add("§6§l§o" + omp.getMessage(new Message("Linkermuisknop", "Left Click")) + " §7(Details)");
                }
            }
            kitLore.add("");

            return kitLore;
        }
    }
}
