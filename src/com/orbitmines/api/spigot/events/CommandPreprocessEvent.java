package com.orbitmines.api.spigot.events;

import com.orbitmines.api.Server;
import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.commands.Command;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class CommandPreprocessEvent implements Listener {

    @Deprecated
    private List<String> list = Arrays.asList("/abandonclaim", "/abandontoplevelclaim", "/abandonallclaims", "/trust", "/untrust", "/containertrust", "/accesstrust", "/permissiontrust", "/subdivideclaims", "/restrictsubclaim", "/adjustbonusclaimblocks", "/adjustbonusclaimblocksall", "/setaccruedclaimblocks", "/deleteclaim", "/deleteallclaims", "/deleteclaimsinworld", "/deleteuserclaimsinworld", "/adminclaims", "/restorenature", "/restorenatureaggressive", "/restorenaturefill", "/basicclaims", "/extendclaim",
            "/claim", "/trapped", "/trustlist", "/ignoreclaims", "/deletealladminclaims", "/adminclaimslist", "/transferclaim", "/unlockdrops", "/claimslist", "/claimexplosions", "/softmute",
            "/givepet", "/claimbook");

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPreCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        OMPlayer omp = OMPlayer.getPlayer(p);
        String[] a = e.getMessage().split(" ");

        Command command = Command.getCommand(a[0]);

        if (command == null) {
            if (omp.isOpMode())
                return;

            if (OrbitMinesApi.getApi().server().getServerType() == Server.SURVIVAL && list.contains(a[0]))
                return;

            e.setCancelled(true);
            omp.msgUnknownCommand(a[0]);
            return;
        }

        e.setCancelled(true);
        command.dispatch(omp, a);
    }
}
