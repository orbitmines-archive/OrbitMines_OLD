package com.orbitmines.survival.handlers;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.Color;
import com.orbitmines.api.spigot.handlers.Cooldown;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/*
* OrbitMines - @author Fadi Shawki - 12-8-2017
*/
public class Home implements Teleportable {

    private SurvivalPlayer omp;
    private String name;
    private Location location;

    public Home(SurvivalPlayer omp, String name, Location location) {
        this.omp = omp;
        this.name = name;
        this.location = location;
    }

    @Override
    public void teleportInstantly(SurvivalPlayer omp) {
        omp.setBackLocation(omp.getPlayer().getLocation());
        omp.getPlayer().teleport(this.location);
    }

    @Override
    public Color getColor() {
        return Color.ORANGE;
    }

    @Override
    public String getName() {
        return name;
    }

    public SurvivalPlayer getSurvivalPlayer() {
        return omp;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void teleport() {
        Player p = this.omp.getPlayer();
        SurvivalPlayer omp = SurvivalPlayer.getPlayer(p);

        omp.resetCooldown(Cooldown.TELEPORTING);
        omp.setTeleportingTo(this);

        omp.sendMessage(new Message("§7Teleporting naar §6" + this.name + "§7...", "§7Teleporting to §6" + this.name + "§7..."));
    }

    public void delete() {
        omp.removeHome(this);

        omp.sendMessage(new Message("§7Je home is verwijderd! (§6" + this.name + "§7)", "§7Removed your home! (§6" + this.name + "§7)"));
    }
}
