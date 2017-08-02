package com.orbitmines.api.spigot.handlers.firework;

import com.orbitmines.api.spigot.Color;
import com.orbitmines.api.spigot.utils.ColorUtils;
import org.bukkit.FireworkEffect;

/**
 * Created by Fadi on 3-9-2016.
 */
public class FireworkSettings {

    private Color color1;
    private Color color2;
    private Color fade1;
    private Color fade2;
    private boolean flicker;
    private boolean trail;
    private FireworkEffect.Type type;

    public FireworkSettings(Color color1, Color color2, Color fade1, Color fade2, boolean flicker, boolean trail, FireworkEffect.Type type) {
        this.color1 = color1;
        this.color2 = color2;
        this.fade1 = fade1;
        this.fade2 = fade2;
        this.flicker = flicker;
        this.trail = trail;
        this.type = type;
    }

    public Color getColor1() {
        return color1;
    }

    public void setColor1(Color color1) {
        this.color1 = color1;
    }

    public void nextColor1() {
        this.color1 = ColorUtils.next(this.color1);
    }

    public Color getColor2() {
        return color2;
    }

    public void setColor2(Color color2) {
        this.color2 = color2;
    }

    public void nextColor2() {
        this.color2 = ColorUtils.next(this.color2);
    }

    public Color getFade1() {
        return fade1;
    }

    public void setFade1(Color fade1) {
        this.fade1 = fade1;
    }

    public void nextFade1() {
        this.fade1 = ColorUtils.next(this.fade1);
    }

    public Color getFade2() {
        return fade2;
    }

    public void setFade2(Color fade2) {
        this.fade2 = fade2;
    }

    public void nextFade2() {
        this.fade2 = ColorUtils.next(this.fade2);
    }

    public boolean hasFlicker() {
        return flicker;
    }

    public void setFlicker(boolean flicker) {
        this.flicker = flicker;
    }

    public void nextFlicker() {
        this.flicker = !this.flicker;
    }

    public boolean hasTrail() {
        return trail;
    }

    public void setTrail(boolean trail) {
        this.trail = trail;
    }

    public void nextTrail() {
        this.trail = !this.trail;
    }

    public FireworkEffect.Type getType() {
        return type;
    }

    public void setType(FireworkEffect.Type type) {
        this.type = type;
    }

    public void nextType() {
        switch (this.type) {
            case BALL:
                this.type = FireworkEffect.Type.BALL_LARGE;
                break;
            case BALL_LARGE:
                this.type = FireworkEffect.Type.BURST;
                break;
            case BURST:
                this.type = FireworkEffect.Type.CREEPER;
                break;
            case CREEPER:
                this.type = FireworkEffect.Type.STAR;
                break;
            case STAR:
                this.type = FireworkEffect.Type.BALL;
                break;
            default:
                this.type = FireworkEffect.Type.BALL;
                break;
        }
    }
}
