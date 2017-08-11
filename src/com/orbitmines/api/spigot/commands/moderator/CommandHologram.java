package com.orbitmines.api.spigot.commands.moderator;

import com.orbitmines.api.Message;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.commands.StaffCommand;
import com.orbitmines.api.spigot.handlers.NewsHologram;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import org.bukkit.entity.Player;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandHologram extends StaffCommand {

    private OrbitMinesApi api;
    private final String[] alias = {"/hologram"};

    public CommandHologram() {
        super(StaffRank.MODERATOR);
        api = OrbitMinesApi.getApi();
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void onDispatch(OMPlayer omp, String[] a) {
        if (!api.useNewsHolgrams()) {
            omp.msgUnknownCommand(a[0]);
            return;
        }
        Player p = omp.getPlayer();

        if (a.length == 1) {
            p.sendMessage("§7" + omp.getMessage(new Message("Gebruik", "Use")) + ":\n§7/hologram list\n§7/hologram <name> lines\n/hologram <name> relocate\n§7/hologram <name> delete\n§7/hologram <name> create\n§7/hologram <name> add <line>\n§7/hologram <name> remove <line number>\n§7/hologram <name> edit <line number> <line>");
            return;
        }

        String name = a[1];

        if (name.equalsIgnoreCase("list")) {
            p.sendMessage("§7§lHolograms:");
            for (String news : NewsHologram.getHolograms().keySet()) {
                p.sendMessage("§7 - " + news);
            }
            return;
        }

        if (name.length() > 20) {
            omp.sendMessage(new Message("§7Naam can niet niet meer dan 20 karakters hebben.", "§7Name cannot be longer than 20 characters."));
            return;
        }

        for (char ch : name.toCharArray()) {
            if (Character.isAlphabetic(ch))
                continue;

            omp.sendMessage(new Message("§7Je kan hier alleen letters gebruiken!", "§7You can only use letters here!"));
            return;
        }

        if (a.length < 3) {
            p.sendMessage("§7" + omp.getMessage(new Message("Gebruik", "Use")) + ":\n§7/hologram list\n§7/hologram <name> lines\n/hologram <name> relocate\n§7/hologram <name> delete\n§7/hologram <name> create\n§7/hologram <name> add <line>\n§7/hologram <name> remove <line number>\n§7/hologram <name> edit <line number> <line>");
            return;
        }

        String type = a[2].toLowerCase();

        switch (type) {

            case "lines": {
                NewsHologram news = NewsHologram.getHologram(name);

                if (news != null) {
                    p.sendMessage("§7§lLines: §7(" + name + ")");

                    int lineNumber = 0;
                    for (String line : news.getHologram().getLines()) {
                        lineNumber++;

                        p.sendMessage(" §7- §l" + lineNumber + ". §f" + line);
                    }
                } else {
                    omp.sendMessage(new Message("§7Die hologram bestaat niet!", "§7That hologram does not exist!"));
                }
                break;
            }
            case "relocate": {
                NewsHologram news = NewsHologram.getHologram(name);

                if (news != null) {
                    news.relocate(p.getPlayer().getLocation());

                    omp.sendMessage(new Message("§7Je hebt '" + name + "' verplaatst naar jouw positie!", "§7Successfully relocated '" + name + "' to your position!"));
                } else {
                    omp.sendMessage(new Message("§7Die hologram bestaat niet!", "§7That hologram does not exist!"));
                }
                break;
            }
            case "create": {
                if (NewsHologram.getHologram(name) == null) {
                    new NewsHologram(name, p.getPlayer().getLocation());
                    omp.sendMessage(new Message("§7Nieuwe hologram gemaakt! (" + name + ")", "§7Successfully created new news hologram! (" + name + ")"));
                } else {
                    omp.sendMessage(new Message("§7Die hologram bestaat al! Gebruik '/hologram " + name + " add|remove|edit' om te wijzigen.", "§7That news hologram already exists! Use '/hologram " + name + " add|remove|edit' in order to edit."));
                }
                break;
            }
            case "delete": {
                NewsHologram news = NewsHologram.getHologram(name);

                if (news != null) {
                    news.delete(true);
                } else {
                    omp.sendMessage(new Message("§7Die hologram bestaat niet!", "§7That hologram does not exist!"));
                }
                break;
            }
            case "add": {
                NewsHologram news = NewsHologram.getHologram(name);

                if (news == null) {
                    omp.sendMessage(new Message("§7Die hologram bestaat niet!", "§7That hologram does not exist!"));
                    break;
                }

                if (a.length >= 4) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int x = 3; x < a.length; x++) {
                        stringBuilder.append(a[x]).append(" ");
                    }
                    String line = stringBuilder.toString().substring(0, stringBuilder.length() -1);

                    news.addLine(line);

                    omp.sendMessage(new Message("§7Lijn toegevoegd: '" + line + "'.", "§7Successfully added line: '" + line + "'."));
                } else {
                    p.sendMessage("§7" + omp.getMessage(new Message("Gebruik", "Use")) + ": /news <name> add <line>");
                }
                break;
            }
            case "remove": {
                NewsHologram news = NewsHologram.getHologram(name);

                if (news == null) {
                    omp.sendMessage(new Message("§7Die hologram bestaat niet!", "§7That hologram does not exist!"));
                    break;
                }

                if (a.length == 4) {
                    int index;
                    try {
                        index = Integer.parseInt(a[3]);
                    } catch(NumberFormatException ex) {
                        omp.sendMessage(new Message("§7Dat is geen nummmer!", "§7That is not a valid line number."));
                        break;
                    }

                    if (news.getHologram().getLines().size() < index) {
                        omp.sendMessage(new Message("§7Die lijn nummer bestaat niet!", "§7That line number does not exist!"));
                        break;
                    }

                    news.removeLine(index);

                    omp.sendMessage(new Message("§7Lijn verwijderd: " + index + ".", "§7Successfully removed line " + index + "."));
                } else {
                    p.sendMessage("§7" + omp.getMessage(new Message("Gebruik", "Use")) + ": /news <name> remove <line number>");
                }
                break;
            }
            case "edit": {
                NewsHologram news = NewsHologram.getHologram(name);

                if (news == null) {
                    omp.sendMessage(new Message("§7Die hologram bestaat niet!", "§7That hologram does not exist!"));
                    break;
                }

                if (a.length >= 5) {
                    int index;
                    try {
                        index = Integer.parseInt(a[3]);
                    } catch(NumberFormatException ex) {
                        omp.sendMessage(new Message("§7Dat is geen nummmer!", "§7That is not a valid line number."));
                        break;
                    }

                    if (news.getHologram().getLines().size() < index) {
                        omp.sendMessage(new Message("§7Die lijn nummer bestaat niet!", "§7That line number does not exist!"));
                        break;
                    }

                    StringBuilder stringBuilder = new StringBuilder();
                    for (int x = 4; x < a.length; x++) {
                        stringBuilder.append(a[x]).append(" ");
                    }
                    String line = stringBuilder.toString().substring(0, stringBuilder.length() -1);

                    news.setLine(index, line);

                    omp.sendMessage(new Message("§7Lijn verandert: " + index + " naar: '" + line + "'.", "§7Successfully changed line " + index + " to: '" + line + "'."));
                } else {
                    p.sendMessage("§7" + omp.getMessage(new Message("Gebruik", "Use")) + ": /news <name> edit <line number> <line>");
                }
                break;
            }
            default: {
                p.sendMessage("§7" + omp.getMessage(new Message("Gebruik", "Use")) + ":\n§7/hologram list\n§7/hologram <name> lines\n/hologram <name> relocate\n§7/hologram <name> delete\n§7/hologram <name> create\n§7/hologram <name> add <line>\n§7/hologram <name> remove <line number>\n§7/hologram <name> edit <line number> <line>");
            }
        }
    }
}
