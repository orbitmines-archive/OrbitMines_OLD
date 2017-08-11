package com.orbitmines.kitpvp.handlers.kits.bunny;

import com.orbitmines.api.spigot.handlers.Obtainable;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.itembuilders.LeatherArmorBuilder;
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
public class Bunny_LvL_1 extends KitHandler {

    public Bunny_LvL_1() {
        super(KitPvPKit.Type.BUNNY, 1, new Obtainable(KitPvP.COINS, 37500));
    }

    @Override
    protected KitInteractive registerKit() {
        KitInteractive kit = new KitInteractive(type.toString() + "-" + level);

        kit.setItem(0, new ItemBuilder(Material.IRON_SWORD, 1, 0, name() + "§bWeapon").buildUnbreakable(true));

        kit.setHelmet(new LeatherArmorBuilder(LeatherArmorBuilder.Type.HELMET, Bunny.LEATHER_COLOR, 1, name() + "§bHelmet").addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).buildUnbreakable(true));
        kit.setChestplate(new LeatherArmorBuilder(LeatherArmorBuilder.Type.CHESTPLATE, Bunny.LEATHER_COLOR, 1, name() + "§bChestplate").addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).buildUnbreakable(true));
        kit.setLeggings(new LeatherArmorBuilder(LeatherArmorBuilder.Type.LEGGINGS, Bunny.LEATHER_COLOR, 1, name() + "§bLeggings").addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).buildUnbreakable(true));
        kit.setBoots(new LeatherArmorBuilder(LeatherArmorBuilder.Type.BOOTS, Bunny.LEATHER_COLOR, 1, name() + "§bBoots").addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).buildUnbreakable(true));

        kit.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 2000000, 3));
        kit.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2000000, 0));

        return kit;
    }

    @Override
    public void update(KitPvPPlayer omp) {

    }

    @Override
    public double getMaxHealth() {
        return 18.0D;
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
