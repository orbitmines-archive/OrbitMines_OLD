package com.orbitmines.api.spigot.handlers.inventory.perks;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.inventory.OMInventory;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.playerdata.PetData;
import com.orbitmines.api.spigot.nms.anvilgui.AnvilNms;
import com.orbitmines.api.spigot.perks.Pet;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/*
* OrbitMines - @author Fadi Shawki - 30-7-2017
*/
public class PetInventory extends PerkInventory {

    public PetInventory() {
        super(27, "§0§lPets", 22);
    }

    @Override
    protected void setPerkItems(OMPlayer omp) {
        PetData data = omp.pets();

        int slot = 0;
        for (Pet pet : Pet.values()) {
            add(slot, new ConfirmItemInstance(api.getNms().customItem().setEggId(toItemBuilder(pet, omp).build(), pet.getMob())) {

                @Override
                protected void onConfirm(InventoryClickEvent event, OMPlayer omp) {
                    data.addPet(pet);
                }

                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    if (!pet.hasAccess(omp)) {
                        if (pet.obtainable().isPurchasable() && pet.obtainable().canPurchase(omp)) {
                            confirmPurchase(omp, pet);
                        } else {
                            pet.obtainable().msgNoAccess(omp);
                        }
                    } else {
                        omp.getPlayer().closeInventory();

                        if (data.hasPetEnabled())
                            data.disablePet();

                        data.spawnPet(pet);
                    }
                }
            });

            slot++;
        }

        if (data.hasPetEnabled()) {

            add(21, new ItemInstance(new ItemBuilder(Material.NAME_TAG, 1, 0, omp.getMessage(new Message("§fVerander de naam van je §fPet", "§fRename §fPet"))).build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    Player p = omp.getPlayer();

                    p.closeInventory();
                    p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 5, 1);

                    api.getNms().anvilGui(p, (e) -> {
                        if (e.getSlot() != AnvilNms.AnvilSlot.OUTPUT) {
                            e.setWillClose(false);
                            e.setWillDestroy(false);
                            return;
                        }

                        String petName = e.getName();

                        e.setWillClose(true);
                        e.setWillDestroy(true);
                        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 5, 1);

                        Pet pet = data.getPetEnabled();

                        if (data.hasPetEnabled())
                            data.disablePet();

                        omp.sendMessage(new Message("§7Je hebt de naam van " + data.getPetName(pet) + "§7 veranderd in §f" + petName + "§7!", "§7Changed " + data.getPetName(pet) + "§7's name to §f" + petName + "§7!"));

                        data.setPetName(pet, petName);
                        data.spawnPet(pet);
                    }, new AnvilNms.AnvilCloseEvent() {

                        @Override
                        public void onClose() {
                            p.getInventory().setItem(13, null);
                        }
                    }).open();

                    p.getInventory().setItem(13, new ItemBuilder(Material.NAME_TAG, 1, 0, "§f§oClick the §6§oRight§f§o Egg to rename your Pet!").build());
                }
            });
            add(23, new ItemInstance(new ItemBuilder(Material.BARRIER, 1, 0, omp.getMessage(new Message("§4§nZet Pet UIT", "§4§nDisable Pet"))).build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    Player p = omp.getPlayer();

                    p.closeInventory();
                    p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 5, 1);
                    data.disablePet();
                }
            });
        }
    }

    @Override
    protected OMInventory returnInventory() {
        return new PetInventory();
    }

    @Override
    protected boolean isDisabled() {
        return !api.isPetEnabled();
    }
}
