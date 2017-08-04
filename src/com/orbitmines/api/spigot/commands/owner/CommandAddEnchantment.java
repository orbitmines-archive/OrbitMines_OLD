package com.orbitmines.api.spigot.commands.owner;

import com.orbitmines.api.Message;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.spigot.commands.StaffCommand;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandAddEnchantment extends StaffCommand {

    private final String[] alias = {"/addench", "/addenchantment"};

    public CommandAddEnchantment() {
        super(StaffRank.OWNER);
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void onDispatch(OMPlayer omp, String[] a) {
        Player p = omp.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();

        if (item == null) {
            omp.sendMessage(new Message("§7Je hebt geen item in je hand.", "§7You don't have an item in your hand."));
            return;
        }

        if (a.length == 3) {
            Enchantment ench = Enchantment.getByName(a[1]);

            if (ench != null) {
                try {
                    int level = Integer.parseInt(a[2]);

                    item.addUnsafeEnchantment(ench, level);
                } catch (NumberFormatException ex) {
                    p.sendMessage("§7" + omp.getMessage(new Message("Ongeldig", "Invalid")) + " Level. (§6" + a[2] + "§7)");
                }
            } else {
                p.sendMessage("§7" + omp.getMessage(new Message("Onbekende", "Unknown")) + " Enchantment. (§6" + a[1] + "§7)");
            }
        } else {
            p.sendMessage("§7" + omp.getMessage(new Message("Gebruik", "Use")) + " §6" + a[0].toLowerCase() + " <enchantment> <level>§7.");
        }
    }
}
