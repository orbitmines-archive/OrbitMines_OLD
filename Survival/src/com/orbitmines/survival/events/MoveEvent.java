package com.orbitmines.survival.events;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.Cooldown;
import com.orbitmines.api.spigot.handlers.chat.Title;
import com.orbitmines.survival.Survival;
import com.orbitmines.survival.handlers.SurvivalPlayer;
import com.orbitmines.survival.handlers.Teleportable;
import com.orbitmines.survival.handlers.WorldPortal;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.PlayerData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 12-8-2017
*/
public class MoveEvent implements Listener {

    private Survival survival;
    private Map<Player, BukkitTask> fly;

    public MoveEvent() {
        survival = Survival.getInstance();
        fly = new HashMap<>();
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        SurvivalPlayer omp = SurvivalPlayer.getPlayer(p);

        if (omp.getCooldowns().containsKey(Cooldown.TELEPORTING)) {
            Location l = omp.getTpLocation();
            if (p.getLocation().getX() == l.getX() && p.getLocation().getY() == l.getY() && p.getLocation().getZ() == l.getZ())
                return;

            omp.removeCooldown(Cooldown.TELEPORTING);

            Teleportable teleportable = omp.getTeleportingTo();
            Title t = new Title("", omp.getMessage(new Message(teleportable.getColor().getChatColor() + teleportable.getName() + " §7Teleportatie §c§lGeannuleerd§7.", "§c§lCancelled " + teleportable.getColor().getChatColor() + teleportable.getName() + " §7Teleportation.")), 0, 40, 20);
            t.send(p);

            omp.setTeleportingTo(null);
        }

        if (p.getWorld().getName().equals(survival.getLobby().getName())) {
            Block b = p.getWorld().getBlockAt(p.getLocation());

            for (WorldPortal portal : WorldPortal.getPortals()) {
                if (portal.inPortal(omp, b))
                    break;
            }

            return;
        }

        if (omp.isOpMode())
            return;

        if (p.getWorld().getName().equals(survival.getWorld().getName())) {
            PlayerData playerData = GriefPrevention.instance.dataStore.getPlayerData(p.getUniqueId());
            Claim claim = GriefPrevention.instance.dataStore.getClaimAt(p.getLocation(), true, playerData.lastClaim);

            if (claim == null || claim.allowBuild(p, Material.AIR) != null) {
                if (p.isFlying()) {
                    disableFly(p);

                    omp.sendMessage(new Message("§7Je kan alleen vliegen in je claims, of claims waar je '/trust' hebt.", "§7You are only allowed to fly in your claims, and claims where you have '/trust'."));
                }
            } else if (fly.containsKey(p)) {
                p.setAllowFlight(true);
                p.setFlying(true);

                survival.getNoFall().remove(p);
                fly.get(p).cancel();
                fly.remove(p);
            }
        } else {
            if (p.isFlying()) {
                disableFly(p);

                if (p.getWorld().getName().equals(survival.getWorld().getName() + "_nether"))
                    omp.sendMessage(new Message("§7Het is niet toegestaan om in de §c§lNether§7 te vliegen.", "§7You are not allowed to fly in the §c§lNether§7."));
                else if (p.getWorld().getName().equals(survival.getWorld().getName() + "_nether"))
                    omp.sendMessage(new Message("§7Het is niet toegestaan om in de §lEnd§7 te vliegen.", "§7You are not allowed to fly in the §lEnd§7."));
            }
        }
    }

    private void disableFly(Player p) {
        p.setFlying(false);
        p.setAllowFlight(false);
        survival.getNoFall().add(p);

        fly.put(p, new BukkitRunnable() {
            @Override
            public void run() {
                if (survival.getNoFall().contains(p))
                    survival.getNoFall().remove(p);
            }
        }.runTaskLater(survival, 150));
    }
}
