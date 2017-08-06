package com.orbitmines.api.spigot.enablers;

import com.orbitmines.api.Data;
import com.orbitmines.api.spigot.events.PlayerChatEvent;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.playerdata.ChatColorData;
import com.orbitmines.api.spigot.handlers.playerdata.PlayerData;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class ChatColorEnabler extends Enabler {

    @Override
    public void onEnable() {
        api.enableData(Data.CHATCOLORS, new Data.Register() {
            @Override
            public PlayerData getData(OMPlayer omp) {
                return new ChatColorData(omp);
            }
        });

        api.getServer().getPluginManager().registerEvents(new PlayerChatEvent(), api);
    }
}
