package com.orbitmines.api.spigot.handlers;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.chat.ActionBar;
import com.orbitmines.api.spigot.utils.ItemUtils;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Fadi on 5-9-2016.
 */
public class Cooldown {

    public static Cooldown TELEPORTING = new Cooldown(3000, Action.OTHER);
    public static Cooldown NPC_INTERACT = new Cooldown(1000, Action.OTHER);

    private static Map<OMPlayer, Double> PREV_DOUBLE = new HashMap<>();

    private long cooldown;
    private String name;
    private String itemName;
    private Action action;

    public Cooldown(long cooldown, Action action) {
        this(cooldown, null, action);
    }

    public Cooldown(long cooldown, String name, Action action) {
        this(cooldown, name, null, action);
    }

    public Cooldown(long cooldown, String name, String itemName, Action action) {
        this.cooldown = cooldown;
        this.name = name;
        this.itemName = itemName;
        this.action = action;
    }

    /* Override this: Used to reduce time for certain players */
    public long getCooldown(OMPlayer omp) {
        return cooldown;
    }

    public long getCooldown() {
        return cooldown;
    }

    public String getName() {
        return name;
    }

    public String getItemName() {
        return itemName;
    }

    public Action getAction() {
        return action;
    }

    public boolean updateActionBar(OMPlayer omp) {
        ItemStack item = omp.getPlayer().getInventory().getItemInMainHand();

        if (name == null || ItemUtils.isNull(item))
            return false;

        String displayName = item.getItemMeta().getDisplayName();
        boolean equals = displayName.endsWith(getName().replace("§l", "§n")) || displayName.endsWith(getItemName());

        if (equals && omp.onCooldown(this)) {
            double cooldown = getCooldown(omp) / 1000;
            double left = cooldown - ((System.currentTimeMillis() - omp.getCooldown(this)) / 1000);

            String format = "ss,S";
            if (left < 10)
                format = "s,S";
            if (left > 60)
                format = "m: ss,S";
            if (left > 600)
                format = "mm: ss,S";

            String leftString = (new SimpleDateFormat(format).format(new Date(getCooldown(omp) - (System.currentTimeMillis() - omp.getCooldown(this))))).replace(":", "m");
            leftString = leftString.substring(0, leftString.indexOf(",") + 2) + "s";

            String bar = "";
            if (leftString.contains("m"))
                left = (Integer.parseInt(leftString.substring(0, leftString.indexOf("m"))) * 60) + Integer.parseInt(leftString.substring(leftString.indexOf("m") + 2, leftString.indexOf(","))) + (Double.parseDouble(leftString.substring(leftString.indexOf(",") + 1, leftString.indexOf(",") + 2)) / 10);
            else
                left = Integer.parseInt(leftString.substring(0, leftString.indexOf(","))) + (Double.parseDouble(leftString.substring(leftString.indexOf(",") + 1, leftString.indexOf(",") + 2)) / 10);

            double red = 40 - (((left / cooldown) * 100) / 2.5) + 2;

            /* Fix */
            if (PREV_DOUBLE.containsKey(omp) && (PREV_DOUBLE.get(omp) - red) >= 1.1)
                red = PREV_DOUBLE.get(omp);
            else
                PREV_DOUBLE.put(omp, red);

            bar += "§a|||||||||||||||||||||||||||||||||||||||| §8| §f" + leftString + " §8| " + getName();
            bar = bar.substring(0, (int) red) + "§c" + bar.substring((int) red);

            ActionBar actionbar = new ActionBar(omp.getPlayer(), bar, 20);
            actionbar.send();

            return true;
        } else {
            if (equals) {
                ActionBar actionbar = new ActionBar(omp.getPlayer(), omp.getMessage(action.getMessage()) + " §8| " + name, 20);
                actionbar.send();

                return true;
            }

            return false;
        }
    }

    public static Map<OMPlayer, Double> doubles() {
        return PREV_DOUBLE;
    }

    public enum Action {

        OTHER(null),
        LEFT_CLICK(new Message("§e§lLinkermuisknop", "§e§lLeft Click")),
        RIGHT_CLICK(new Message("§e§lRechtermuisknop", "§e§lRight Click"));

        private final Message message;

        Action(Message message) {
            this.message = message;
        }

        public Message getMessage() {
            return message;
        }
    }
}
