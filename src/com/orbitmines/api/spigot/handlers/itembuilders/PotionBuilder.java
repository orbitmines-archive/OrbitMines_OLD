package com.orbitmines.api.spigot.handlers.itembuilders;

import org.bukkit.Color;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class PotionBuilder {

    private PotionEffectType type;
    private int duration;
    private int amplifier;
    private boolean ambient;
    private boolean particles;
    private Color color;

    public PotionBuilder(PotionEffectType type, int duration, int amplifier) {
        this(type, duration, amplifier, true);
    }

    public PotionBuilder(PotionEffectType type, int duration, int amplifier, boolean ambient) {
        this(type, duration, amplifier, ambient, true);
    }

    public PotionBuilder(PotionEffectType type, int duration, int amplifier, boolean ambient, boolean particles) {
        this(type, duration, amplifier, ambient, particles, null);
    }

    public PotionBuilder(PotionEffectType type, int duration, int amplifier, boolean ambient, boolean particles, Color color) {
        this.type = type;
        this.duration = duration;
        this.amplifier = amplifier;
        this.ambient = ambient;
        this.particles = particles;
        this.color = color;
    }

    public PotionEffectType getType() {
        return type;
    }

    public void setType(PotionEffectType type) {
        this.type = type;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getAmplifier() {
        return amplifier;
    }

    public void setAmplifier(int amplifier) {
        this.amplifier = amplifier;
    }

    public boolean isAmbient() {
        return ambient;
    }

    public void setAmbient(boolean ambient) {
        this.ambient = ambient;
    }

    public boolean isParticles() {
        return particles;
    }

    public void setParticles(boolean particles) {
        this.particles = particles;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public PotionEffect build() {
        return new PotionEffect(type, duration, amplifier, ambient, particles, color);
    }
}
