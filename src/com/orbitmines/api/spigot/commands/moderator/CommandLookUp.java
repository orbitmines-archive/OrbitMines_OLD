package com.orbitmines.api.spigot.commands.moderator;

import com.orbitmines.api.Message;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.spigot.commands.StaffCommand;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.chat.ComponentMessage;
import com.orbitmines.api.utils.uuid.UUIDUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandLookUp extends StaffCommand {

    private final String[] alias = {"/lookup", "/uuid"};

    public CommandLookUp() {
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
            UUID uuid = UUIDUtils.getUUID(a[1]);

            if (uuid != null) {
                p.sendMessage("");
                omp.sendMessage(new Message("§6" + a[1] + "'s §7UUID info laden...", "§7Loading §6" + a[1] + "'s §7UUID info..."));

                send(omp, uuid);
            } else {
                if (a[1].length() > 16) {
                    uuid = UUID.fromString(a[1]);

                    p.sendMessage("");
                    omp.sendMessage(new Message("§6" + a[1] + "§7 info laden...", "§7Loading §6" + a[1] + "§7 info..."));

                    send(omp, uuid);
                } else {
                    p.sendMessage("§7" + omp.getMessage(new Message("Ongeldige", "Invalid")) + " Player. (§6" + a[0].toLowerCase() + " <player>§7)");
                }
            }
        } else {
            p.sendMessage("§7" + omp.getMessage(new Message("Ongeldige Command", "Invalid Command")) + ". (§6" + a[0].toLowerCase() + " <player | uuid>§7)");
        }
    }

    private void send(OMPlayer omp, UUID uuid) {
        Player p = omp.getPlayer();

        ComponentMessage cm = new ComponentMessage();
        cm.addPart(" §7UUID: ");
        cm.addPart("§6" + uuid.toString(), ClickEvent.Action.SUGGEST_COMMAND, uuid.toString(), HoverEvent.Action.SHOW_TEXT, omp.getMessage(new Message("§7Kopieer §6UUID§7.", "§7Copy §6UUID§7.")));
        cm.addPart("§7.");
        cm.send(p);

        omp.sendMessage(new Message(" §7Naam Geschiedenis:", " §7Name History:"));
        HashMap<String, String> names = UUIDUtils.getNames(uuid);
        for (String name : names.keySet()) {
            if (names.get(name) != null) {
                p.sendMessage("  §6" + name + " " + names.get(name));
            } else {
                p.sendMessage("  §6" + name);
            }
        }
    }
}
