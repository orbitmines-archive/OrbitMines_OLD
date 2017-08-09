package com.orbitmines.api;

/*
* OrbitMines - @author Fadi Shawki - 7/28/2017
*/
public enum Language {

    DUTCH,
    ENGLISH;

    public Language next() {
        Language[] values = Language.values();

        if (values.length == ordinal() +1)
            return values[0];
        return values[ordinal() + 1];
    }
}
