package com.orbitmines.api.spigot.handlers.playerdata;

import com.orbitmines.api.Data;
import com.orbitmines.api.Message;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.spigot.handlers.Currency;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.Obtainable;
import com.orbitmines.api.spigot.handlers.particle.Particle;
import com.orbitmines.api.spigot.handlers.particle.ParticleAnimation;
import com.orbitmines.api.spigot.perks.Trail;
import com.orbitmines.api.spigot.perks.TrailType;
import org.bukkit.Location;
import org.bukkit.Sound;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 30-7-2017
*/
public class TrailData extends PlayerData {

    private List<Trail> trails;
    private List<TrailType> trailTypes;
    private Obtainable specialTrailObtainable = new Obtainable(Currency.VIP_POINTS, 750);
    private boolean unlockedSpecialTrail;
    private Obtainable particleAmountObtainable = new Obtainable(VipRank.GOLD);

    private Trail trail;
    private TrailType trailType;
    private int particleAmount;
    private boolean special;

    /* Not saved in database */
    private ParticleAnimation lastParticle;
    private boolean particlePlayNext;
    private Location lastLocation;

    public TrailData(OMPlayer omp) {
        super(Data.TRAILS, omp);

        /* Load Defaults */
        trails = new ArrayList<>();
        trailTypes = new ArrayList<>();
        unlockedSpecialTrail = false;

        special = false;
    }

    @Override
    public void onLogin() {

    }

    @Override
    public void onLogout() {

    }

    @Override
    public String serialize() {
        return null;
    }

    @Override
    public void parse(String string) {

    }

    public List<Trail> getTrails() {
        List<Trail> list = new ArrayList<>(this.trails);
        for (Trail value : Trail.values()) {
            if (value.obtainable().getVipRank() != null && omp.isEligible(value.obtainable().getVipRank()))
                list.add(value);
        }

        return list;
    }

    public void addTrail(Trail trail) {
        this.trails.add(trail);
    }

    public boolean hasTrail() {
        return getTrails().size() > 0;
    }

    public boolean hasTrail(Trail trail) {
        return getTrails().contains(trail);
    }

    public List<TrailType> getTrailTypes() {
        List<TrailType> list = new ArrayList<>(this.trailTypes);
        for (TrailType value : TrailType.values()) {
            if (value.obtainable().getVipRank() != null && omp.isEligible(value.obtainable().getVipRank()))
                list.add(value);
        }

        return list;
    }

    public void addTrailType(TrailType trailType) {
        getTrailTypes().add(trailType);
    }

    public boolean hasTrailType(TrailType trailType){
        return getTrailTypes().contains(trailType);
    }

    public Obtainable getSpecialTrailObtainable() {
        return specialTrailObtainable;
    }

    public Obtainable getParticleAmountObtainable() {
        return particleAmountObtainable;
    }

    public boolean hasUnlockedSpecialTrail() {
        return unlockedSpecialTrail;
    }

    public void setUnlockedSpecialTrail(boolean unlockedSpecialTrail) {
        this.unlockedSpecialTrail = unlockedSpecialTrail;
    }

    public Trail getTrail() {
        return trail;
    }

    public void setTrail(Trail trail) {
        this.trail = trail;

        if (trail == null)
            return;

        omp.getPlayer().playSound(omp.getPlayer().getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 5, 1);
        omp.sendMessage(new Message("§7Je " + trail.getDisplayName() + "§7 staat nu §a§lAAN§7.", "§a§lENABLED §7your " + trail.getDisplayName() + "§7."));
    }

    public boolean hasTrailEnabled() {
        return trail != null;
    }

    public void disableTrail() {
        omp.sendMessage(new Message("§7Je " + trail.getDisplayName() + "§7 staat nu §c§lUIT§7.", "§c§lDISABLED §7your " + trail.getDisplayName() + "§7!"));
        this.trail = null;
    }

    public TrailType getTrailType() {
        return trailType;
    }

    public void setTrailType(TrailType trailType) {
        this.trailType = trailType;
        this.lastParticle = null;
        this.particlePlayNext = true;

        omp.getPlayer().playSound(omp.getPlayer().getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 5, 1);
        omp.sendMessage(new Message("§7Je " + trailType.getDisplayName() + "§7 staat nu §a§lAAN§7.", "§a§lENABLED §7your " + trailType.getDisplayName() + "§7."));
    }

    public int getParticleAmount() {
        return particleAmount;
    }

    public void setParticleAmount(int particleAmount) {
        this.particleAmount = particleAmount;

        omp.getPlayer().playSound(omp.getPlayer().getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 5, 1);
    }

    public void addParticleAmount() {
        if (particleAmount >= 6)
            return;

        setParticleAmount(particleAmount +1);
    }

    public boolean isSpecial() {
        return special;
    }

    public void setSpecial(boolean special) {
        this.special = special;

        omp.getPlayer().playSound(omp.getPlayer().getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 5, 1);
        if (special)
            omp.sendMessage(new Message("§7Je §7§lSpecial Trail§7 staat nu §a§lAAN§7.", "§a§lENABLED §7your §7§lSpecial Trail§7."));
        else
            omp.sendMessage(new Message("§7Je §7§lSpecial Trail§7 staat nu §c§lUIT§7.", "§a§lDISABLED §7your §7§lSpecial Trail§7."));
    }

    public ParticleAnimation getLastParticle() {
        return lastParticle;
    }

    public void setLastParticle(ParticleAnimation lastParticle) {
        this.lastParticle = lastParticle;
    }

    public boolean canParticlePlayNext() {
        return particlePlayNext;
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(Location lastLocation) {
        this.lastLocation = lastLocation;
    }

    public void checkLastLocation() {
        if (lastLocation != null && lastLocation.getWorld().getName().equals(omp.getPlayer().getWorld().getName())) {
            particlePlayNext = lastLocation.distance(omp.getPlayer().getLocation()) <= 0.1;

            if (!particlePlayNext)
                lastLocation = omp.getPlayer().getLocation();
        } else {
            lastLocation = omp.getPlayer().getLocation();
            particlePlayNext = lastLocation == null;
        }
    }

    public void play() {
        play(trail, trailType, particleAmount, isSpecial() ? 1 : 0);
    }

    public void play(Trail trail, TrailType trailType, int particleAmount, int special) {
        if (!hasTrailEnabled())
            return;

        Particle particle = null;
        if (this.lastParticle != null && particlePlayNext)
            particle = this.lastParticle;

        if (!trailType.isAnimated()) {
            if (particle == null) {
                particle = new Particle(trail.getParticle(), omp.getPlayer().getLocation());
                applySettings(particle, particleAmount, special);
                particle.apply(trailType);
            }

            particle.send();
        } else {
            ParticleAnimation animation;

            if (particle == null) {
                animation = new ParticleAnimation(trail.getParticle(), omp.getPlayer().getLocation());
                applySettings(animation, particleAmount, special);
            } else {
                animation = (ParticleAnimation) particle;
            }
            animation.apply(trailType);
        }
    }

    private void applySettings(Particle particle, int particleAmount, int special) {
        particle.setParticle(particle.getParticle());
        particle.setAmount(particleAmount);
        particle.setSpecial(special);
    }
}
