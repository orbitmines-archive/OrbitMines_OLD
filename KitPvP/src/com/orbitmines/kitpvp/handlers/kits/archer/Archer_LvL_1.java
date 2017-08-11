package com.orbitmines.kitpvp.handlers.kits.archer;

import com.orbitmines.api.VipRank;
import com.orbitmines.api.spigot.handlers.Obtainable;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.itembuilders.LeatherArmorBuilder;
import com.orbitmines.api.spigot.handlers.kit.KitInteractive;
import com.orbitmines.api.spigot.runnable.OMRunnable;
import com.orbitmines.kitpvp.HealthRegenType;
import com.orbitmines.kitpvp.handlers.KitHandler;
import com.orbitmines.kitpvp.handlers.KitPvPKit;
import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import com.orbitmines.kitpvp.handlers.kits.Shooter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public class Archer_LvL_1 extends KitHandler implements Shooter {

    public Archer_LvL_1() {
        super(KitPvPKit.Type.ARCHER, 1, new Obtainable(VipRank.NONE));
    }

    @Override
    protected KitInteractive registerKit() {
        KitInteractive kit = new KitInteractive(type.toString() + "-" + level);

        kit.setItem(0, new ItemBuilder(Material.WOOD_SWORD, 1, 0, name() + "§bWeapon").buildUnbreakable(true));
        kit.setItem(1, new ItemBuilder(Material.BOW, 1, 0, name() + "§bBow").buildUnbreakable(true));
        kit.setItem(2, new ItemBuilder(Material.ARROW, 40, 0, name() + "§bArrow").build());

        kit.setHelmet(new LeatherArmorBuilder(LeatherArmorBuilder.Type.HELMET, Archer.LEATHER_COLOR, 1, name() + "§bHelmet").buildUnbreakable(true));
        kit.setChestplate(new LeatherArmorBuilder(LeatherArmorBuilder.Type.CHESTPLATE, Archer.LEATHER_COLOR, 1, name() + "§bChestplate").buildUnbreakable(true));
        kit.setLeggings(new LeatherArmorBuilder(LeatherArmorBuilder.Type.LEGGINGS, Archer.LEATHER_COLOR, 1, name() + "§bLeggings").buildUnbreakable(true));
        kit.setBoots(new LeatherArmorBuilder(LeatherArmorBuilder.Type.BOOTS, Archer.LEATHER_COLOR, 1, name() + "§bBoots").addEnchantment(Enchantment.PROTECTION_FALL, 1).buildUnbreakable(true));

        return kit;
    }

    @Override
    public void update(KitPvPPlayer omp) {

    }

    @Override
    public double getMaxHealth() {
        return 16.0D;
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
        return HealthRegenType.LOW;
    }

    @Override
    public OMRunnable.Time getReceiveTime() {
        return new OMRunnable.Time(OMRunnable.TimeUnit.SECOND, 14);
    }

    @Override
    public int getMaxProjectiles() {
        return 40;
    }
}
