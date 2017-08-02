package com.orbitmines.api.spigot.utils;

import org.bukkit.util.Vector;

import java.util.Random;

/*
* OrbitMines - @author Fadi Shawki - 7/20/2017
*/
public class RandomUtils {

    public static Random RANDOM = new Random();

    public static int random(int lower, int upper) {
        return RANDOM.nextInt((upper - lower) + 1) + lower;
    }

    public static Vector randomVelocity() {
        float x = (float) -0.03 + (float) (Math.random() * ((0.03 - -0.03) + 0.3));
        float y = (float) -0.03 + (float) (Math.random() * ((0.03 - -0.03) + 0.3));
        float z = (float) -0.03 + (float) (Math.random() * ((0.03 - -0.03) + 0.3));
        int iInt = RANDOM.nextInt(4);

        switch (iInt) {

            case 0:
                return new Vector(x -0.2, y, z -0.2);
            case 1:
                return new Vector(x, y, z);
            case 2:
                return new Vector(x -0.2, y, z);
            case 3:
                return new Vector(x, y, z -0.2);
            default:
                return new Vector(x, y, z);
        }
    }
}
