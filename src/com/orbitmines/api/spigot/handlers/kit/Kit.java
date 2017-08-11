package com.orbitmines.api.spigot.handlers.kit;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

public class Kit {

    private static List<Kit> kits = new ArrayList<>();

    private String name;
    private ItemStack[] armorContents;
    private ItemStack[] contents;
    private ItemStack itemOffHand;

    private List<PotionEffect> potionEffects;

    public Kit(String name) {
        kits.add(this);

        this.name = name;
        this.armorContents = new ItemStack[4];
        this.contents = new ItemStack[36];
        this.potionEffects = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemStack getHelmet() {
        return this.armorContents[3];
    }

    public void setHelmet(ItemStack helmet) {
        this.armorContents[3] = helmet;
    }

    public ItemStack getChestplate() {
        return this.armorContents[2];
    }

    public void setChestplate(ItemStack chestplate) {
        this.armorContents[2] = chestplate;
    }

    public ItemStack getLeggings() {
        return this.armorContents[1];
    }

    public void setLeggings(ItemStack leggings) {
        this.armorContents[1] = leggings;
    }

    public ItemStack getBoots() {
        return this.armorContents[0];
    }

    public void setBoots(ItemStack boots) {
        this.armorContents[0] = boots;
    }

    public ItemStack[] getArmorContents() {
        return armorContents;
    }

    public void setArmorContents(ItemStack[] armorContents) {
        this.armorContents = armorContents;
    }

    public ItemStack[] getContents() {
        return contents;
    }

    public void setContents(ItemStack[] contents) {
        this.contents = contents;
    }

    public void setItem(int index, ItemStack content) {
        this.contents[index] = content;
    }

    public ItemStack getItem(int index) {
        return this.contents[index];
    }

    public void setItemOffHand(ItemStack itemOffHand) {
        this.itemOffHand = itemOffHand;
    }

    public ItemStack getItemOffHand() {
        return itemOffHand;
    }

    public ItemStack getFirstItem() {
        for (ItemStack item : this.contents) {
            if (item != null)
                return item;
        }
        return null;
    }

    public int contentItems() {
        int amount = 0;
        for (ItemStack item : getContents()) {
            if (item != null)
                amount++;
        }
        return amount;
    }

    public List<PotionEffect> getPotionEffects() {
        return potionEffects;
    }

    public void addPotionEffect(PotionEffect potionEffect) {
        this.potionEffects.add(potionEffect);
    }

    public void setItems(Player p) {
        for (ItemStack item : getContents()) {
            if (item != null && item.getType() == Material.SKULL_ITEM && item.getDurability() == (short) 3) {
                SkullMeta meta = (SkullMeta) item.getItemMeta();
                meta.setOwner(p.getName());
                item.setItemMeta(meta);
            }
        }

        ItemStack[] armorContents = new ItemStack[4];
        int index = 0;
        for (ItemStack item : getArmorContents()) {
            if (item != null)
                armorContents[index] = item;
            else
                armorContents[index] = p.getInventory().getArmorContents()[index];

            index++;
        }
        p.getInventory().setArmorContents(armorContents);

        index = 0;
        for (ItemStack item : getContents()) {
            if (item != null)
                p.getInventory().setItem(index, item);

            index++;
        }

        p.getInventory().setItemInOffHand(getItemOffHand());

        if (potionEffects.size() != 0) {
            for (PotionEffect potionEffect : potionEffects) {
                p.addPotionEffect(potionEffect);
            }
        }
    }

    public void replaceItems(Player p) {
        for (ItemStack item : getContents()) {
            if (item != null && item.getType() == Material.SKULL_ITEM && item.getDurability() == (short) 3) {
                SkullMeta meta = (SkullMeta) item.getItemMeta();
                meta.setOwner(p.getName());
                item.setItemMeta(meta);
            }
        }

        ItemStack[] armorContents = new ItemStack[4];
        int index = 0;
        for (ItemStack item : getArmorContents()) {
            if (item != null && setItem(p.getInventory().getArmorContents(), index, item))
                armorContents[index] = item;
            else
                armorContents[index] = p.getInventory().getArmorContents()[index];

            index++;
        }
        p.getInventory().setArmorContents(armorContents);

        index = 0;
        for (ItemStack item : getContents()) {
            if (item != null && setItem(p.getInventory().getContents(), index, item))
                p.getInventory().setItem(index, item);

            index++;
        }

        if (getItemOffHand() != null && setItem(getItemOffHand(), p.getInventory().getItemInOffHand()))
            p.getInventory().setItemInOffHand(getItemOffHand());

        if (potionEffects.size() != 0) {
            for (PotionEffect potionEffect : potionEffects) {
                p.addPotionEffect(potionEffect);
            }
        }
    }

    private boolean setItem(ItemStack item, ItemStack item2) {
        if (item2 == null)
            return true;

        return item.getType() != item2.getType() || item.getAmount() != item2.getAmount() || !item.getItemMeta().getDisplayName().equals(item2.getItemMeta().getDisplayName());
    }

    private boolean setItem(ItemStack[] contents, int index, ItemStack item) {
        ItemStack item2 = contents[index];

        if (item2 == null)
            return true;

        return item.getType() != item2.getType() || item.getAmount() != item2.getAmount() || !item.getItemMeta().getDisplayName().equals(item2.getItemMeta().getDisplayName());
    }

    public void addItems(Player p) {
        for (ItemStack item : getContents()) {
            if (item != null)
                p.getInventory().addItem(item);
        }
        int index = 0;
        for (ItemStack item : getArmorContents()) {
            ItemStack item2 = p.getInventory().getArmorContents()[index];

            if (item2 != null)
                p.getInventory().addItem(item2);

            if (index == 0)
                p.getInventory().setBoots(item);
            else if (index == 1)
                p.getInventory().setLeggings(item);
            else if (index == 2)
                p.getInventory().setChestplate(item);
            else
                p.getInventory().setHelmet(item);

            index++;
        }

        if (p.getInventory().getItemInOffHand() == null)
            p.getInventory().setItemInOffHand(getItemOffHand());
        else
            p.getInventory().addItem(getItemOffHand());

        if (potionEffects.size() != 0) {
            for (PotionEffect potionEffect : potionEffects) {
                p.addPotionEffect(potionEffect);
            }
        }
    }

    public static Kit getKit(String name) {
        for (Kit kit : kits) {
            if (kit.getName().equals(name)) {
                return kit;
            }
        }
        return null;
    }
}
