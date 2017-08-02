package com.orbitmines.api.spigot.handlers.particle;

import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.perks.TrailType;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Fadi on 3-9-2016.
 */
public class Particle {

    protected OrbitMinesApi api;
    protected org.bukkit.Particle particle;
    protected Location location;
    protected Entity entity;
    protected int xSize;
    protected int ySize;
    protected int zSize;
    protected int special;
    protected int amount;

    protected double xAdded;
    protected double yAdded;
    protected double zAdded;

    public Particle(org.bukkit.Particle particle) {
        this(particle, null);
    }

    public Particle(org.bukkit.Particle particle, Location location) {
        this.api = OrbitMinesApi.getApi();
        this.particle = particle;
        this.location = location;
        this.xSize = 0;
        this.ySize = 0;
        this.zSize = 0;
        this.special = 0;
        this.amount = 1;

        this.xAdded = 0;
        this.yAdded = 0;
        this.zAdded = 0;
    }

    public org.bukkit.Particle getParticle() {
        return particle;
    }

    public void setParticle(org.bukkit.Particle particle) {
        this.particle = particle;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void fixOnEntity(Entity entity) {
        this.entity = entity;
    }

    public float getX() {
        return (float) (entity != null ? entity.getLocation().getX() : location.getX()) + (float) xAdded;
    }

    public float getY() {
        return (float) (entity != null ? entity.getLocation().getY() : location.getY()) + (float) yAdded;
    }

    public float getZ() {
        return (float) (entity != null ? entity.getLocation().getZ() : location.getZ()) + (float) zAdded;
    }

    public void setSize(int xSize, int ySize, int zSize) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
    }

    public int getXSize() {
        return xSize;
    }

    public int getYSize() {
        return ySize;
    }

    public int getZSize() {
        return zSize;
    }

    public int getSpecial() {
        return special;
    }

    public void setSpecial(int special) {
        this.special = special;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void send() {
        send(location.getWorld().getPlayers());
    }

    public void send(Player... players) {
        send(Arrays.asList(players));
    }

    public void send(Collection<? extends Player> players) {
        api.getNms().particle().send(players, particle, getX(), getY(), getZ(), xSize, ySize, zSize, special, amount);

        if (particle == org.bukkit.Particle.SNOW_SHOVEL)
            api.getNms().particle().send(players, org.bukkit.Particle.SNOWBALL, getX(), getY(), getZ(), xSize, ySize, zSize, special, amount);
    }

    public void apply(TrailType trailType) {

        switch (trailType) {

            case BASIC_TRAIL:
                setSize(1, 1, 1);
                break;
            case GROUND_TRAIL:
                setSize(1, 0, 1);
                break;
            case HEAD_TRAIL:
                yAdded = 2;
                break;
            case BODY_TRAIL:
                yAdded = 1;
                break;
            case BIG_TRAIL:
                setSize(2, 2, 2);
                break;
            case VERTICAL_TRAIL:
                setSize(0, 2, 0);
                break;
        }
    }
}
