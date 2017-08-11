package com.orbitmines.kitpvp.handlers.kits.soldier;

import com.orbitmines.api.spigot.handlers.Obtainable;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.kit.KitInteractive;
import com.orbitmines.kitpvp.HealthRegenType;
import com.orbitmines.kitpvp.KitPvP;
import com.orbitmines.kitpvp.handlers.KitHandler;
import com.orbitmines.kitpvp.handlers.KitPvPKit;
import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import com.orbitmines.kitpvp.handlers.passives.thor.Thor_LvL_1;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public class Soldier_LvL_2 extends KitHandler {

    public Soldier_LvL_2() {
        super(KitPvPKit.Type.SOLDIER, 2, new Obtainable(KitPvP.COINS, 35000));
    }

    @Override
    protected KitInteractive registerKit() {
        KitInteractive kit = new KitInteractive(type.toString() + "-" + level);

        kit.setItem(0, add(new ItemBuilder(Material.STONE_SWORD, 1, 0, name() + "§bWeapon"), new Thor_LvL_1()).buildUnbreakable(true));
        kit.setItem(1, new ItemBuilder(Material.BOW, 1, 0, name() + "§bBow").buildUnbreakable(true));
        kit.setItem(2, new ItemBuilder(Material.ARROW, 30, 0, name() + "§bArrow").build());

        kit.setHelmet(new ItemBuilder(Material.LEATHER_HELMET, 1, 0, name() + "§bHelmet").addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).buildUnbreakable(true));
        kit.setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE, 1, 0, name() + "§bChestplate").buildUnbreakable(true));
        kit.setLeggings(new ItemBuilder(Material.IRON_LEGGINGS, 1, 0, name() + "§bLeggings").buildUnbreakable(true));
        kit.setBoots(new ItemBuilder(Material.LEATHER_BOOTS, 1, 0, name() + "§bBoots").buildUnbreakable(true));

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
