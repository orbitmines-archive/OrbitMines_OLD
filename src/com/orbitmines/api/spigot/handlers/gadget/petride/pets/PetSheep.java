package com.orbitmines.api.spigot.handlers.gadget.petride.pets;


import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.gadget.petride.PetHandler;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.playerdata.PetData;
import com.orbitmines.api.spigot.perks.Pet;
import com.orbitmines.api.spigot.utils.RandomUtils;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Sheep;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class PetSheep extends PetHandler {

    private List<Sheep> entities;
    private DyeColor[] values;

    public PetSheep() {
        super(Pet.SHEEP);

        entities = new ArrayList<>();

        values = DyeColor.values();

        add(2, new ItemInstance() {
            @Override
            public void onInteract(PlayerInteractEvent event, OMPlayer omp, PetData petData) {
                event.setCancelled(true);
                omp.updateInventory();

                Sheep pet = (Sheep) petData.getPet();
                if (entities.contains(pet))
                    entities.remove(pet);
                else
                    entities.add(pet);

                ItemMeta meta = event.getItem().getItemMeta();
                meta.setDisplayName(getItem(omp, petData).getItemMeta().getDisplayName());
                event.getItem().setItemMeta(meta);

                omp.getPlayer().playSound(omp.getPlayer().getLocation(), Sound.UI_BUTTON_CLICK, 5, 1);
            }

            @Override
            public ItemStack getItem(OMPlayer omp, PetData petData) {
                return new ItemBuilder(Material.MONSTER_EGG, 1, 0, "§f§nSheep Disco§7 (" + omp.statusString(entities.contains(petData.getPet())) + "§7)").build();
            }
        });

        add(6, new ItemInstance() {
            @Override
            public void onInteract(PlayerInteractEvent event, OMPlayer omp, PetData petData) {
                event.setCancelled(true);
                omp.updateInventory();

                Sheep pet = (Sheep) petData.getPet();
                int colorIndex = pet.getColor().ordinal() + 1;
                if (values.length == colorIndex + 1)
                    colorIndex = 0;

                pet.setColor(values[colorIndex]);

                ItemStack item = getItem(omp, petData);
                ItemMeta meta = event.getItem().getItemMeta();
                meta.setDisplayName(item.getItemMeta().getDisplayName());
                event.getItem().setItemMeta(meta);

                event.getItem().setDurability(item.getDurability());

                omp.getPlayer().playSound(omp.getPlayer().getLocation(), Sound.UI_BUTTON_CLICK, 5, 1);
            }

            @Override
            public ItemStack getItem(OMPlayer omp, PetData petData) {
                DyeColor color = ((Sheep) petData.getPet()).getColor();
                return new ItemBuilder(Material.INK_SACK, 1, color.getDyeData(), omp.getMessage(new Message("§9§nVerander Kleur", "§9§nChange Color")) + "§7 (" + name(color) + "§7)").build();
            }
        });
    }

    @Override
    protected void onRun() {
        for (Sheep entity : entities) {
            entity.setColor(values[RandomUtils.RANDOM.nextInt(values.length)]);
        }
    }

    @Override
    public void onLogout(OMPlayer omp) {
        Sheep pet = (Sheep) omp.pets().getPet();

        if (entities.contains(pet))
            entities.remove(pet);
    }

    private String name(DyeColor dyecolor){

        switch(dyecolor){
            case BLACK:
                return "§0§lBlack";
            case BLUE:
                return "§1§lBlue";
            case BROWN:
                return "§f§lBrown";
            case CYAN:
                return "§3§lCyan";
            case GRAY:
                return "§8§lGray";
            case GREEN:
                return "§2§lGreen";
            case LIGHT_BLUE:
                return "§b§lLight Blue";
            case LIME:
                return "§a§lLight Green";
            case MAGENTA:
                return "§d§lMagenta";
            case ORANGE:
                return "§6§lOrange";
            case PINK:
                return "§d§lPink";
            case PURPLE:
                return "§5§lPurple";
            case RED:
                return "§c§lRed";
            case SILVER:
                return "§7§lLight Gray";
            case WHITE:
                return "§f§lWhite";
            case YELLOW:
                return "§e§lYellow";
            default:
                return null;
        }
    }
}
