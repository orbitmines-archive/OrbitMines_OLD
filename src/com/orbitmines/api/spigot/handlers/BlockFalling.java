package com.orbitmines.api.spigot.handlers;

import com.orbitmines.api.spigot.utils.RandomUtils;
import org.bukkit.Location;
import org.bukkit.entity.FallingBlock;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class BlockFalling {

    private FallingBlock fallingBlock;
    private Location location;
    private ItemSet item;
    private boolean drop;

    public BlockFalling(Location location, ItemSet item) {
        this.location = location;
        this.item = item;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ItemSet item() {
        return item;
    }

    public boolean hasDrop() {
        return drop;
    }

    public void setDrop(boolean drop) {
        this.drop = drop;
    }

    public void spawn() {
        fallingBlock = location.getWorld().spawnFallingBlock(location, new MaterialData(item.getMaterial(), (byte) item.getDurability()));
        fallingBlock.setDropItem(hasDrop());
    }

    public void randomVelocity() {
        randomVelocity(1.0D);
    }

    public void randomVelocity(double multiply) {
        Vector vector = RandomUtils.randomVelocity();
        if (vector == null)
            return;

        fallingBlock.setVelocity(vector.multiply(multiply));
    }
}
