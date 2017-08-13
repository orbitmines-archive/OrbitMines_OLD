package com.orbitmines.api.spigot.commands.owner;

import com.orbitmines.api.Message;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.spigot.commands.StaffCommand;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.SchematicGenerator;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandGenerator extends StaffCommand {

    private final String[] alias = {"/generator"};

    public CommandGenerator() {
        super(StaffRank.OWNER);
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void onDispatch(OMPlayer omp, String[] a) {
        Player p = omp.getPlayer();

        if (a.length >= 2) {
            Map<OMPlayer, SchematicGenerator> generators = SchematicGenerator.getGenerators();

            if (!generators.containsKey(omp) || a[1].equalsIgnoreCase("new")) {
                if (a[1].equalsIgnoreCase("new") && a.length >= 5) {
                    Material air;
                    try {
                        if (a[2].equals("null"))
                            air = null;
                        else
                            air = Material.valueOf(a[2]);
                    } catch(IllegalArgumentException ex) {
                        omp.sendMessage(new Message("§7Dat is geen Material!", "That is not a valid Material!"));
                        return;
                    }
                    omp.sendMessage(new Message("§7Nieuwe Schematic Generator gemaakt!", "§7Created new Schematic Generator!"));

                    String output = "";
                    for (int i = 4; i < a.length; i++) {
                        output += a[i];
                    }

                    generators.put(omp, new SchematicGenerator(omp, a[3], air, output, p.getLocation().getBlock().getLocation()));
                } else {
                    p.sendMessage("§7" + omp.getMessage(new Message("Gebruik", "Use")) + " §6" + a[0].toLowerCase() + " new <air material>/null <name> <output %x, %y, %z, %m, %d>");
                }
            } else {
                if (a[1].equalsIgnoreCase("wand")) {
                    omp.sendMessage(new Message("§7Je hebt de §eSchematic Generator Wand§7. gekregen", "§7You've been given the §eSchematic Generator Wand§7."));
                    p.getInventory().addItem(SchematicGenerator.WAND);
                } else if (a[1].equalsIgnoreCase("generate")) {
                    omp.sendMessage(new Message("§eSchematic§7 maken...", "§7Generating §eSchematic§7..."));
                    generators.get(omp).generate();
                    omp.sendMessage(new Message("§eSchematic§7 gemaakt!", "§eSchematic§7 generated!"));
                } else {
                    p.sendMessage("§7" + omp.getMessage(new Message("Gebruik", "Use")) + " §6" + a[0].toLowerCase() + " new|wand|generate");
                }
            }
        } else {
            p.sendMessage("§7" + omp.getMessage(new Message("Gebruik", "Use")) + " §6" + a[0].toLowerCase() + " new|wand|generate");
        }
    }
}
