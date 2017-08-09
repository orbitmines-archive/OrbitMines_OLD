package com.orbitmines.bungee.commands.staff;

import com.orbitmines.api.StaffRank;
import com.orbitmines.bungee.commands.StaffCommand;
import com.orbitmines.bungee.handlers.BungeePlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
@Deprecated
public class CommandStaffHelp extends StaffCommand {

    private final String[] alias = {"/staffhelp"};

    public CommandStaffHelp() {
        super(StaffRank.BUILDER);
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void onDispatch(BungeePlayer omp, String[] a) {
        ProxiedPlayer p = omp.getPlayer();

        //TODO ALSO THROUGH MYSQL
        p.sendMessage("§6§lOrbitMines§4§lNetwork §7- §e§lStaff Commands:");
        if (omp.isEligible(StaffRank.OWNER)) {
            p.sendMessage(" §4§lOwner");
            p.sendMessage(" §7 /maintenance");
            p.sendMessage(" §7 /opmode");
            p.sendMessage(" §7 /freekits");
            p.sendMessage(" §7 /clearentities");
            p.sendMessage(" §7 /cagebuilder");
        }

        if (omp.isEligible(StaffRank.MODERATOR)) {
            p.sendMessage(" §b§lModerator");
            p.sendMessage(" §7 /ban <player>|<ip> <amount>y/m/d/h (reason)\n     §o-> /ban rienk222 30d Always Trolling");
            p.sendMessage(" §7 /unban <player>|<ip>");
            p.sendMessage(" §7 /kick <player> (reason)");
            p.sendMessage(" §7 /kickall (reason)");
            p.sendMessage(" §7 /staffmsg");
            p.sendMessage(" §7 /broadcast <message>");
            p.sendMessage(" §7 /allchat | /all");
            p.sendMessage(" §7 /silent");
            p.sendMessage(" §7 /say <message>");
            p.sendMessage(" §7 /lookup <uuid>|<player>|<ip>");
            p.sendMessage(" §7 /invsee <player>");
            p.sendMessage(" §7 /tp <player | player1> (player2 | x) (y) (z)");
            p.sendMessage(" §7 /fly (player)");
        }

        p.sendMessage(" §d§lBuilder §7(In Builderworld)");
        p.sendMessage(" §7 /fly (player)");
        p.sendMessage(" §7 /builderworld");
        p.sendMessage(" §7 /builderworld2");
    }
}
