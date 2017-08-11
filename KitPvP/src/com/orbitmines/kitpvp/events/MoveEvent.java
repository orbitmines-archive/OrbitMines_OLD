package com.orbitmines.kitpvp.events;

import com.orbitmines.api.Message;
import com.orbitmines.kitpvp.KitPvP;
import com.orbitmines.kitpvp.Passive;
import com.orbitmines.kitpvp.handlers.KitHandler;
import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import com.orbitmines.kitpvp.handlers.passives.BootsMoveEventHandler;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
@Deprecated //TODO Change borders
public class MoveEvent implements Listener {

    private KitPvP kitPvP;

    public MoveEvent() {
        kitPvP = KitPvP.getInstance();
    }

    @EventHandler
    public void onDeath(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        KitPvPPlayer omp = KitPvPPlayer.getPlayer(p);

        KitHandler handler = omp.getKitActive();

        if(handler != null || omp.isSpectator()){
            if(p.getLocation().getY() >= kitPvP.getCurrentMap().getMaxY()){
                p.setVelocity(new Vector(0, 0, 0));
                Location l = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() -2, p.getLocation().getZ(), p.getLocation().getYaw(), p.getLocation().getPitch());
                p.teleport(l);
                omp.sendMessage(new Message("§7§lBlijf in de Arena!", "§7§lStay in the Arena!"));
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_SCREAM, 5, 1);
            }

            if (handler != null && p.getInventory().getBoots() != null) {
                Passive passive = handler.getPassive(p.getInventory().getBoots());

                if (passive == null || !(passive instanceof BootsMoveEventHandler))
                    return;

                ((BootsMoveEventHandler) passive).onMove(event, omp);
            }
        }
    }
}
