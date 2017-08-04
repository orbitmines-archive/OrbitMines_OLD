package com.orbitmines.api.spigot.commands.vip;

import com.orbitmines.api.Message;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.spigot.commands.VipCommand;
import com.orbitmines.api.spigot.handlers.OMPlayer;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandNick extends VipCommand {

    private final String[] alias = {"/nick"};

    public CommandNick() {
        super(VipRank.GOLD);
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void onDispatch(OMPlayer omp, String[] a) {
        if (a.length == 2) {
            if (a[1].length() <= 30) {

                if (a[1].equalsIgnoreCase("off")) {
                    omp.sendMessage(new Message("§7Je §6nickname§7 is verwijderd!", "§7Removed your §6nickname§7!"));
                    omp.general().setNickName(null);
                } else {
                    if (omp.isEligible(VipRank.EMERALD)) {
                        String newNickName = a[1].replace("&a", "§a").replace("&b", "§b").replace("&c", "§c").replace("&d", "§d").replace("&e", "§e").replace("&f", "§f").replace("&0", "§0").replace("&1", "§1").replace("&2", "§2").replace("&3", "§3").replace("&4", "§4").replace("&5", "§5").replace("&6", "§6").replace("&7", "§7").replace("&8", "§8").replace("&9", "§9").replace("&r", "§r").replace("&k", "§k").replace("&m", "§m").replace("&n", "§n").replace("&l", "§l");
                        omp.sendMessage(new Message("§7Je §6nickname§7 is veranderd in '§a" + newNickName + "§7'.", "§7Changed your §6nickname§7 to '§a" + newNickName + "§7'."));
                        omp.general().setNickName(newNickName);
                    } else if (omp.isEligible(VipRank.DIAMOND)) {
                        String newNickName = a[1].replace("&a", "§a").replace("&b", "§b").replace("&c", "§c").replace("&d", "§d").replace("&e", "§e").replace("&f", "§f").replace("&0", "§0").replace("&1", "§1").replace("&2", "§2").replace("&3", "§3").replace("&4", "§4").replace("&5", "§5").replace("&6", "§6").replace("&7", "§7").replace("&8", "§8").replace("&9", "§9");
                        omp.sendMessage(new Message("§7Je §6nickname§7 is veranderd in '§a" + newNickName + "§7'.", "§7Changed your §6nickname§7 to '§a" + newNickName + "§7'."));
                        omp.general().setNickName(newNickName);
                    } else {
                        omp.sendMessage(new Message("§7Je §6nickname§7 is veranderd in '§a" + a[1] + "§7'.", "§7Changed your §6nickname§7 to '§a" + a[1] + "§7'."));
                        omp.general().setNickName(a[1]);
                    }
                }
            } else {
                omp.sendMessage(new Message("§7Je §6nickname§7 kan niet langer dan §630 karakters§7 zijn!", "§7Your §6nickname§7 cannot be longer than §630 characters§7!"));
            }
        } else {
            omp.getPlayer().sendMessage("§7" + omp.getMessage(new Message("Ongeldige Command", "Invalid Command"))+ ". (§6/nick <nickname | off>§7)");
        }
    }
}
