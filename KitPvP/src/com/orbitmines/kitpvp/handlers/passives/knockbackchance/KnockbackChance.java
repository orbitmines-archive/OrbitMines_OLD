package com.orbitmines.kitpvp.handlers.passives.knockbackchance;

import com.orbitmines.api.utils.RandomUtils;
import com.orbitmines.kitpvp.KitPvP;
import com.orbitmines.kitpvp.Passive;
import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import com.orbitmines.kitpvp.handlers.passives.EntityDamageByEntityEventHandler;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public abstract class KnockbackChance extends Passive implements EntityDamageByEntityEventHandler {

    private KitPvP kitPvP;

    public KnockbackChance(int level) {
        super(Type.KNOCKBACK_CHANCE, level);

        kitPvP = KitPvP.getInstance();
    }

    public abstract int getKnockbackLevel();

    public abstract double getKnockbackChance();

    @Override
    public void onDamage(EntityDamageByEntityEvent event, KitPvPPlayer ompD, KitPvPPlayer ompE) {
        if (RandomUtils.RANDOM.nextDouble() >= getKnockbackChance())
            return;

        ItemStack item = ompD.getPlayer().getInventory().getItemInMainHand();
        item.addUnsafeEnchantment(Enchantment.KNOCKBACK, getKnockbackLevel());

        new BukkitRunnable() {
            @Override
            public void run() {
                item.removeEnchantment(Enchantment.KNOCKBACK);
            }
        }.runTaskLater(kitPvP, 1);
    }
}
