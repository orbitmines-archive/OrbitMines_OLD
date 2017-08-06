package com.orbitmines.api.spigot.handlers.playerdata;

import com.orbitmines.api.Data;
import com.orbitmines.api.Message;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.spigot.Color;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.Obtainable;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.itembuilders.LeatherArmorBuilder;
import com.orbitmines.api.spigot.perks.Wardrobe;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 30-7-2017
*/
public class WardrobeData extends PlayerData {

    private List<Wardrobe> wardrobe;
    private Obtainable wardrobeDiscoObtainable = new Obtainable(VipRank.DIAMOND);
    private boolean wardrobeDisco;

    public WardrobeData(OMPlayer omp) {
        super(Data.WARDROBE, omp);

        /* Load Defaults */
        wardrobe = new ArrayList<>();
        wardrobeDisco = false;
    }

    @Override
    public void onLogin() {

    }

    @Override
    public void onLogout() {

    }

    @Override
    public String serialize() {
        String wardrobeString = null;
        if (wardrobe.size() != 0){
            StringBuilder stringBuilder = new StringBuilder();
            for (Wardrobe wardrobe : this.wardrobe) {
                stringBuilder.append(wardrobe.toString());
                stringBuilder.append("\\=");
            }
            wardrobeString = stringBuilder.toString().substring(0, stringBuilder.length() -1);
        }

        return wardrobeString + "-" + wardrobeDisco;
    }

    @Override
    public void parse(String string) {
        String[] data = string.split("-");

        if (!data[0].equals("null")) {
            for (String wardrobe : data[0].split("=")) {
                this.wardrobe.add(Wardrobe.valueOf(wardrobe));
            }
        }

        wardrobeDisco = Boolean.parseBoolean(data[1]);
    }

    public List<Wardrobe> getWardrobe() {
        return this.wardrobe;
    }

    public void addWardrobe(Wardrobe wardrobe) {
        this.wardrobe.add(wardrobe);

        omp.updateStats();
    }

    public List<Wardrobe> getDiscoWardrobe() {
        List<Wardrobe> wardrobe = new ArrayList<>(this.wardrobe);
        if (wardrobe.contains(Wardrobe.ELYTRA))
            wardrobe.remove(Wardrobe.ELYTRA);
        return wardrobe;
    }

    public boolean hasWardrobe(Wardrobe wardrobe){
        return getWardrobe().contains(wardrobe);
    }

    public Obtainable getWardrobeDiscoObtainable() {
        return wardrobeDiscoObtainable;
    }

    public boolean isWardrobeDisco() {
        return wardrobeDisco;
    }

    public void setWardrobeDisco(boolean wardrobeDisco) {
        this.wardrobeDisco = wardrobeDisco;

        if (!wardrobeDisco)
            return;

        if (hasWardrobeEnabled() && !wardrobeDisco)
            disableWardrobe();

        omp.getPlayer().playSound(omp.getPlayer().getLocation(), Sound.BLOCK_ANVIL_LAND, 5, 1);

        Wardrobe wardrobe = Wardrobe.random(getDiscoWardrobe());
        omp.sendMessage(new Message("§7Je " + wardrobe.color().getChatColor() + "Disco Armor§7 staat nu §a§lAAN§7.", "§a§lENABLED §7your " + wardrobe.color().getChatColor() + "Disco Armor§7."));

        discoWardrobe(wardrobe);
    }

    public boolean hasWardrobeEnabled() {
        return omp.getPlayer().getInventory().getChestplate() != null;
    }

    public void disableWardrobe(){
        Player p = omp.getPlayer();

        String name = p.getInventory().getChestplate().getItemMeta().getDisplayName();
        omp.sendMessage(new Message("§7Je " + name + "§7 staat nu §c§lUIT§7.", "§c§lDISABLED §7your " + name +"§7!"));

        p.getInventory().setChestplate(null);
        p.getInventory().setLeggings(null);
        p.getInventory().setBoots(null);

        setWardrobeDisco(false);
    }

    public void discoWardrobe() {
        discoWardrobe(Wardrobe.random(getDiscoWardrobe()));
    }

    public void discoWardrobe(Wardrobe wardrobe){
        wardrobe(wardrobe, wardrobe.color().getChatColor() + "Disco Armor");
    }

    public void wardrobe(Wardrobe wardrobe) {
        Player p = omp.getPlayer();
        String displayName = wardrobe.getDisplayName();

        if (hasWardrobeEnabled())
            disableWardrobe();

        p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 5, 1);
        omp.sendMessage(new Message("§7Je " + displayName + "§7 staat nu §a§lAAN§7.", "§a§lENABLED §7your " + displayName + " Armor§7."));

        if (wardrobe == Wardrobe.ELYTRA) {
            p.getInventory().setChestplate(new ItemBuilder(Material.ELYTRA, 1, 0, displayName).build());
        } else if (wardrobe.obtainable().getVipRank() != null) {
            ItemBuilder builder = new ItemBuilder(wardrobe.item().getMaterial(), 1, 0, displayName);
            p.getInventory().setChestplate(builder.build());

            switch (wardrobe.obtainable().getVipRank()) {

                case IRON:
                    builder.setMaterial(Material.IRON_LEGGINGS);
                    p.getInventory().setLeggings(builder.build());
                    builder.setMaterial(Material.IRON_BOOTS);
                    p.getInventory().setBoots(builder.build());
                    break;
                case GOLD:
                    builder.setMaterial(Material.GOLD_LEGGINGS);
                    p.getInventory().setLeggings(builder.build());
                    builder.setMaterial(Material.GOLD_BOOTS);
                    p.getInventory().setBoots(builder.build());
                    break;
                case DIAMOND:
                    builder.setMaterial(Material.DIAMOND_LEGGINGS);
                    p.getInventory().setLeggings(builder.build());
                    builder.setMaterial(Material.DIAMOND_BOOTS);
                    p.getInventory().setBoots(builder.build());
                    break;
                case EMERALD:
                    builder.setMaterial(Material.CHAINMAIL_LEGGINGS);
                    p.getInventory().setLeggings(builder.build());
                    builder.setMaterial(Material.CHAINMAIL_BOOTS);
                    p.getInventory().setBoots(builder.build());
                    break;
            }
        } else {
            wardrobe(wardrobe, displayName);
        }
    }

    private void wardrobe(Wardrobe wardrobe, String displayName) {
        Player p = omp.getPlayer();
        Color color = wardrobe.color();

        LeatherArmorBuilder builder = new LeatherArmorBuilder(LeatherArmorBuilder.Type.CHESTPLATE, color.getBukkitColor(), 1, displayName);
        p.getInventory().setChestplate(builder.build());
        builder.setType(LeatherArmorBuilder.Type.LEGGINGS);
        p.getInventory().setLeggings(builder.build());
        builder.setType(LeatherArmorBuilder.Type.BOOTS);
        p.getInventory().setBoots(builder.build());
    }
}
