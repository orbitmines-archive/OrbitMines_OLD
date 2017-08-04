package com.orbitmines.api.spigot.commands.owner;

import com.orbitmines.api.Message;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.spigot.commands.StaffCommand;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandGive extends StaffCommand {

    private final String[] alias = {"/feed", "/eat"};

    public CommandGive() {
        super(StaffRank.OWNER);
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void onDispatch(OMPlayer omp, String[] a) {
        Player p = omp.getPlayer();

        if (a.length == 3 || a.length == 4) {
            try {
                int amount = 64;
                if (a.length == 4)
                    amount = Integer.parseInt(a[3]);

                Player p2 = Bukkit.getPlayer(a[1]);
                OMPlayer omp2 = OMPlayer.getPlayer(p2);

                if (p2 != null) {
                    if (a[2].contains(":")) {
                        String[] itemString = a[2].split(":");

                        try {
                            int durability = Integer.parseInt(itemString[1]);

                            try {
                                int id = Integer.parseInt(itemString[0]);

                                if (p2 == p) {
                                    try {
                                        ItemStack item = new ItemStack(Material.getMaterial(id), amount);
                                        item.setDurability((short) durability);
                                        p.getInventory().addItem(item);
                                        p.updateInventory();

                                        omp.sendMessage(new Message("§7Je hebt jezelf §6§l" + item.getAmount() + " §6" + item.getType().toString().toLowerCase() + "§7 gegeven!", "§7You gave yourself §6§l" + item.getAmount() + " §6" + item.getType().toString().toLowerCase() + "§7!"));
                                    } catch (IllegalArgumentException ex) {
                                        p.sendMessage("§7" + omp.getMessage(new Message("Ongeldig", "Invalid")) + " Item ID.");
                                    }
                                } else {
                                    try {
                                        ItemStack item = new ItemStack(Material.getMaterial(id), amount);
                                        item.setDurability((short) durability);
                                        p2.getInventory().addItem(item);
                                        p2.updateInventory();

                                        String material = item.getType().toString().toLowerCase();
                                        omp.sendMessage(new Message("§7Je hebt " + omp2.getName() + " §6§l" + item.getAmount() + " §6" + material + "§7 gegeven!", "§7You gave " + omp2.getName() + " §6§l" + item.getAmount() + " §6" + material + "§7!"));
                                        omp2.sendMessage(new Message("§7" + omp.getName() + "§7 heeft je §6§l" + item.getAmount() + " §6" + material + "§7 gegeven!", "§7" + omp.getName() + "§7 gave you §6§l" + item.getAmount() + " §6" + material + "§7!"));
                                    } catch (IllegalArgumentException ex) {
                                        p.sendMessage("§7" + omp.getMessage(new Message("Ongeldig", "Invalid")) + " Item ID.");
                                    }
                                }

                            } catch (NumberFormatException ex) {
                                Material m = null;

                                for (Material ma : Material.values()) {
                                    if (ma.toString().equalsIgnoreCase(itemString[0])) {
                                        m = ma;
                                    } else if (ma.toString().replace("_", "").equalsIgnoreCase(itemString[0])) {
                                        m = ma;
                                    }
                                }

                                if (m != null) {
                                    if (p2 == p) {
                                        ItemStack item = new ItemStack(m, amount);
                                        item.setDurability((short) durability);
                                        p.getInventory().addItem(item);
                                        p.updateInventory();
                                        omp.sendMessage(new Message("§7Je hebt jezelf §6§l" + item.getAmount() + " §6" + item.getType().toString().toLowerCase() + "§7 gegeven!", "§7You gave yourself §6§l" + item.getAmount() + " §6" + item.getType().toString().toLowerCase() + "§7!"));
                                    } else {
                                        ItemStack item = new ItemStack(m, amount);
                                        item.setDurability((short) durability);
                                        p2.getInventory().addItem(item);
                                        p2.updateInventory();

                                        String material = item.getType().toString().toLowerCase();
                                        omp.sendMessage(new Message("§7Je hebt " + omp2.getName() + " §6§l" + item.getAmount() + " §6" + material + "§7 gegeven!", "§7You gave " + omp2.getName() + " §6§l" + item.getAmount() + " §6" + material + "§7!"));
                                        omp2.sendMessage(new Message("§7" + omp.getName() + "§7 heeft je §6§l" + item.getAmount() + " §6" + material + "§7 gegeven!", "§7" + omp.getName() + "§7 gave you §6§l" + item.getAmount() + " §6" + material + "§7!"));
                                    }
                                } else {
                                    p.sendMessage("§7" + omp.getMessage(new Message("Ongeldig", "Invalid")) + " Item ID.");
                                }
                            }
                        } catch (NumberFormatException ex) {
                            p.sendMessage("§7" + omp.getMessage(new Message("Ongeldige", "Invalid")) + " Item Durability.");
                        }
                    } else {
                        try {
                            int id = Integer.parseInt(a[2]);

                            if (p2 == p) {
                                try {
                                    ItemStack item = new ItemStack(Material.getMaterial(id), amount);
                                    p.getInventory().addItem(item);
                                    p.updateInventory();
                                    omp.sendMessage(new Message("§7Je hebt jezelf §6§l" + item.getAmount() + " §6" + item.getType().toString().toLowerCase() + "§7 gegeven!", "§7You gave yourself §6§l" + item.getAmount() + " §6" + item.getType().toString().toLowerCase() + "§7!"));
                                } catch (Exception ex) {
                                    p.sendMessage("§7" + omp.getMessage(new Message("Ongeldig", "Invalid")) + " Item ID.");
                                }
                            } else {
                                try {
                                    ItemStack item = new ItemStack(Material.getMaterial(id), amount);
                                    p2.getInventory().addItem(item);
                                    p2.updateInventory();

                                    String material = item.getType().toString().toLowerCase();
                                    omp.sendMessage(new Message("§7Je hebt " + omp2.getName() + " §6§l" + item.getAmount() + " §6" + material + "§7 gegeven!", "§7You gave " + omp2.getName() + " §6§l" + item.getAmount() + " §6" + material + "§7!"));
                                    omp2.sendMessage(new Message("§7" + omp.getName() + "§7 heeft je §6§l" + item.getAmount() + " §6" + material + "§7 gegeven!", "§7" + omp.getName() + "§7 gave you §6§l" + item.getAmount() + " §6" + material + "§7!"));
                                } catch (Exception ex) {
                                    p.sendMessage("§7" + omp.getMessage(new Message("Ongeldig", "Invalid")) + " Item ID.");
                                }
                            }

                        } catch (NumberFormatException ex) {
                            Material m = null;

                            for (Material ma : Material.values()) {
                                if (ma.toString().equalsIgnoreCase(a[2])) {
                                    m = ma;
                                } else if (ma.toString().replace("_", "").equalsIgnoreCase(a[2])) {
                                    m = ma;
                                }
                            }

                            if (m != null) {
                                if (p2 == p) {
                                    ItemStack item = new ItemStack(m, amount);
                                    p.getInventory().addItem(item);
                                    p.updateInventory();
                                    omp.sendMessage(new Message("§7Je hebt jezelf §6§l" + item.getAmount() + " §6" + item.getType().toString().toLowerCase() + "§7 gegeven!", "§7You gave yourself §6§l" + item.getAmount() + " §6" + item.getType().toString().toLowerCase() + "§7!"));
                                } else {
                                    ItemStack item = new ItemStack(m, amount);
                                    p2.getInventory().addItem(item);
                                    p2.updateInventory();

                                    String material = item.getType().toString().toLowerCase();
                                    omp.sendMessage(new Message("§7Je hebt " + omp2.getName() + " §6§l" + item.getAmount() + " §6" + material + "§7 gegeven!", "§7You gave " + omp2.getName() + " §6§l" + item.getAmount() + " §6" + material + "§7!"));
                                    omp2.sendMessage(new Message("§7" + omp.getName() + "§7 heeft je §6§l" + item.getAmount() + " §6" + material + "§7 gegeven!", "§7" + omp.getName() + "§7 gave you §6§l" + item.getAmount() + " §6" + material + "§7!"));
                                }
                            } else {
                                p.sendMessage("§7" + omp.getMessage(new Message("Ongeldig", "Invalid")) + " Item ID.");
                            }
                        }
                    }
                } else {
                    omp.sendMessage(new Message("§7Player §6" + a[1] + " §7is niet §aOnline§7!", "§7Player §6" + a[1] + " §7isn't §aOnline§7!"));
                }
            } catch (NumberFormatException ex) {
                omp.sendMessage(new Message("§6" + a[3] + "§7 is geen nummer.", "§6" + a[3] + "§7 isn't a number."));
            }
        } else {
            p.sendMessage("§7" + omp.getMessage(new Message("Ongeldige Command", "Invalid Command")) + ". (§6" + a[0].toLowerCase() + " <player> <item | id> (amount)§7)");
        }
    }
}
