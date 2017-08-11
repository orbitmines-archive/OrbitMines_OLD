package com.orbitmines.kitpvp.events;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.chat.ActionBar;
import com.orbitmines.api.spigot.nms.entity.EntityNms;
import com.orbitmines.kitpvp.KitPvP;
import com.orbitmines.kitpvp.handlers.ActiveBooster;
import com.orbitmines.kitpvp.handlers.KitHandler;
import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import com.orbitmines.kitpvp.handlers.passives.DeathEventHandler;
import com.orbitmines.kitpvp.handlers.playerdata.KitPvPData;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public class DeathEvent implements Listener {

    private KitPvP kitPvP;

    public DeathEvent() {
        kitPvP = KitPvP.getInstance();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();
        KitPvPPlayer omp = KitPvPPlayer.getPlayer(p);
        KitPvPData data = omp.kitPvP();

        event.setDeathMessage(null);
        event.getDrops().clear();

        omp.clearInventory();
        omp.clearPotionEffects();
        omp.setKitActive(null);
        omp.setCurrentStreak(0);
        data.addDeath();

        EntityNms nms = kitPvP.getApi().getNms().entity();
        nms.setAttribute(p, EntityNms.Attribute.MAX_HEALTH, 20D);
        p.setHealth(20D);
        p.teleport(kitPvP.getSpawnLocation());

        if (p.getKiller() != null && p.getKiller() != p) {
            Player pK = p.getKiller();
            KitPvPPlayer ompK = KitPvPPlayer.getPlayer(pK);
            KitPvPData dataK = ompK.kitPvP();

            KitHandler handler = ompK.getKitActive();
            if (handler != null && handler instanceof DeathEventHandler)
                ((DeathEventHandler) handler).onKill(event, omp, ompK);


            int money;
            ActiveBooster booster = kitPvP.getBooster();
            if (booster == null) {
                money = (int) (10000 * ompK.getCoinBooster());
                int vipExtra = money - 10000;

                dataK.addCoins(money);
                omp.createKillHologram(ompK, money);
                pK.sendMessage("§6§l+10000 Coins");
                if (vipExtra != 0) {
                    pK.sendMessage("§6§l+" + vipExtra + " Coins §7(" + ompK.getVipRank().getRankString() + " §lVIP§7)");
                }
            } else {
                money = (int) (10000 * booster.getType().getMultiplier());
                int extra = money - 10000;
                money *= ompK.getCoinBooster();
                int vipExtra = money - 10000 - extra;

                dataK.addCoins(money);
                omp.createKillHologram(ompK, money);

                pK.sendMessage("§6§l+10000 Coins");
                pK.sendMessage("§6§l+" + extra + " Coins §7(§a" + booster.getPlayer() + "'s Booster§7)");
                if (vipExtra != 0) {
                    pK.sendMessage("§6§l+" + vipExtra + " Coins §7(" + ompK.getVipRank().getRankString() + " §lVIP§7)");
                }
            }

            ompK.setCurrentStreak(ompK.getCurrentStreak() + 1);
            dataK.addKill();
            pK.sendMessage("§f§l" + ompK.getMessage(new Message("Huidige", "Current")) + " Streak: §c§l" + ompK.getCurrentStreak() + " §f§l" + ompK.getMessage(new Message("Beste", "Best")) + " Streak: §c§l" + dataK.getBestStreak());
            if (dataK.getBestStreak() < ompK.getCurrentStreak()) {
                dataK.setBestStreak(ompK.getCurrentStreak());

                ompK.sendMessage(new Message("§f§lNieuw Streak Record: §c§l" + ompK.getCurrentStreak(), "§f§lNew Best Streak: §c§l" + ompK.getCurrentStreak()));
                pK.playSound(pK.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 5, 1);
            }

            ItemStack item = pK.getInventory().getItemInMainHand();
            if (item == null || item.getType() != Material.BOW) {
                new Message("§6" + omp.getColorName() + "§7 is gedood door §6" + ompK.getColorName() + "§7!", "§6" + omp.getColorName() + "§7 was killed by §6" + ompK.getColorName() + "§7!").broadcast();
            } else {
                new Message("§6" + omp.getColorName() + "§7 is doodgeschoten door §6" + ompK.getColorName() + "§7!", "§6" + omp.getColorName() + "§7 was shot by §6" + ompK.getColorName() + "§7!").broadcast();
            }

            dataK.addExp(2);
            if(dataK.isLevelUp()){
                dataK.setExp(dataK.getExp() - (int) dataK.getExpRequired());
                dataK.addLevel();
                new Message("§6" + ompK.getName() + " §7is nu level §6" + dataK.getLevel() + "§7!", "§6" + ompK.getName() + " §7reached level §6" + dataK.getLevel() + "§7!").broadcast();
            }
            dataK.updateLevel();

            ActionBar ab = new ActionBar(pK, "§6+" + money + " Coins§7, §e+" + (int) (2 * ompK.getExpBooster()) + " XP", 40);
            ab.send();

            int streak = ompK.getCurrentStreak();
            if(streak == 3 || streak == 5 || streak >= 10){
                new BukkitRunnable(){
                    public void run(){
                        new Message("§c§l" + ompK.getName() + "§7 heeft een §c§l" + streak + " Kill Streak§7!", "§c§l" + ompK.getName() + "§7 has a §c§l" + streak + " Kill Streak§7!").broadcast();
                    }
                }.runTaskLater(kitPvP, 1);
            }
        } else {
            omp.broadcastMessage(new Message("§6" + omp.getColorName() + "§7 is dood gegaan.", "§6" + omp.getColorName() + "§7 died."));
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                p.setVelocity(new Vector(0, 0, 0));
                p.setFireTicks(0);
                kitPvP.getLobbyKit(omp).setItems(p);
            }
        }.runTaskLater(kitPvP, 1);
    }
}
