package com.orbitmines.kitpvp.handlers.kits.drunk;

import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.handlers.Obtainable;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.itembuilders.PotionBuilder;
import com.orbitmines.api.spigot.handlers.kit.KitInteractive;
import com.orbitmines.api.spigot.nms.entity.EntityNms;
import com.orbitmines.api.spigot.runnable.OMRunnable;
import com.orbitmines.kitpvp.HealthRegenType;
import com.orbitmines.kitpvp.KitPvP;
import com.orbitmines.kitpvp.handlers.KitHandler;
import com.orbitmines.kitpvp.handlers.KitPvPKit;
import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import com.orbitmines.kitpvp.handlers.kits.Shooter;
import com.orbitmines.kitpvp.handlers.passives.bleed.Bleed_LvL_1;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public class Drunk_LvL_1 extends KitHandler implements Shooter {

    private EntityNms nms;

    public Drunk_LvL_1() {
        super(KitPvPKit.Type.DRUNK, 1, new Obtainable(KitPvP.COINS, 17500));

        nms = OrbitMinesApi.getApi().getNms().entity();
    }

    @Override
    protected KitInteractive registerKit() {
        KitInteractive kit = new KitInteractive(type.toString() + "-" + level);

        kit.setItem(0, add(new ItemBuilder(Material.GLASS_BOTTLE, 1, 0, name() + "§bWeapon").addEnchantment(Enchantment.DAMAGE_ALL, 6), new Bleed_LvL_1()).buildUnbreakable(true));
        kit.setItem(1, new ItemBuilder(Material.BOW, 1, 0, name() + "§bBow").buildUnbreakable(true));
        kit.setItem(2, new ItemBuilder(Material.ARROW, 10, 0, name() + "§bArrow").build());

        kit.setHelmet(new ItemBuilder(Material.CHAINMAIL_HELMET, 1, 0, name() + "§bHelmet").buildUnbreakable(true));
        kit.setChestplate(new ItemBuilder(Material.CHAINMAIL_CHESTPLATE, 1, 0, name() + "§bChestplate").buildUnbreakable(true));
        kit.setLeggings(new ItemBuilder(Material.CHAINMAIL_LEGGINGS, 1, 0, name() + "§bLeggings").buildUnbreakable(true));
        kit.setBoots(new ItemBuilder(Material.CHAINMAIL_BOOTS, 1, 0, name() + "§bBoots").buildUnbreakable(true));

        kit.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 2000000, 0));

        return kit;
    }

    @Override
    public void update(KitPvPPlayer omp) {
        Player p = omp.getPlayer();

        if (p.getHealth() <= 4) {
            if (!p.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
                p.addPotionEffect(new PotionBuilder(PotionEffectType.DAMAGE_RESISTANCE, 2000000, 0).build());
                p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_FALL, 5, 1);
            }
        } else if (p.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
            p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        }
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
        return omp.getPlayer().getHealth() <= 4 ? HealthRegenType.INSANE : HealthRegenType.NORMAL;
    }

    @Override
    public OMRunnable.Time getReceiveTime() {
        return new OMRunnable.Time(OMRunnable.TimeUnit.SECOND, 15);
    }

    @Override
    public int getMaxProjectiles() {
        return 10;
    }
}
