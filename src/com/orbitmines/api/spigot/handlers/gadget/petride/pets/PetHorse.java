package com.orbitmines.api.spigot.handlers.gadget.petride.pets;


import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.gadget.petride.PetHandler;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.playerdata.PetData;
import com.orbitmines.api.spigot.nms.entity.EntityNms;
import com.orbitmines.api.spigot.perks.Pet;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Horse;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class PetHorse extends PetHandler {

    public PetHorse() {
        super(Pet.HORSE);

        add(2, new ItemInstance() {
            @Override
            public void onInteract(PlayerInteractEvent event, OMPlayer omp, PetData petData) {
                event.setCancelled(true);
                omp.updateInventory();

                int speed = event.getItem().getAmount() +1;
                if (speed > 3)
                    speed = 1;

                api.getNms().entity().setAttribute(petData.getPet(), EntityNms.Attribute.MOVEMENT_SPEED, speed * 0.25);
                event.getItem().setAmount(getItem(omp, petData).getAmount());

                omp.getPlayer().playSound(omp.getPlayer().getLocation(), Sound.UI_BUTTON_CLICK, 5, 1);
            }

            @Override
            public ItemStack getItem(OMPlayer omp, PetData petData) {
                return new ItemBuilder(Material.FEATHER, (int) (api.getNms().entity().getAttribute(petData.getPet(), EntityNms.Attribute.MOVEMENT_SPEED) / 0.25), 0, omp.getMessage(new Message("§f§nVerander Snelheid", "§f§nChange Speed"))).build();
            }
        });

        Horse.Color[] values = Horse.Color.values();

        add(6, new ItemInstance() {
            @Override
            public void onInteract(PlayerInteractEvent event, OMPlayer omp, PetData petData) {
                event.setCancelled(true);
                omp.updateInventory();

                Horse pet = (Horse) petData.getPet();
                int colorIndex = pet.getColor().ordinal() + 1;
                if (values.length == colorIndex + 1)
                    colorIndex = 0;

                pet.setColor(values[colorIndex]);

                omp.getPlayer().playSound(omp.getPlayer().getLocation(), Sound.UI_BUTTON_CLICK, 5, 1);
            }

            @Override
            public ItemStack getItem(OMPlayer omp, PetData petData) {
                return new ItemBuilder(Material.LEATHER, 1, 0, omp.getMessage(new Message("§e§nVerander Kleur", "§e§nChange Color"))).build();
            }
        });
    }

    @Override
    protected void onRun() {

    }

    @Override
    public void onLogout(OMPlayer omp) {

    }
}
