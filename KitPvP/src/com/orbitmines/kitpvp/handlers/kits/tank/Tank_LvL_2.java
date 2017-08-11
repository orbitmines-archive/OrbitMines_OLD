package com.orbitmines.kitpvp.handlers.kits.tank;

import com.orbitmines.api.spigot.handlers.Obtainable;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.kit.KitInteractive;
import com.orbitmines.kitpvp.HealthRegenType;
import com.orbitmines.kitpvp.KitPvP;
import com.orbitmines.kitpvp.handlers.KitHandler;
import com.orbitmines.kitpvp.handlers.KitPvPKit;
import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import com.orbitmines.kitpvp.handlers.passives.knockbackchance.KnockbackChance_LvL_2;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public class Tank_LvL_2 extends KitHandler {

    public Tank_LvL_2() {
        super(KitPvPKit.Type.TANK, 2, new Obtainable(KitPvP.COINS, 50000));
    }

    @Override
    protected KitInteractive registerKit() {
        KitInteractive kit = new KitInteractive(type.toString() + "-" + level);

        kit.setItem(0, add(new ItemBuilder(Material.WOOD_SWORD, 1, 0, name() + "§bWeapon"), new KnockbackChance_LvL_2()).buildUnbreakable(true));

        kit.setHelmet(new ItemBuilder(Material.IRON_HELMET, 1, 0, name() + "§bHelmet").buildUnbreakable(true));
        kit.setChestplate(new ItemBuilder(Material.IRON_CHESTPLATE, 1, 0, name() + "§bChestplate").buildUnbreakable(true));
        kit.setLeggings(new ItemBuilder(Material.IRON_LEGGINGS, 1, 0, name() + "§bLeggings").addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).buildUnbreakable(true));
        kit.setBoots(new ItemBuilder(Material.DIAMOND_BOOTS, 1, 0, name() + "§bBoots").addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).buildUnbreakable(true));

        kit.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2000000, 2));

        return kit;
    }

    @Override
    public void update(KitPvPPlayer omp) {

    }

    @Override
    public double getMaxHealth() {
        return 25.0D;
    }

    @Override
    public double getKnockbackMultiplier() {
        return 0.75D;
    }

    @Override
    public double getDamageMultiplier() {
        return 0.75D;
    }

    @Override
    public HealthRegenType getHealthRegenType(KitPvPPlayer omp) {
        return HealthRegenType.EXTREMELY_LOW;
    }
}
