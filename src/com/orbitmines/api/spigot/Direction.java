package com.orbitmines.api.spigot;

import com.orbitmines.api.spigot.utils.LocationUtils;
import org.bukkit.Location;

/*
* OrbitMines - @author Fadi Shawki - 28-7-2017
*/
public enum Direction {

    NORTH(1, 1),
    EAST(-1, 1),
    SOUTH(-1, -1),
    WEST(1, -1);

    private final int multiplyX;
    private final int multiplyZ;

    Direction(int multiplyX, int multiplyZ) {
        this.multiplyX = multiplyX;
        this.multiplyZ = multiplyZ;
    }

    /* Use addX,addZ for Direction=NORTH */
    public Location getAsNewLocation(Location location, double addX, double addY, double addZ) {
        return LocationUtils.asNewLocation(location, multiplyX * addX, addY, multiplyZ * addZ);
    }
}
