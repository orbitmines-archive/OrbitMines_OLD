package com.orbitmines.kitpvp.handlers.kits.wizard;

import com.orbitmines.api.spigot.handlers.Obtainable;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.itembuilders.PotionBuilder;
import com.orbitmines.api.spigot.handlers.itembuilders.PotionItemBuilder;
import com.orbitmines.api.spigot.handlers.kit.KitInteractive;
import com.orbitmines.api.utils.RandomUtils;
import com.orbitmines.kitpvp.HealthRegenType;
import com.orbitmines.kitpvp.KitPvP;
import com.orbitmines.kitpvp.handlers.KitHandler;
import com.orbitmines.kitpvp.handlers.KitPvPKit;
import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import com.orbitmines.kitpvp.handlers.passives.DeathEventHandler;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffectType;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public class Wizard_LvL_1 extends KitHandler implements DeathEventHandler {

    public Wizard_LvL_1() {
        super(KitPvPKit.Type.WIZARD, 1, new Obtainable(KitPvP.COINS, 10000));
    }

    @Override
    protected KitInteractive registerKit() {
        KitInteractive kit = new KitInteractive(type.toString() + "-" + level);

        kit.setItem(0, new ItemBuilder(Material.STONE_SWORD, 1, 0, name() + "§bWeapon").buildUnbreakable(true));
        kit.setItem(1, new PotionItemBuilder(PotionItemBuilder.Type.SPLASH, new PotionBuilder(PotionEffectType.FIRE_RESISTANCE, 20 * 30, 0, false, true, Color.ORANGE), false, 1, name() + "§6Fire Resistance Potion").build());
        kit.setItem(2, new PotionItemBuilder(PotionItemBuilder.Type.SPLASH, new PotionBuilder(PotionEffectType.WEAKNESS, 20 * 15, 0, false, true, Color.GRAY), false, 1, name() + "§8Weakness Potion").build());
        kit.setItem(3, new PotionItemBuilder(PotionItemBuilder.Type.SPLASH, new PotionBuilder(PotionEffectType.HARM, 0, 0, false, true, Color.MAROON), false, 1, name() + "§4Harming Potion").build());
        kit.setItem(4, new PotionItemBuilder(PotionItemBuilder.Type.SPLASH, new PotionBuilder(PotionEffectType.SPEED, 20 * 60, 0, false, true, Color.AQUA), false, 1, name() + "§bSpeed Potion").build());
        kit.setItem(5, new PotionItemBuilder(PotionItemBuilder.Type.SPLASH, new PotionBuilder(PotionEffectType.REGENERATION, 20 * 20, 0, false, true, Color.FUCHSIA), false, 1, name() + "§dRegeneration Potion").build());

        kit.setHelmet(new ItemBuilder(Material.GOLD_HELMET, 1, 0, name() + "§bHelmet").buildUnbreakable(true));
        kit.setChestplate(new ItemBuilder(Material.GOLD_CHESTPLATE, 1, 0, name() + "§bChestplate").buildUnbreakable(true));
        kit.setLeggings(new ItemBuilder(Material.GOLD_LEGGINGS, 1, 0, name() + "§bLeggings").buildUnbreakable(true));
        kit.setBoots(new ItemBuilder(Material.GOLD_BOOTS, 1, 0, name() + "§bBoots").buildUnbreakable(true));

        return kit;
    }

    @Override
    public void update(KitPvPPlayer omp) {

    }

    @Override
    public double getMaxHealth() {
        return 22.0D;
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
        return HealthRegenType.HIGH;
    }

    @Override
    public void onKill(PlayerDeathEvent event, KitPvPPlayer died, KitPvPPlayer killer) {
        if (RandomUtils.RANDOM.nextDouble() < 0.125) {
            Player p = killer.getPlayer();
            p.getInventory().addItem(kit.getItem(RandomUtils.random(1, 5)));
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_DRINK, 5, 1);
        }
    }
}
