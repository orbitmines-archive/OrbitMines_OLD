package com.orbitmines.api.spigot.handlers.gadget.petride.pets;


import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.Cooldown;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.gadget.petride.PetHandler;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.playerdata.PetData;
import com.orbitmines.api.spigot.perks.Pet;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Slime;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class PetSlime extends PetHandler {

    private Cooldown COOLDOWN = new Cooldown(5000, "§6§lSuper Jump", "§6§lSuper Jump", Cooldown.Action.RIGHT_CLICK);

    public PetSlime() {
        super(Pet.SLIME);

        add(2, new ItemInstance() {
            @Override
            public void onInteract(PlayerInteractEvent event, OMPlayer omp, PetData petData) {
                if (omp.onCooldown(COOLDOWN))
                    return;

                petData.getPet().setVelocity(new Vector(0, 3, 0));

                omp.resetCooldown(COOLDOWN);
            }

            @Override
            public ItemStack getItem(OMPlayer omp, PetData petData) {
                return new ItemBuilder(Material.LEATHER_BOOTS, 1, 0, "§6§nSuper Jump").build();
            }
        });

        add(6, new ItemInstance() {
            @Override
            public void onInteract(PlayerInteractEvent event, OMPlayer omp, PetData petData) {
                event.setCancelled(true);
                omp.updateInventory();

                Slime pet = (Slime) petData.getPet();
                pet.setSize(pet.getSize() == 3 ? 0 : pet.getSize() +1);
                event.getItem().setAmount(getItem(omp, petData).getAmount());

                omp.getPlayer().playSound(omp.getPlayer().getLocation(), Sound.UI_BUTTON_CLICK, 5, 1);
            }

            @Override
            public ItemStack getItem(OMPlayer omp, PetData petData) {
                return new ItemBuilder(Material.SLIME_BALL, ((Slime) petData.getPet()).getSize(), 0, omp.getMessage(new Message("§a§nVerander Grootte", "§a§nChange Size"))).build();
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
