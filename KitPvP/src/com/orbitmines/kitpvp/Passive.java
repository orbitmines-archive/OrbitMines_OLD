package com.orbitmines.kitpvp;

import com.orbitmines.api.utils.Roman;

import java.util.HashMap;
import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public abstract class Passive {

    protected final Type type;
    protected final int level;

    public Passive(Type type, int level) {
        this.type = type;
        this.level = level;

        type.getLevels().put(level, this);
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

        LIGHTNING("§7Lightning"),
        THOR("§7Thor"),
        KNOCKBACK_CHANCE("§7Knockback Chance"),
        BLEED("§7Bleed"),
        FIRE_TRAIL("§7Fire Trail");

        private final String name;
        private final Map<Integer, Passive> levels;

        Type(String name) {
            this.name = name;
            this.levels = new HashMap<>();
        }

        public String getName() {
            return name;
        }

        public String getName(int level) {
            return name + " " + Roman.parse(level);
        }

        public Map<Integer, Passive> getLevels() {
            return levels;
        }

        public Passive getLevel(int level) {
            return levels.get(level -1);
        }
    }
}
