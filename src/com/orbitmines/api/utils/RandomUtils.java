package com.orbitmines.api.utils;

import java.util.Random;

/*
* OrbitMines - @author Fadi Shawki - 7/20/2017
*/
public class RandomUtils {

    public static Random RANDOM = new Random();

    public static int random(int lower, int upper) {
        return RANDOM.nextInt((upper - lower) + 1) + lower;
    }

}
