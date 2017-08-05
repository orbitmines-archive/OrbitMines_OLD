package com.orbitmines.api.spigot.handlers.playerdata;

import com.orbitmines.api.Data;
import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.Color;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.firework.FireworkSettings;
import com.orbitmines.api.spigot.perks.Gadget;
import org.bukkit.FireworkEffect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 30-7-2017
*/
public class GadgetData extends PlayerData {

    private List<Gadget> gadgets;

    private Gadget gadgetEnabled;

    private FireworkSettings fireworkSettings;
    private int fireworkPasses;

    private boolean stackerEnabled;

    public GadgetData(OMPlayer omp) {
        super(Data.GADGETS, omp);

        /* Load Defaults */
        stackerEnabled = true;
        gadgets = new ArrayList<>();
        fireworkSettings = new FireworkSettings(Color.AQUA, Color.BLUE, Color.SILVER, Color.BLUE, false, false, FireworkEffect.Type.BALL);
    }

    @Override
    public void onLogin() {

    }

    @Override
    public void onLogout() {
        if (gadgetEnabled != null)
            gadgetEnabled.getHandler().onLogout(omp);

        if (!api.isPetEnabled())
            return;

        PetData data = omp.pets();
        if (data.hasPetEnabled())
            data.getPetEnabled().getHandler().onLogout(omp);
    }

    @Override
    public String serialize() {
        return stackerEnabled + "";
    }

    @Override
    public void parse(String string) {
        String[] data = string.split("-");

        stackerEnabled = Boolean.parseBoolean(data[0]);
    }

    public List<Gadget> getGadgets() {
        List<Gadget> list = new ArrayList<>(this.gadgets);
        for (Gadget value : Gadget.values()) {
            if (value.obtainable().getVipRank() != null && omp.isEligible(value.obtainable().getVipRank()))
                list.add(value);
        }

        return list;
    }

    public void addGadget(Gadget gadget) {
        this.gadgets.add(gadget);
    }

    public boolean hasGadget(Gadget gadget){
        return getGadgets().contains(gadget);
    }

    public FireworkSettings getFireworkSettings() {
        return fireworkSettings;
    }

    public int getFireworkPasses() {
        return fireworkPasses;
    }

    public void addFireworkPasses(int fireworkPasses){
        this.fireworkPasses += fireworkPasses;
    }

    public void removeFireworkPass(){
        this.fireworkPasses--;
    }

    public boolean hasStackerEnabled() {
        return stackerEnabled;
    }

    public void setStackerEnabled(boolean stackerEnabled) {
        this.stackerEnabled = stackerEnabled;
    }

    public boolean hasGadgetEnabled() {
        return gadgetEnabled != null;
    }

    public Gadget getGadgetEnabled() {
        return gadgetEnabled;
    }

    public void enableGadget(Gadget gadget) {
        this.gadgetEnabled = gadget;

        Player p = omp.getPlayer();
        p.playSound(p.getLocation(), gadget == Gadget.FIREWORK_GUN ? Sound.BLOCK_ANVIL_BREAK : Sound.ENTITY_ARROW_HIT_PLAYER, 5, 1);
        p.getInventory().setItem(api.gadgets().getGadgetSlot(), gadget.getHandler().getItem(omp));

        omp.sendMessage(new Message("§7Je " + gadget.getDisplayName() + "§7 staat nu §a§lAAN§7.", "§a§lENABLED §7your " + gadget.getDisplayName() +"§7!"));
    }

    public void disableGadget() {
        omp.sendMessage(new Message("§7Je " + gadgetEnabled.getDisplayName() + "§7 staat nu §c§lUIT§7.", "§c§lDISABLED §7your " + gadgetEnabled.getDisplayName() +"§7!"));

        gadgetEnabled = null;

        omp.getPlayer().getInventory().setItem(api.gadgets().getGadgetSlot(), null);
    }
}
