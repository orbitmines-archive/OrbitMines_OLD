package com.orbitmines.api.spigot.handlers.gadget.gadgets;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.firework.FireworkBuilder;
import com.orbitmines.api.spigot.handlers.gadget.GadgetHandler;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.playerdata.GadgetData;
import com.orbitmines.api.spigot.perks.Gadget;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class GadgetFireworkGun extends GadgetHandler {

    public GadgetFireworkGun() {
        super(Gadget.FIREWORK_GUN);
    }

    @Override
    public void onRun() {

    }

    @Override
    public void onLogout(OMPlayer omp) {

    }

    @Override
    public ItemStack getItem(OMPlayer omp) {
        Gadget gadget = getGadget();
        return new ItemBuilder(gadget.item().getMaterial(), 1, gadget.item().getDurability(), gadget.color().getChatColor() + "§l" + gadget.getName() + " " + gadget.color().getChatColor() + "(§6" + omp.gadgets().getFireworkPasses() + gadget.color().getChatColor() + ")").build();
    }

    @Override
    public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
        event.setCancelled(true);
        omp.updateInventory();

        GadgetData data = omp.gadgets();

        if (data.getFireworkPasses() != 0) {
            Player p = omp.getPlayer();

            FireworkBuilder fw = new FireworkBuilder(p.getLocation());
            fw.applySettings(data.getFireworkSettings());
            fw.setVelocity(p.getLocation().getDirection().multiply(0.2));

            data.removeFireworkPass();

            p.getInventory().setItem(api.gadgets().getGadgetSlot(), getItem(omp));
        } else {
            omp.sendMessage(new Message("§7Je hebt geen §6§lFirework Passes§7 meer!", "§7You don't have any §6§lFirework Passes§7."));
        }
    }
}
