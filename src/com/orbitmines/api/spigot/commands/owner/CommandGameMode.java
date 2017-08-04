package com.orbitmines.api.spigot.commands.owner;

import com.orbitmines.api.Message;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.spigot.commands.StaffCommand;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandGameMode extends StaffCommand {

    private final String[] alias = {"/gamemode", "/gm"};

    public CommandGameMode() {
        super(StaffRank.OWNER);
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void onDispatch(OMPlayer omp, String[] a) {
        Player p = omp.getPlayer();

        if (a.length == 2) {
            GameMode gameMode = PlayerUtils.getGameMode(a[1]);

            if (gameMode != null) {
                p.setGameMode(gameMode);
                omp.sendMessage(new Message("§7Je §6GameMode§7 is nu " + PlayerUtils.name(gameMode) + "§7!", "§7Set your §6GameMode§7 to " + PlayerUtils.name(gameMode) + "§7!"));
            } else {
                p.sendMessage("§7" + omp.getMessage(new Message("Ongeldige Command", "Invalid Command")) + ". (§6" + a[0] + " s|c|a|spec§7)");
            }
        } else if (a.length == 3) {
            Player p2 = Bukkit.getPlayer(a[2]);
            GameMode gameMode = PlayerUtils.getGameMode(a[1]);

            if (p2 != null) {
                if (p2 == p) {
                    if (gameMode != null) {
                        p.setGameMode(gameMode);
                        omp.sendMessage(new Message("§7Je §6GameMode§7 is nu " + PlayerUtils.name(gameMode) + "§7!", "§7Set your §6GameMode§7 to " + PlayerUtils.name(gameMode) + "§7!"));
                    } else {
                        p.sendMessage("§7" + omp.getMessage(new Message("Ongeldige Command", "Invalid Command")) + ". (§6" + a[0] + " s|c|a|spec <player>§7)");
                    }
                } else {
                    if (gameMode != null) {
                        OMPlayer omp2 = OMPlayer.getPlayer(p2);
                        String name = PlayerUtils.name(gameMode);

                        p2.setGameMode(gameMode);
                        omp.sendMessage(new Message(omp2.getName() + "'s §6GameMode§7 is nu " + name + "§7!", "§7Set " + omp2.getName() + "'s §6GameMode§7 to " + name + "§7!"));
                        omp2.sendMessage(new Message("§7Je §6GameMode§7 is nu " + name + "§7 door " + omp.getName() + "§7!", omp.getName() + " §7Set your §6GameMode§7 to " + name + "§7!"));
                    } else {
                        p.sendMessage("§7" + omp.getMessage(new Message("Ongeldige Command", "Invalid Command")) + ". (§6" + a[0] + " s|c|a|spec <player>§7)");
                    }
                }
            } else {
                omp.sendMessage(new Message("§7Player §6" + a[2] + " §7is niet §aOnline§7!", "§7Player §6" + a[2] + " §7isn't §aOnline§7!"));
            }
        } else {
            p.sendMessage("§7" + omp.getMessage(new Message("Ongeldige Command", "Invalid Command")) + ". (§6" + a[0] + " s|c|a|spec§7)");
        }
    }
}
