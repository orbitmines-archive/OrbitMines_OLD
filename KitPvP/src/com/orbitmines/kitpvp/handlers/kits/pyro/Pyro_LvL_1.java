package com.orbitmines.kitpvp.handlers.kits.pyro;

import com.orbitmines.api.spigot.handlers.Obtainable;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.kit.KitInteractive;
import com.orbitmines.kitpvp.HealthRegenType;
import com.orbitmines.kitpvp.KitPvP;
import com.orbitmines.kitpvp.handlers.KitHandler;
import com.orbitmines.kitpvp.handlers.KitPvPKit;
import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public class Pyro_LvL_1 extends KitHandler {

    public Pyro_LvL_1() {
        super(KitPvPKit.Type.PYRO, 1, new Obtainable(KitPvP.COINS, 25000));
    }

    @Override
    protected KitInteractive registerKit() {
        KitInteractive kit = new KitInteractive(type.toString() + "-" + level);

        kit.setItem(0, new ItemBuilder(Material.IRON_SWORD, 1, 0, name() + "§bWeapon").addEnchantment(Enchantment.FIRE_ASPECT, 2).buildUnbreakable(true));

        kit.setHelmet(new ItemBuilder(Material.GOLD_HELMET, 1, 0, name() + "§bHelmet").buildUnbreakable(true));
        kit.setChestplate(new ItemBuilder(Material.GOLD_CHESTPLATE, 1, 0, name() + "§bChestplate").buildUnbreakable(true));
        kit.setLeggings(new ItemBuilder(Material.GOLD_LEGGINGS, 1, 0, name() + "§bLeggings").buildUnbreakable(true));
        kit.setBoots(new ItemBuilder(Material.GOLD_BOOTS, 1, 0, name() + "§bBoots").buildUnbreakable(true));

        kit.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 2000000, 0));

        return kit;
    }

    @Override
    public void update(KitPvPPlayer omp) {

    }

    @Override
    public double getMaxHealth() {
        return 20.0D;
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
        return HealthRegenType.NORMAL;
    }
}
