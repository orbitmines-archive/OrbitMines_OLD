package com.orbitmines.kitpvp.events;

import com.orbitmines.kitpvp.KitPvP;
import com.orbitmines.kitpvp.Mastery;
import com.orbitmines.kitpvp.Passive;
import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import com.orbitmines.kitpvp.handlers.passives.EntityDamageByEntityEventHandler;
import com.orbitmines.kitpvp.handlers.playerdata.MasteriesData;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public class DamageByEntityEvent implements Listener {

    private KitPvP kitPvP;

    public DamageByEntityEvent() {
        kitPvP = KitPvP.getInstance();
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player pD = (Player) event.getDamager();
            KitPvPPlayer ompD = KitPvPPlayer.getPlayer(pD);
            MasteriesData mD = ompD.masteries();

            if(ompD.isSpectator() || ompD.isPlayer() && ompD.getKitActive() == null)
                event.setCancelled(true);

            if (event.getEntity() instanceof Player) {
                Player pE = (Player) event.getEntity();
                KitPvPPlayer ompE = KitPvPPlayer.getPlayer(pE);
                MasteriesData mE = ompE.masteries();

                if (ompE.hasSpawnProtection()) {
                    event.setCancelled(true);
                    return;
                }

                // Masteries \\
                double meleeDamage = event.getDamage() * mD.getMasteryEffect(Mastery.MELEE);
                double meleeProtected = event.getDamage() * mE.getMasteryEffect(Mastery.MELEE_PROTECTION);

                // Kit Damage \\
                double kitDamage = ompD.getKitActive() == null ? 0 : (event.getDamage() * ompD.getKitActive().getDamageMultiplier()) - event.getDamage();

                event.setDamage(event.getDamage() + meleeProtected + meleeDamage + kitDamage);

                // Passives \\
                ItemStack item = pD.getInventory().getItemInMainHand();
                Passive passive = ompD.getKitActive().getPassive(item);

                if (passive == null || !(passive instanceof EntityDamageByEntityEventHandler))
                    return;

                ((EntityDamageByEntityEventHandler) passive).onDamage(event, ompD, ompE);
            }
        } else if (event.getDamager() instanceof Projectile && event.getEntity() instanceof Player) {
            Projectile proj = (Projectile) event.getDamager();
            Player pE = (Player) event.getEntity();
            KitPvPPlayer ompE = KitPvPPlayer.getPlayer(pE);
            MasteriesData mE = ompE.masteries();

            // Head Shot \\
            if (pE.getLocation().subtract(proj.getLocation()).getY() > 1.4) {
                event.setDamage(event.getDamage() * 1.5);
                proj.getWorld().playEffect(proj.getLocation(), Effect.STEP_SOUND, 152);
            }

            if (proj.getShooter() instanceof Player) {
                Player pD = (Player) proj.getShooter();
                KitPvPPlayer ompD = KitPvPPlayer.getPlayer(pD);
                MasteriesData mD = ompD.masteries();

                // Masteries \\
                double rangeDamage = event.getDamage() * mD.getMasteryEffect(Mastery.RANGE);
                double rangeProtected = event.getDamage() * mE.getMasteryEffect(Mastery.RANGE_PROTECTION);
                event.setDamage(event.getDamage() + rangeDamage + rangeProtected);
            }
        }
    }
}
