package com.orbitmines.api.spigot.handlers.podium;

import com.orbitmines.api.spigot.Direction;
import com.orbitmines.api.spigot.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fadi-Laptop on 23-9-2016.
 */
public abstract class Podium {

    private static List<Podium> podia = new ArrayList<>();

    private Location location;
    private Direction direction;
    private Location[] locations;

    public Podium(Location location, Direction direction) {
        podia.add(this);

        this.location = location;
        this.direction = direction;
        this.locations = new Location[3];

        setup();
    }

    public abstract String[] getLines(int index, PodiumPlayer stringInt);

    public abstract PodiumPlayer[] getStringInts();

    public Location getLocation() {
        return location;
    }

    public Direction getDirection() {
        return direction;
    }

    @Deprecated
    private void setup() {
        switch(getDirection()){
            case NORTH:
                locations[0] = LocationUtils.asNewLocation(location, 0, +2, -1);
                locations[1] = LocationUtils.asNewLocation(location, -1, +1, -1);
                locations[2] = LocationUtils.asNewLocation(location, +1, 0, -1);
                break;
            case EAST:
                locations[0] = LocationUtils.asNewLocation(location, +1, +2, 0);
                locations[1] = LocationUtils.asNewLocation(location, +1, +1, -1);
                locations[2] = LocationUtils.asNewLocation(location, +1, 0, +1);
                break;
            case SOUTH:
                locations[0] = LocationUtils.asNewLocation(location, 0, +2, +1);
                locations[1] = LocationUtils.asNewLocation(location, +1, +1, +1);
                locations[2] = LocationUtils.asNewLocation(location, -1, 0, +1);
                break;
            case WEST:
                locations[0] = LocationUtils.asNewLocation(location, -1, +2, 0);
                locations[1] = LocationUtils.asNewLocation(location, -1, +1, +1);
                locations[2] = LocationUtils.asNewLocation(location, -1, 0, -1);
                break;
        }
    }

    private Skull getSkull(Location location) {
        Block b = location.getWorld().getBlockAt(getSkullLocation(location));

        if (b.getType() != Material.SKULL) {
            b.setType(Material.SKULL);
            b.setData((byte) 1);
        }

        return (Skull) b.getState();
    }

    @Deprecated
    private Location getSkullLocation(Location location) {
        switch(getDirection()){
            case NORTH:
                return LocationUtils.asNewLocation(location, 0, 1, 1);
            case EAST:
                return LocationUtils.asNewLocation(location, -1, 1, 0);
            case SOUTH:
                return LocationUtils.asNewLocation(location, 0, 1, -1);
            case WEST:
                return LocationUtils.asNewLocation(location, 1, 1, 0);
            default:
                return null;
        }
    }

    public void update() {
        PodiumPlayer[] players = getStringInts();
        if (players == null)
            return;

        int index = 0;
        for (Location location : locations) {
            PodiumPlayer player = null;
            if (players.length > index)
                player = players[index];

            updateSkull(getSkull(location), player);
            updateSign(location, getLines(index, player));

            index++;
        }
    }

    private void updateSkull(Skull skull, PodiumPlayer player) {
        if (player == null || player.getUuid() == null) {
            skull.setSkullType(SkullType.SKELETON);
        } else {
            skull.setSkullType(SkullType.PLAYER);
            skull.setOwningPlayer(Bukkit.getOfflinePlayer(player.getUuid()));
        }
        skull.setRotation(BlockFace.valueOf(getDirection().toString()));
        skull.update();
    }

    private void updateSign(Location location, String[] lines) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendSignChange(location, lines);
        }
    }

    public static List<Podium> getPodia() {
        return podia;
    }
}
