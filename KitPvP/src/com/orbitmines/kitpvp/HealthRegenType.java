package com.orbitmines.kitpvp;

import com.orbitmines.api.Message;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public enum HealthRegenType {

    EXTREMELY_LOW(0.5D, new Message("Extreem Laag", "Extremely Low")),
    LOW(0.7D, new Message("Laag", "Low")),
    MEDIUM(0.825D, new Message("Middelmatig", "Medium")),
    ALMOST_NORMAL(0.90D, new Message("Bijna Normaal", "Almost Normal")),
    NORMAL(1.0D, new Message("Normaal", "Normal")),
    HIGH(1.1D, new Message("Hoog", "High")),
    HIGHEST(1.1825D, new Message("Hoogste", "Highest")),
    INSANE(1.50D, new Message("Insane", "Insane"));

    private final double multiplier;
    private final Message message;

    HealthRegenType(double multiplier, Message message) {
        this.multiplier = multiplier;
        this.message = message;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public Message getMessage() {
        return message;
    }
}
