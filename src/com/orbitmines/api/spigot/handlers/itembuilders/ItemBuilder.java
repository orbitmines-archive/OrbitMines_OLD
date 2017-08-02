package com.orbitmines.api.spigot.handlers.itembuilders;

import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class ItemBuilder {

    protected Material material;
    protected int amount;
    protected short durability;
    protected String displayName;
    protected List<String> lore;

    protected Map<Enchantment, Integer> enchantments;
    protected boolean enchantmentsHidden;

    public ItemBuilder(Material material) {
        this(material, 1);
    }

    public ItemBuilder(Material material, int amount) {
        this(material, amount, 0);
    }

    public ItemBuilder(Material material, int amount, int durability) {
        this(material, amount, durability, null);
    }

    public ItemBuilder(Material material, int amount, int durability, String displayName) {
        this(material, amount, durability, displayName, (List<String>) null);
    }

    public ItemBuilder(Material material, int amount, int durability, String displayName, String... lore) {
        this(material, amount, durability, displayName, Arrays.asList(lore));
    }

    public ItemBuilder(Material material, int amount, int durability, String displayName, List<String> lore) {
        this.material = material;
        this.amount = amount;
        this.durability = (short) durability;
        this.displayName = displayName;
        this.lore = lore;
        this.enchantmentsHidden = false;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public short getDurability() {
        return durability;
    }

    public void setDurability(short durability) {
        this.durability = durability;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public void addEnchantment(Enchantment enchantment, int level) {
        if (enchantments == null)
            enchantments = new HashMap<>();

        enchantments.put(enchantment, level);
    }

    public void setEnchantments(Map<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
    }

    public void setEnchantmentsHidden(boolean enchantmentsHidden) {
        this.enchantmentsHidden = enchantmentsHidden;
    }

    public boolean enchantmentsHidden() {
        return enchantmentsHidden;
    }

    public void glow() {
        addEnchantment(Enchantment.DURABILITY, 1);
        enchantmentsHidden = true;
    }

    public ItemStack build() {
        ItemStack itemStack = new ItemStack(material, amount, durability);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(new ArrayList<>(lore));
        itemStack.setItemMeta(meta);

        return addEnchantments(itemStack);
    }

    protected ItemStack addEnchantments(ItemStack building) {
        if (enchantments == null)
            return building;

        building.addUnsafeEnchantments(new HashMap<>(enchantments));

        if (enchantmentsHidden)
            return OrbitMinesApi.getApi().getNms().customItem().hideFlags(building, ItemUtils.FLAG_ENCHANTMENTS);

        return building;
    }
}
