package com.orbitmines.kitpvp.handlers.kits.knight;

import com.orbitmines.api.spigot.handlers.Obtainable;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.itembuilders.PotionBuilder;
import com.orbitmines.api.spigot.handlers.itembuilders.PotionItemBuilder;
import com.orbitmines.api.spigot.handlers.kit.KitInteractive;
import com.orbitmines.kitpvp.HealthRegenType;
import com.orbitmines.kitpvp.KitPvP;
import com.orbitmines.kitpvp.handlers.KitHandler;
import com.orbitmines.kitpvp.handlers.KitPvPKit;
import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffectType;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public class Knight_LvL_3 extends KitHandler {

    public Knight_LvL_3() {
        super(KitPvPKit.Type.KNIGHT, 3, new Obtainable(KitPvP.COINS, 50000));
    }

    @Override
    protected KitInteractive registerKit() {
        KitInteractive kit = new KitInteractive(type.toString() + "-" + level);

        kit.setItem(0, new ItemBuilder(Material.IRON_SWORD, 1, 0, name() + "§bWeapon").addEnchantment(Enchantment.DAMAGE_ALL, 1).buildUnbreakable(true));
        kit.setItem(1, new PotionItemBuilder(PotionItemBuilder.Type.SPLASH, new PotionBuilder(PotionEffectType.HEAL, 0, 1), true, 2, name() + "§cHealing Potion").build());

        kit.setHelmet(new ItemBuilder(Material.CHAINMAIL_HELMET, 1, 0, name() + "§bHelmet").addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).buildUnbreakable(true));
        kit.setChestplate(new ItemBuilder(Material.CHAINMAIL_CHESTPLATE, 1, 0, name() + "§bChestplate").addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).buildUnbreakable(true));
        kit.setLeggings(new ItemBuilder(Material.CHAINMAIL_LEGGINGS, 1, 0, name() + "§bLeggings").addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).buildUnbreakable(true));
        kit.setBoots(new ItemBuilder(Material.CHAINMAIL_BOOTS, 1, 0, name() + "§bBoots").addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).buildUnbreakable(true));

        return kit;
    }

    @Override
    public void update(KitPvPPlayer omp) {

    }

    @Override
    public double getMaxHealth() {
        return 19.0D;
    }

    @Override
    public double getKnockbackMultiplier() {
        return 1.0D;
    }

    @Override
    public double getDamageMultiplier() {
        return 1.0D;
    }

    @Override
    public HealthRegenType getHealthRegenType(KitPvPPlayer omp) {
        return HealthRegenType.HIGHEST;
    }
}
