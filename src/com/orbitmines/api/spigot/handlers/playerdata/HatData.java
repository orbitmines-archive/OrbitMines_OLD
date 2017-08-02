package com.orbitmines.api.spigot.handlers.playerdata;

import com.orbitmines.api.Data;
import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.perks.Hat;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 30-7-2017
*/
public class HatData extends PlayerData {

    private List<Hat> hats;
    private boolean unlockedHatsBlockTrail;
    private boolean hatsBlockTrail;

    /* Not saved in database */
    private int page;

    public HatData(OMPlayer omp) {
        super(Data.HATS, omp);

        /* Load Defaults */
        hats = new ArrayList<>();
        unlockedHatsBlockTrail = false;
        hatsBlockTrail = false;
    }

    @Override
    public void onLogin() {

    }

    @Override
    public void onLogout() {

    }

    @Override
    public String serialize() {
        return null;
    }

    @Override
    public void parse(String string) {

    }

    public List<Hat> getHats() {
        List<Hat> list = new ArrayList<>(this.hats);
        for (Hat value : Hat.values()) {
            if (value.obtainable().getVipRank() != null && omp.isEligible(value.obtainable().getVipRank()))
                list.add(value);
        }

        return list;
    }

    public void addHat(Hat hat) {
        this.hats.add(hat);
    }

    public boolean hasHat(){
        return getHats().size() > 0;
    }

    public boolean hasHat(Hat hat) {
        return getHats().contains(hat);
    }

    public boolean hasUnlockedHatsBlockTrail() {
        return unlockedHatsBlockTrail;
    }

    public void setUnlockedHatsBlockTrail(boolean unlockedHatsBlockTrail) {
        this.unlockedHatsBlockTrail = unlockedHatsBlockTrail;
    }

    public boolean hasHatsBlockTrail() {
        return hatsBlockTrail;
    }

    public void setHatsBlockTrail(boolean hatsBlockTrail) {
        this.hatsBlockTrail = hatsBlockTrail;

        omp.getPlayer().playSound(omp.getPlayer().getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 5, 1);
        if(hatsBlockTrail)
            omp.sendMessage(new Message("§7Je §7Hat Block Trail§7 staat nu §a§lAAN§7.", "§a§lENABLED §7your §7Hat Block Trail§7."));
        else
            omp.sendMessage(new Message("§7Je §7Hat Block Trail§7 staat nu §c§lUIT§7.", "§c§lDISABLED §7your §7Hat Block Trail§7."));
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int nextHatsPage() {
        return page++;
    }

    public int prevHatsPage() {
        return page--;
    }

    public void setHat(Hat hat) {
        if (hasHatEnabled())
            disableHat();

        String displayName = hat.getDisplayName();
        Player player = omp.getPlayer();
        player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 5, 1);
        player.getInventory().setHelmet(new ItemBuilder(hat.item().getMaterial(), 1, hat.item().getDurability(), displayName).build());
        getPlayer().sendMessage(new Message("§7Je " + displayName + "§7 staat nu §a§lAAN§7.", "§a§lENABLED §7your " + displayName + "§7."));
    }

    public boolean hasHatEnabled() {
        return omp.getPlayer().getInventory().getHelmet() != null;
    }

    public void disableHat() {
        String displayName = omp.getPlayer().getInventory().getHelmet().getItemMeta().getDisplayName();
        omp.sendMessage(new Message("§7Je " + displayName + "§7 staat nu §c§lUIT§7.", "§c§lDISABLED §7your " + displayName +"§7!"));
        omp.getPlayer().getInventory().setHelmet(null);
    }
}
