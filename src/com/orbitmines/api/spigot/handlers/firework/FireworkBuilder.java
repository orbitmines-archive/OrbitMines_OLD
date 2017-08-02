package com.orbitmines.api.spigot.handlers.firework;

import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.utils.ColorUtils;
import com.orbitmines.api.spigot.utils.RandomUtils;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class FireworkBuilder {

    private Firework firework;
    private FireworkMeta fireworkMeta;
    private FireworkEffect.Builder builder;

    public FireworkBuilder(Location location) {
        this.firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        this.fireworkMeta = firework.getFireworkMeta();
        this.builder = FireworkEffect.builder();
    }

    public void applySettings(FireworkSettings fireworkSettings) {
        if (fireworkSettings.getColor1() != null)
            getBuilder().withColor(fireworkSettings.getColor1().getBukkitColor());
        if (fireworkSettings.getColor2() != null)
            getBuilder().withColor(fireworkSettings.getColor2().getBukkitColor());
        if (fireworkSettings.getFade1() != null)
            getBuilder().withFade(fireworkSettings.getFade1().getBukkitColor());
        if (fireworkSettings.getFade2() != null)
            getBuilder().withFade(fireworkSettings.getFade2().getBukkitColor());

        getBuilder().with(fireworkSettings.getType());

        if (fireworkSettings.hasFlicker())
            getBuilder().withFlicker();
        if (fireworkSettings.hasTrail())
            getBuilder().withTrail();

        build();
    }

    public Firework getFirework() {
        return firework;
    }

    public FireworkMeta getFireworkMeta() {
        return fireworkMeta;
    }

    public FireworkEffect.Builder getBuilder() {
        return builder;
    }

    public void randomize() {
        builder.withColor(ColorUtils.random().getBukkitColor());
        builder.withColor(ColorUtils.random().getBukkitColor());
        builder.withFade(ColorUtils.random().getBukkitColor());
        builder.withFade(ColorUtils.random().getBukkitColor());

        if (RandomUtils.RANDOM.nextBoolean())
            builder.withFlicker();
        if (RandomUtils.RANDOM.nextBoolean())
            builder.withTrail();
    }

    public void build() {
        fireworkMeta.addEffects(builder.build());
        firework.setFireworkMeta(fireworkMeta);
    }

    public void setVelocity(Vector vector) {
        getFirework().setVelocity(vector);
    }

    public void explode() {
        OrbitMinesApi.getApi().getNms().firework().explode(firework);
    }
}
