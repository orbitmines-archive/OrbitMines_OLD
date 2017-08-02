package com.orbitmines.api.spigot.handlers.playerdata;

import com.orbitmines.api.Data;
import com.orbitmines.api.spigot.handlers.OMPlayer;
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

    }

    @Override
    public String serialize() {
        return null;
    }

    @Override
    public void parse(String string) {

    }

    public Map<Pet, String> getPets() {
        return pets;
    }

    public void addPet(Pet pet) {
        this.pets.put(pet, omp.getPlayer().getName() + "'s " + pet.getMob().getName());
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

    public Pet getPetEnabled() {
        return petEnabled;
    }

    public boolean hasPetEnabled() {
        return petEnabled != null;
    }
}
