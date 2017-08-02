package com.orbitmines.api.spigot.utils;

import com.orbitmines.api.spigot.Color;

import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 7/20/2017
*/
public class ColorUtils {

    public static Color random() {
        return random(Color.values());
    }

    public static Color random(Color[] colors) {
        return colors[RandomUtils.RANDOM.nextInt(colors.length)];
    }

    public static Color random(List<Color> colors) {
        return colors.get(RandomUtils.RANDOM.nextInt(colors.size()));
    }

    public static Color next(Color color) {
        Color[] values = Color.values();
        if (color.ordinal() == values.length - 1)
            return values[0];

        return values[color.ordinal() + 1];
    }
}
