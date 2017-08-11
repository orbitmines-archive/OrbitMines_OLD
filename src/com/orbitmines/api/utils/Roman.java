package com.orbitmines.api.utils;

import java.util.LinkedHashMap;
import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public class Roman {

    public static String parse(int number) {
        if (number <= 0)
            return "" + number;

        Map<Integer, String> map = new LinkedHashMap<>();
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");

        String romanEqui = "";

        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            int key = entry.getKey();
            if (number / key != 0) {
                for (int i = 0; i < (number / key); i++) {
                    romanEqui = romanEqui + map.get(key);
                }
                number = number % key;
            }
        }
        return romanEqui;
    }
}
