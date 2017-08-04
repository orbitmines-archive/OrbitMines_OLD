package com.orbitmines.api.spigot.commands.moderator;

import com.orbitmines.api.Message;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.spigot.commands.StaffCommand;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandTeleport extends StaffCommand {

    private final String[] alias = {"/tp", "/teleport"};

    public CommandTeleport() {
        super(StaffRank.MODERATOR);
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void onDispatch(OMPlayer omp, String[] a) {
        Player p = omp.getPlayer();

        if (a.length == 2) {
            Player p2 = Bukkit.getPlayer(a[1]);
            OMPlayer omp2 = OMPlayer.getPlayer(p2);

            if (p2 != null) {
                if (p2 != p) {
                    p.teleport(p2);
                    omp.sendMessage(new Message("§7Geteleporteerd naar " + omp2.getName() + "§7!", "§7Teleported to " + omp2.getName() + "§7!"));
                } else {
                    omp.sendMessage(new Message("§7Je kan niet naar jezelf toe §6teleporten§7!", "§7You can't §6teleport§7 to yourself!"));
                }
            } else {
                omp.sendMessage(new Message("§7Player §6" + a[1] + " §7is niet §aOnline§7!", "§7Player §6" + a[1] + " §7isn't §aOnline§7!"));
            }
        } else if (a.length == 3) {
            Player p2 = Bukkit.getPlayer(a[1]);
            Player p3 = Bukkit.getPlayer(a[2]);
            OMPlayer omp2 = OMPlayer.getPlayer(p2);
            OMPlayer omp3 = OMPlayer.getPlayer(p3);

            if (p2 != null) {
                if (p3 != null) {
                    p2.teleport(p3);
                    omp.sendMessage(new Message("§7Je hebt " + omp2.getName()+ "§7 naar " + omp3.getName() + "§7 geteleporteerd!", "§7Teleported " + omp2.getName() + "§7 to " + omp3.getName() + "§7!"));
                } else {
                    omp.sendMessage(new Message("§7Player §6" + a[2] + " §7is niet §aOnline§7!", "§7Player §6" + a[2] + " §7isn't §aOnline§7!"));
                }
            } else {
                omp.sendMessage(new Message("§7Player §6" + a[1] + " §7is niet §aOnline§7!", "§7Player §6" + a[1] + " §7isn't §aOnline§7!"));
            }
        } else if (a.length == 5) {
            Player p2 = Bukkit.getPlayer(a[1]);
            OMPlayer omp2 = OMPlayer.getPlayer(p2);

            if (p2 != null) {
                try {
                    int x = Integer.parseInt(a[2]);
                    int y = Integer.parseInt(a[3]);
                    int z = Integer.parseInt(a[4]);

                    Location l = new Location(p2.getWorld(), x, y, z, p2.getLocation().getYaw(), p2.getLocation().getPitch());

                    p2.teleport(l);

                    if (p2 != p) {
                        omp.sendMessage(new Message("§7Je hebt " + omp2.getName() + "§7 naar §6" + x + "§7, §6" + y + "§7, §6" + z + "§7 geteleporteerd!", "§7Teleported " + omp2.getName() + "§7 to §6" + x + "§7, §6" + y + "§7, §6" + z + "§7!"));
                    } else {
                        omp.sendMessage(new Message("§7Geteleporteerd naar §6" + x + "§7, §6" + y + "§7, §6" + z + "§7!" ,"§7Teleported to §6" + x + "§7, §6" + y + "§7, §6" + z + "§7!" ));
                    }
                } catch (NumberFormatException ex) {
                    p.sendMessage(omp.getMessage(new Message("Ongeldige", "Invalid")) + " Cords. (x/y/z)");
                }
            } else {
                omp.sendMessage(new Message("§7Player §6" + a[1] + " §7is niet §aOnline§7!", "§7Player §6" + a[1] + " §7isn't §aOnline§7!"));
            }
        } else {
            p.sendMessage("§7" + omp.getMessage(new Message("Ongeldige Command", "Invalid Command")) + ". §7(§6" + a[0].toLowerCase() + " <player | player1> (player2 | x) (y) (z)");
        }
    }
}
