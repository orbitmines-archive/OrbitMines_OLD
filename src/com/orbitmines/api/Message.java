package com.orbitmines.api;

import com.orbitmines.api.spigot.handlers.OMPlayer;

/*
* OrbitMines - @author Fadi Shawki - 7/28/2017
*/
public class Message {

    private final String[] messages;

    public Message(String... messages) {
        this.messages = messages;
    }

    public String lang(Language language) {
        if (messages.length < language.ordinal() + 1)
            return messages[messages.length -1];
        return messages[language.ordinal()];
    }

    public void broadcast() {
        for (OMPlayer omp : OMPlayer.getPlayers()) {
            omp.sendMessage(this);
        }
    }
}
