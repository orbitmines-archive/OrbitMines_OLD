package com.orbitmines.kitpvp.handlers;

import com.orbitmines.api.VipRank;
import com.orbitmines.api.spigot.Color;
import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.handlers.ItemSet;
import com.orbitmines.api.spigot.handlers.Obtainable;
import org.bukkit.Material;

public class ActiveBooster {

	private Type type;
	private String player;
	private int minutes;
	private int seconds;

	public ActiveBooster(Type type, String player, int minutes, int seconds) {
		this.type = type;
		this.player = player;
		this.minutes = minutes;
		this.seconds = seconds;
	}

	public Type getType() {
		return type;
	}

	public String getPlayer() {
		return player;
	}

	public int getMinutes() {
		return minutes;
	}

	public int getSeconds() {
		return seconds;
	}

	public void tickTimer() {
		if (seconds != 0) {
            seconds = seconds - 1;
        } else {
			minutes = minutes - 1;
			seconds = 59;
		}
	}

	public boolean canCancel() {
        return this.minutes == 0 && this.seconds == 0;
    }

    public enum Type {

	    IRON("Iron VIP Booster", Color.SILVER, 1.25, new ItemSet(Material.IRON_INGOT), VipRank.IRON, new Obtainable(OrbitMinesApi.VIP_POINTS, 250)),
	    GOLD("Gold VIP Booster", Color.ORANGE, 1.50, new ItemSet(Material.GOLD_INGOT), VipRank.GOLD, new Obtainable(OrbitMinesApi.VIP_POINTS, 250)),
	    DIAMOND("Diamond VIP Booster", Color.AQUA, 2.00, new ItemSet(Material.DIAMOND), VipRank.DIAMOND, new Obtainable(OrbitMinesApi.VIP_POINTS, 250)),
	    EMERALD("Emerald VIP Booster", Color.LIME, 2.50, new ItemSet(Material.EMERALD), VipRank.EMERALD, new Obtainable(OrbitMinesApi.VIP_POINTS, 250)),;

        private final String name;
        private final Color color;
        private final double multiplier;
        private final ItemSet item;
        private final VipRank vipRank;
        private final Obtainable obtainable;

        Type(String name, Color color, double multiplier, ItemSet item, VipRank vipRank, Obtainable obtainable) {
            this.name = name;
            this.color = color;
            this.multiplier = multiplier;
            this.item = item;
            this.vipRank = vipRank;
            this.obtainable = obtainable;
        }

        public String getName() {
            return name;
        }

        public Color color() {
            return color;
        }

        public double getMultiplier() {
            return multiplier;
        }

        public ItemSet item() {
            return item;
        }

        public VipRank getVipRank() {
            return vipRank;
        }

        public Obtainable obtainable() {
            return obtainable;
        }

        public String getDisplayName() {
            return color.getChatColor() + "§l" + name;
        }
    }
}