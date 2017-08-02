package com.orbitmines.api.spigot.handlers.particle;

import com.orbitmines.api.spigot.perks.TrailType;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;

/*
* OrbitMines - @author Fadi Shawki - 30-7-2017
*/
public class ParticleAnimation extends Particle {

    private boolean positive;
    private int index;

    public ParticleAnimation(org.bukkit.Particle particle) {
        this(particle, null);
    }

    public ParticleAnimation(org.bukkit.Particle particle, Location location) {
        super(particle, location);

    }

    public float getOppositeX() {
        return (float) (entity != null ? entity.getLocation().getX() : location.getX()) - (float) xAdded;
    }

    public float getOppositeY() {
        return (float) (entity != null ? entity.getLocation().getY() : location.getY()) - (float) yAdded;
    }

    public float getOppositeZ() {
        return (float) (entity != null ? entity.getLocation().getZ() : location.getZ()) - (float) zAdded;
    }

    public boolean isPositive() {
        return positive;
    }

    public void setPositiv(boolean positive) {
        this.positive = positive;
    }

    public double getXAdded() {
        return xAdded;
    }

    public void setXAdded(double xAdded) {
        this.xAdded = xAdded;
    }

    public double getYAdded() {
        return yAdded;
    }

    public void setYAdded(double yAdded) {
        this.yAdded = yAdded;
    }

    public double getZAdded() {
        return zAdded;
    }

    public void setZAdded(double zAdded) {
        this.zAdded = zAdded;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void sendOpposite() {
        sendOpposite(location.getWorld().getPlayers());
    }

    public void sendOpposite(Player... players) {
        sendOpposite(Arrays.asList(players));
    }

    public void sendOpposite(Collection<? extends Player> players) {
        api.getNms().particle().send(players, particle, getOppositeX(), getOppositeY(), getOppositeZ(), xSize, ySize, zSize, special, amount);

        if (particle == org.bukkit.Particle.SNOW_SHOVEL)
            api.getNms().particle().send(players, org.bukkit.Particle.SNOWBALL, getOppositeX(), getOppositeY(), getOppositeZ(), xSize, ySize, zSize, special, amount);
    }

    @Override
    public void apply(TrailType trailType) {
        switch (trailType) {

            case CYLINDER_TRAIL: {
                if (index == 30) {
                    index = 0;

                    if (yAdded < 2)
                        yAdded += 0.25;
                    else
                        yAdded = 0;
                } else {
                    index++;
                }

                double rad = Math.toRadians(360 * (index / 30));
                xAdded = 1.1 * Math.cos(rad);
                zAdded = 1.1 * Math.sin(rad);

                send();
                sendOpposite();

                double tempYAdded = yAdded;

                yAdded = 2 - yAdded;
                send();
                sendOpposite();

                yAdded = tempYAdded;

                break;
            }
            case ORBIT_TRAIL: {
                if (yAdded == 1.65)
                    positive = false;
                else if (yAdded == 0)
                    positive = true;

                int multiplier = positive ? 1 : -1;

                yAdded += multiplier * 0.05;

                double rad = Math.toRadians(multiplier * (360 * (yAdded / 1.65)));

                xAdded = 1.1 * Math.cos(rad);
                zAdded = 1.1 * Math.sin(rad);

                send();
                sendOpposite();
                break;
            }
            case SNAKE_TRAIL: {
                if (index == 10)
                    positive = false;
                else if (index == 0)
                    positive = true;

                index += positive ? 1 : -1;

                double a = 1.75 / 2;
                /* b = 2π / period */
                double b = 1;
                double x = (index / 10) / (2 * Math.PI);
                double c = 0;
                double d = 0;

                /* y = |a| * sin( |b|(x + c) ) + d */
                yAdded = Math.abs(a) * Math.sin(Math.abs(b) * (x + c)) + d;

                send();
                break;
            }
            case FULL_CYLINDER: {
                for (int i = 0; i < 120; i++) {
                    apply(TrailType.CYLINDER_TRAIL);
                }
            }
        }
    }
}
