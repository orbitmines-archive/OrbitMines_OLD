package com.orbitmines.api.spigot.handlers.podium;

import com.orbitmines.api.spigot.Direction;
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

    private void setup() {
        locations[0] = direction.getAsNewLocation(location, 0, 2, -1);
        locations[1] = direction.getAsNewLocation(location, -1, 1, -1);
        locations[2] = direction.getAsNewLocation(location, 1, 0, -1);
    }

    private Skull getSkull(Location location) {
        Block b = location.getWorld().getBlockAt(getSkullLocation(location));

        if (b.getType() != Material.SKULL) {
            b.setType(Material.SKULL);
            b.setData((byte) 1);
        }

        return (Skull) b.getState();
    }

    private Location getSkullLocation(Location location) {
        return direction.getAsNewLocation(location, 0, 1, 1);
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
            skull.setOwningPlayer(null);
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
