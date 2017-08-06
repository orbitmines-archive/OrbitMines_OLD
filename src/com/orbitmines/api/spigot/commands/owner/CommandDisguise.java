package com.orbitmines.api.spigot.commands.owner;

import com.orbitmines.api.Message;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.spigot.Mob;
import com.orbitmines.api.spigot.commands.StaffCommand;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandDisguise extends StaffCommand {

    private final String[] alias = {"/disguise", "/dis", "/d"};

    public CommandDisguise() {
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
            if (a[1].equalsIgnoreCase("block")) {
                omp.sendMessage(new Message("§7Ongeldige Command. (§6/disguise block <id>§7)", "§7Invalid Usage. (§6/disguise block <id>§7)"));
            } else {
                try {
                    Mob mob = Mob.valueOf(a[1].toUpperCase());
                    omp.disguises().disguiseAsMob(mob);
                    omp.sendMessage(new Message("§7Vermomd als: §6" + mob.getName() + "§7.", "§7Disguised as: §6" + mob.getName() + "§7."));
                } catch (IllegalArgumentException ex) {
                    p.sendMessage("§7" + omp.getMessage(new Message("Ongeldige", "Invalid")) + " Disguise.");
                }
            }
        } else if (a.length == 3) {
            if (a[1].equalsIgnoreCase("block")) {
                try {
                    int id = Integer.parseInt(a[2]);
                    omp.disguises().disguiseAsBlock(id);

                    omp.sendMessage(new Message("§7Vermomd als: §6" + Material.getMaterial(id).toString() + "§7.", "§7Disguised as: §6" + Material.getMaterial(id).toString() + "§7."));
                } catch (IllegalArgumentException ex) {
                    p.sendMessage("§7" + omp.getMessage(new Message("Ongeldig", "Invalid")) + " Block ID.");
                }
            } else {
                p.sendMessage("§7" + omp.getMessage(new Message("Ongeldige", "Invalid")) + " Disguise.");
            }
        } else if (a.length == 4) {
            if (a[1].equalsIgnoreCase("player")) {
                Player p2 = Bukkit.getPlayer(a[2]);
                OMPlayer omp2 = OMPlayer.getPlayer(p2);

                if (p2 != null) {
                    try {
                        Mob mob = Mob.valueOf(a[3].toUpperCase());
                        omp2.disguises().disguiseAsMob(mob);
                        omp.sendMessage(new Message("§7Je hebt " + omp2.getName() + " §7vermomd als: §6" + mob.getName() + "§7.", "§7Disguised " + omp2.getName() + " §7as: §6" + mob.getName() + "§7."));
                        omp2.sendMessage(new Message("§7Vermomd als: §6" + mob.getName() + "§7.", "§7Disguised as: §6" + mob.getName() + "§7."));
                    } catch (IllegalArgumentException ex) {
                        p.sendMessage("§7" + omp.getMessage(new Message("Ongeldige", "Invalid")) + " Disguise.");
                    }
                } else {
                    omp.sendMessage(new Message("§7Player §6" + a[2] + " §7is niet §aOnline§7!", "§7Player §6" + a[2] + " §7isn't §aOnline§7!"));
                }
            } else if (a[1].equalsIgnoreCase("near")) {
                try {
                    int radius = Integer.parseInt(a[2]);

                    try {
                        Mob mob = Mob.valueOf(a[3].toUpperCase());
                        int amount = 1;

                        Message message = new Message("§7Vermomd als: §6" + mob.getName() + "§7.", "§7Disguised as: §6" + mob.getName() + "§7.");
                        for (Entity en : p.getNearbyEntities(radius, radius, radius)) {
                            if (en instanceof Player) {
                                amount++;
                                Player player = (Player) en;
                                OMPlayer omplayer = OMPlayer.getPlayer(player);
                                omplayer.disguises().disguiseAsMob(mob);
                                omplayer.sendMessage(message);
                            }
                        }

                        omp.disguises().disguiseAsMob(mob);
                        omp.sendMessage(new Message("§7Spelers in de buurt vermomd (§6" + amount + "§7) §7als: §6" + mob.getName() + "§7.", "§7Disguised near players (§6" + amount + "§7) §7as: §6" + mob.getName() + "§7."));
                        omp.sendMessage(message);

                    } catch (IllegalArgumentException ex) {
                        p.sendMessage("§7" + omp.getMessage(new Message("Ongeldige", "Invalid")) + " Disguise.");
                    }
                } catch (NumberFormatException ex) {
                    p.sendMessage("§7" + omp.getMessage(new Message("Ongeldige", "Invalid")) + " Radius.");
                }
            } else {
                p.sendMessage("§7" + omp.getMessage(new Message("Ongeldige", "Invalid")) + " Disguise.");
            }
        } else if (a.length == 5) {
            if (a[1].equalsIgnoreCase("player")) {
                Player p2 = Bukkit.getPlayer(a[2]);
                OMPlayer omp2 = OMPlayer.getPlayer(p2);

                if (p2 != null) {
                    if (a[3].equalsIgnoreCase("block")) {
                        try {
                            int id = Integer.parseInt(a[4]);
                            String material = Material.getMaterial(id).toString();

                            omp2.disguises().disguiseAsBlock(id);
                            omp.sendMessage(new Message("§7Je hebt " + omp2.getName() + " §7vermomd als: §6Block§7. (§6" + material + "§7)", "§7Disguised " + omp2.getName() + " §7as: §6Block§7. (§6" + material + "§7)"));
                            omp2.sendMessage(new Message("§7Vermomd als: §aBlock§7. (§6" + material + "§7)", "§7Disguised as: §aBlock§7. (§6" + material + "§7)"));
                        } catch (NumberFormatException ex) {
                            p.sendMessage("§7" + omp.getMessage(new Message("Ongeldig", "Invalid")) + " Block ID.");
                        }
                    } else {
                        p.sendMessage("§7" + omp.getMessage(new Message("Ongeldige", "Invalid")) + " Disguise.");
                    }
                } else {
                    omp.sendMessage(new Message("§7Player §6" + a[2] + " §7is niet §aOnline§7!", "§7Player §6" + a[2] + " §7isn't §aOnline§7!"));
                }
            } else if (a[1].equalsIgnoreCase("near")) {
                try {
                    int radius = Integer.parseInt(a[2]);

                    if (a[3].equalsIgnoreCase("block")) {
                        try {
                            int id = Integer.parseInt(a[4]);
                            String material = Material.getMaterial(id).toString();

                            int amount = 1;

                            Message message = new Message("§7Vermomd als: §6" + material + "§7.", "§7Disguised as: §6" + material + "§7.");
                            for (Entity en : p.getNearbyEntities(radius, radius, radius)) {
                                if (en instanceof Player) {
                                    amount++;
                                    Player player = (Player) en;
                                    OMPlayer omplayer = OMPlayer.getPlayer(player);
                                    omplayer.disguises().disguiseAsBlock(id);
                                    omplayer.sendMessage(message);
                                }
                            }

                            omp.disguises().disguiseAsBlock(id);
                            omp.sendMessage(new Message("§7Spelers in de buurt vermomd (§a" + amount + "§7) §7als: §aBlock§7. (§a" + material + "§7)", "§7Disguised near players (§a" + amount + "§7) §7as: §aBlock§7. (§a" + material + "§7)"));
                            omp.sendMessage(message);
                        } catch (NumberFormatException ex) {
                            p.sendMessage("§7" + omp.getMessage(new Message("Ongeldig", "Invalid")) + " Block ID.");
                        }
                    } else {
                        p.sendMessage("§7" + omp.getMessage(new Message("Ongeldige", "Invalid")) + " Disguise.");
                    }
                } catch (NumberFormatException ex) {
                    p.sendMessage("§7" + omp.getMessage(new Message("Ongeldige", "Invalid")) + " Radius.");
                }
            } else {
                p.sendMessage("§7" + omp.getMessage(new Message("Ongeldige", "Invalid")) + " Disguise.");
            }
        } else {
            p.sendMessage("§7§lMobs");
            p.sendMessage(" §6/" + a[0] + " (player | <player>) <mob>§7): ");

            StringBuilder stringBuilder = new StringBuilder(" §7§l" + omp.getMessage(new Message("Beschikbaar", "Available")) + "§7:");
            for (Mob mob : Mob.values()) {
                stringBuilder.append(" §6").append(mob.getName().replaceAll(" ", "_")).append("§7,");
            }
            p.sendMessage(stringBuilder.toString().substring(0, stringBuilder.length() - 3));
            p.sendMessage("§7§lBlocks");
            p.sendMessage(" §6/" + a[0] + " (player | <player>) block <id>");
            omp.sendMessage(new Message("§7§lVermom dichtstbijzijnde spelers in een Mob", "§7§lDisguise near to Mob"));
            p.sendMessage(" §6/" + a[0] + " near <radius> <mob>");
            omp.sendMessage(new Message("§7§lVermom dichtstbijzijnde spelers in een Block", "§7§lDisguise near to Block"));
            p.sendMessage(" §6/" + a[0] + " near <radius> block <id>");
        }
    }
}
