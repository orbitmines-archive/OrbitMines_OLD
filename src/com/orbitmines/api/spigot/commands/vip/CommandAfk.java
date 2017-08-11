package com.orbitmines.api.spigot.commands.vip;

import com.orbitmines.api.Message;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.spigot.commands.VipCommand;
import com.orbitmines.api.spigot.handlers.OMPlayer;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandAfk extends VipCommand {

    private final String[] alias = {"/afk"};

    public CommandAfk() {
        super(VipRank.IRON);
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void onDispatch(OMPlayer omp, String[] a) {
        if (omp.isAfk()) {
            omp.noLongerAfk();
        } else {
            if (a.length == 1) {
                omp.setAfk("null");
            } else if (a.length == 2) {
                if (omp.isEligible(VipRank.DIAMOND)) {
                    if (a[1].length() <= 20) {
                        String afkMessage = a[1];
                        if (omp.isEligible(VipRank.EMERALD))
                            afkMessage = a[1].replaceAll("&a", "§a").replaceAll("&b", "§b").replaceAll("&c", "§c").replaceAll("&d", "§d").replaceAll("&e", "§e").replaceAll("&f", "§f").replaceAll("&0", "§0").replaceAll("&1", "§1").replaceAll("&2", "§2").replaceAll("&3", "§3").replaceAll("&4", "§4").replaceAll("&5", "§5").replaceAll("&6", "§6").replaceAll("&7", "§7").replaceAll("&8", "§8").replaceAll("&9", "§9");

                        omp.setAfk(afkMessage);
                    } else {
                        omp.sendMessage(new Message("§7Jouw §6afk tekst§7 kan niet langer dan §620 karakters§7 zijn!", "§7Your §6afk text§7 can't be longer than §620 characters§7!"));
                    }
                } else {
                    omp.msgRequiredVipRank(VipRank.DIAMOND);
                }
            } else {
                omp.sendMessage(new Message("§7Ongeldige Command. (§6/afk §7of §6/afk <reden>§7)", "§7Invalid Usage. (§6/afk §7or §6/afk <reason>§7)"));
            }
        }
    }
}
