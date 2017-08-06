package com.orbitmines.api.spigot.enablers;

import com.orbitmines.api.Data;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.playerdata.HatData;
import com.orbitmines.api.spigot.handlers.playerdata.PlayerData;
import com.orbitmines.api.spigot.utils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class HatEnabler extends Enabler implements Listener {

    @Override
    public void onEnable() {
        api.enableData(Data.HATS, new Data.Register() {
            @Override
            public PlayerData getData(OMPlayer omp) {
                return new HatData(omp);
            }
        });

        api.getServer().getPluginManager().registerEvents(this, api);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        OMPlayer omp = OMPlayer.getPlayer(p);
        HatData data = omp.hats();

        if (!omp.canReceiveVelocity() || !data.hasHatsBlockTrail() || !data.hasHatEnabled())
            return;

        final Block b = p.getLocation().getBlock().getRelative(BlockFace.DOWN);
        Material mb = b.getType();

        if(!b.isEmpty() && !WorldUtils.CANNOT_TRANSFORM.contains(mb)){
            Material m = p.getInventory().getHelmet().getType();
            byte mB = (byte) p.getInventory().getHelmet().getDurability();

            for(Player player : Bukkit.getOnlinePlayers()){
                player.sendBlockChange(b.getLocation(), m, mB);
            }

            new BukkitRunnable(){
                public void run(){
                    for(Player player : Bukkit.getOnlinePlayers()){
                        player.sendBlockChange(b.getLocation(), b.getType(), b.getData());
                    }
                }
            }.runTaskLater(api, 40);
        }
    }
}
