package com.orbitmines.kitpvp;

import com.orbitmines.api.utils.Roman;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public abstract class Active {

    private final Type type;
    private final int level;

    public Active(Type type, int level) {
        this.type = type;
        this.level = level;
    }

    public Type getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return type.getName(level);
    }

    public enum Type {
        FLUFFY_BUNNY("§7Fluffy Bunny");

        private final String name;

        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public String getName(int level) {
            return name + " " + Roman.parse(level);
        }
    }
}
