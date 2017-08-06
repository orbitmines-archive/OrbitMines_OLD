package com.orbitmines.api.spigot.handlers.playerdata;

import com.orbitmines.api.Data;
import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.perks.Gadget;
import com.orbitmines.api.spigot.perks.Pet;
import org.bukkit.entity.Entity;

import java.util.HashMap;
import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 30-7-2017
*/
public class PetData extends PlayerData {

    private Map<Pet, String> pets;

    /* Not saved in database */
    private Entity pet;
    private Pet petEnabled;

    public PetData(OMPlayer omp) {
        super(Data.PETS, omp);

        /* Load Defaults */
        pets = new HashMap<>();
    }

    @Override
    public void onLogin() {

    }

    @Override
    public void onLogout() {
        if (petEnabled != null)
            disablePet();
    }

    @Override
    public String serialize() {
        String petsString = null;
        if (pets.size() != 0){
            StringBuilder stringBuilder = new StringBuilder();
            for (Pet pet : pets.keySet()) {
                stringBuilder.append(pet.toString());
                stringBuilder.append("\\<");
                stringBuilder.append(pets.get(pet).replace("'", "`"));
                stringBuilder.append("\\=");
            }
            petsString = stringBuilder.toString().substring(0, stringBuilder.length() -1);
        }

        return petsString;
    }

    @Override
    public void parse(String string) {
        String[] data = string.split("-");

        if (!data[0].equals("null")) {
            for (String pet : data[0].split("=")) {
                String[] petData = pet.split("<");
                pets.put(Pet.valueOf(petData[0]), petData[1].replace("`", "'"));
            }
        }
    }

    public Map<Pet, String> getPets() {
        return pets;
    }

    public void addPet(Pet pet) {
        this.pets.put(pet, omp.getPlayer().getName() + "'s " + pet.getMob().getName());

        omp.updateStats();
    }

    public boolean hasPet(){
        return getPets().size() > 0;
    }

    public boolean hasPet(Pet pet){
        return getPets().containsKey(pet);
    }

    public void setPetName(Pet pet, String petName) {
        this.pets.put(pet, petName);
    }

    public String getPetName(Pet pet){
        return this.pets.get(pet);
    }

    public Entity getPet() {
        return pet;
    }

    public void setPet(Entity pet) {
        this.pet = pet;
    }

    public Pet getPetEnabled() {
        return petEnabled;
    }

    public void setPetEnabled(Pet petEnabled) {
        this.petEnabled = petEnabled;
    }

    public boolean hasPetEnabled() {
        return petEnabled != null;
    }

    public void disablePet() {
        omp.sendMessage(new Message("§7Je " + this.pets.get(petEnabled) + "§7 staat nu §c§lUIT§7.", "§c§lDISABLED §7 " + this.pets.get(petEnabled) +"§7!"));

        pet.remove();

        petEnabled.getHandler().onLogout(omp);

        Pet.getEntities().remove(pet);

        pet = null;
        petEnabled = null;
    }

    public void spawnPet(Pet pet) {
        this.pet = pet.spawn(omp);
        this.petEnabled = pet;
        Pet.getEntities().add(this.pet);

        omp.sendMessage(new Message("§7Je " + this.pets.get(petEnabled) + "§7 staat nu §a§lAAN§7.", "§a§lENABLED §7" + this.pets.get(petEnabled) +"§7!"));
    }
}
