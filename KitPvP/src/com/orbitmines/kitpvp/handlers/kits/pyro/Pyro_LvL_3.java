package com.orbitmines.kitpvp.handlers.kits.pyro;

import com.orbitmines.api.spigot.handlers.Obtainable;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.kit.KitInteractive;
import com.orbitmines.api.spigot.runnable.OMRunnable;
import com.orbitmines.kitpvp.HealthRegenType;
import com.orbitmines.kitpvp.KitPvP;
import com.orbitmines.kitpvp.handlers.KitHandler;
import com.orbitmines.kitpvp.handlers.KitPvPKit;
import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import com.orbitmines.kitpvp.handlers.kits.Shooter;
import com.orbitmines.kitpvp.handlers.passives.firetrail.FireTrail_LvL_1;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public class Pyro_LvL_3 extends KitHandler implements Shooter {

    public Pyro_LvL_3() {
        super(KitPvPKit.Type.PYRO, 3, new Obtainable(KitPvP.COINS, 95000));
    }

    @Override
    protected KitInteractive registerKit() {
        KitInteractive kit = new KitInteractive(type.toString() + "-" + level);

        kit.setItem(0, new ItemBuilder(Material.IRON_SWORD, 1, 0, name() + "§bWeapon").addEnchantment(Enchantment.FIRE_ASPECT, 3).buildUnbreakable(true));
        kit.setItem(1, new ItemBuilder(Material.BOW, 1, 0, name() + "§bBow").addEnchantment(Enchantment.ARROW_FIRE, 1).buildUnbreakable(true));
        kit.setItem(2, new ItemBuilder(Material.ARROW, 10, 0, name() + "§bArrow").build());

        kit.setHelmet(new ItemBuilder(Material.GOLD_HELMET, 1, 0, name() + "§bHelmet").addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).buildUnbreakable(true));
        kit.setChestplate(new ItemBuilder(Material.GOLD_CHESTPLATE, 1, 0, name() + "§bChestplate").addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).buildUnbreakable(true));
        kit.setLeggings(new ItemBuilder(Material.GOLD_LEGGINGS, 1, 0, name() + "§bLeggings").addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).buildUnbreakable(true));
        kit.setBoots(add(new ItemBuilder(Material.GOLD_BOOTS, 1, 0, name() + "§bBoots").addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1), new FireTrail_LvL_1()).buildUnbreakable(true));

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

    @Override
    public OMRunnable.Time getReceiveTime() {
        return new OMRunnable.Time(OMRunnable.TimeUnit.SECOND, 10);
    }

    @Override
    public int getMaxProjectiles() {
        return 10;
    }
}
