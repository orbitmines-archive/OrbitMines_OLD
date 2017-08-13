package com.orbitmines.survival.commands;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.chat.Title;
import com.orbitmines.survival.Survival;
import com.orbitmines.survival.handlers.SurvivalPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Fadi on 7-9-2016.
 */
@Deprecated
public class SurvivalConsoleCommandExecuter implements CommandExecutor {

    private Survival survival;

    public SurvivalConsoleCommandExecuter() {
        this.survival = Survival.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String l, String[] a) {
        SurvivalPlayer omp = null;
        if (sender instanceof Player)
            omp = SurvivalPlayer.getPlayer((Player) sender);

        if (cmd.getName().equalsIgnoreCase("money")) {
            if (sender instanceof ConsoleCommandSender || sender instanceof Player && omp.isOpMode()) {
                if (a.length == 0) {
                    if (sender instanceof Player) {
                        int money = omp.survival().getMoney();
                        omp.sendMessage(new Message("§7Jouw Geld: §2§l" + money + "$", "§7Your Money: §2§l" + money + "$"));
                    }
                } else if (a.length == 3) {
                    if (a[0].equalsIgnoreCase("give")) {
                        int amount = Integer.parseInt(a[2]);
                        Player player = Bukkit.getPlayer(a[1]);

                        if (player != null) {
                            omp.survival().addMoney(amount);

                            sender.sendMessage("§7You gave §2" + a[1] + " §2§l" + a[2] + "$§7!");
                        } else {
                            sender.sendMessage("§7Player §2" + a[1] + " §7isn't §aOnline§7!");
                        }
                    } else if (a[0].equalsIgnoreCase("remove")) {
                        int amount = Integer.parseInt(a[2]);
                        Player player = Bukkit.getPlayer(a[1]);

                        if (player != null) {
                            omp.survival().removeMoney(amount);

                            sender.sendMessage("§7You removed §2§l" + a[2] + "$§7 from §2" + a[1] + "§7!");
                        } else {
                            sender.sendMessage("§7Player §2" + a[1] + " §7isn't §aOnline§7!");
                        }

                    } else {
                        sender.sendMessage("§7/money give|remove <player> <amount>");
                    }

                } else {
                    sender.sendMessage("§7/money give|remove <player> <amount>");
                }
            } else {
                int money = omp.survival().getMoney();
                omp.sendMessage(new Message("§7Jouw Geld: §2§l" + money + "$", "§7Your Money: §2§l" + money + "$"));
            }
        } else if (cmd.getName().equalsIgnoreCase("addhomes")) {
            if (sender instanceof ConsoleCommandSender || sender instanceof Player && omp.isOpMode()) {
                if (a.length == 2) {
                    final int amount = Integer.parseInt(a[1]);
                    final Player player = Bukkit.getPlayer(a[0]);

                    if (player != null) {
                        omp.survival().setExtraHomes(omp.survival().getExtraHomes() + amount);

                        sender.sendMessage("§7You gave §2" + a[0] + " §6" + a[1] + " Homes§7!");

                        new BukkitRunnable() {
                            public void run() {
                                Title t = new Title("", "§6+" + amount + " Homes", 20, 40, 20);
                                t.send(player);
                            }
                        }.runTaskLater(survival, 40);
                    } else {
                        sender.sendMessage("§7Player §2" + a[0] + " §7isn't §aOnline§7!");
                    }
                } else {
                    sender.sendMessage("§7/addhomes <player> <amount>");
                }
            } else {
                omp.msgUnknownCommand("/" + cmd.getName());
            }
//        } else if (cmd.getName().equalsIgnoreCase("addshops")) {
//            if (sender instanceof ConsoleCommandSender || sender instanceof Player && omp.isOpMode()) {
//                if (a.length == 2) {
//                    final int amount = Integer.parseInt(a[1]);
//                    final Player player = Bukkit.getPlayer(a[0]);
//
//                    if (player != null) {
//                        omp.survival().setExtraShops(omp.survival().getExtraShops() + amount);
//
//                        sender.sendMessage("§7You gave §2" + a[0] + " §6" + a[1] + " Chest Shops§7!");
//
//                        new BukkitRunnable() {
//                            public void run() {
//                                Title t = new Title("", "§6+" + amount + " Chest Shops", 20, 40, 20);
//                                t.send(player);
//                            }
//                        }.runTaskLater(survival, 40);
//                    } else {
//                        sender.sendMessage("§7Player §2" + a[0] + " §7isn't §aOnline§7!");
//                    }
//                } else {
//                    sender.sendMessage("§7/addshops <player> <amount>");
//                }
//            } else {
//                omp.msgUnknownCommand("/" + cmd.getName());
//            }
        } else if (cmd.getName().equalsIgnoreCase("addwarps")) {
            if (sender instanceof ConsoleCommandSender || sender instanceof Player && omp.isOpMode()) {
                if (a.length == 2) {
                    final int amount = Integer.parseInt(a[1]);
                    final Player player = Bukkit.getPlayer(a[0]);

                    if (player != null) {
                        omp.survival().setExtraWarps(omp.survival().getExtraWarps() + amount);

                        sender.sendMessage("§7You gave §2" + a[0] + " §3" + a[1] + " Warp§7!");

                        new BukkitRunnable() {
                            public void run() {
                                Title t = new Title("", "§3+" + amount + " Warp", 20, 40, 20);
                                t.send(player);
                            }
                        }.runTaskLater(survival, 40);
                    } else {
                        sender.sendMessage("§7Player §2" + a[0] + " §7isn't §aOnline§7!");
                    }
                } else {
                    sender.sendMessage("§7/addwarps <player> <amount>");
                }
            } else {
                omp.msgUnknownCommand("/" + cmd.getName());
            }
        }

        return false;
    }
}
