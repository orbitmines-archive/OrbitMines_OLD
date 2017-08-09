package com.orbitmines.api.spigot.handlers.itembuilders;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class LeatherArmorBuilder extends ItemBuilder {

    private Color color;

    public LeatherArmorBuilder(Type type, Color color) {
        this(type, color, 1);
    }

    public LeatherArmorBuilder(Type type, Color color, int amount) {
        this(type, color, amount, null);
    }

    public LeatherArmorBuilder(Type type, Color color, int amount, String displayName) {
        this(type, color, amount, displayName, (List<String>) null);
    }

    public LeatherArmorBuilder(Type type, Color color, int amount, String displayName, String... lore) {
        this(type, color, amount, displayName, Arrays.asList(lore));
    }

    public LeatherArmorBuilder(Type type, Color color, int amount, String displayName, List<String> lore) {
        super(type.material, amount, 0, displayName, lore);

        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setType(Type type) {
        this.material = type.material;
    }

    @Override
    public ItemStack build() {
        ItemStack itemStack = new ItemStack(material, amount, durability);
        LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(lore == null ? null : new ArrayList<>(lore));
        meta.setColor(color);
        itemStack.setItemMeta(meta);

        return addEnchantments(itemStack);
    }

    public enum Type {

        HELMET(Material.LEATHER_HELMET),
        CHESTPLATE(Material.LEATHER_CHESTPLATE),
        LEGGINGS(Material.LEATHER_LEGGINGS),
        BOOTS(Material.LEATHER_BOOTS);

        private final Material material;

        Type(Material material) {
            this.material = material;
        }

        public Material material() {
            return material;
        }
    }
}
