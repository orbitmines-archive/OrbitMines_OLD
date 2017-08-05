package com.orbitmines.api.spigot.enablers;

import com.orbitmines.api.Data;
import com.orbitmines.api.spigot.events.PlayerChatEvent;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class ChatColorEnabler extends Enabler {

    @Override
    public void onEnable() {
        api.enableData(Data.CHATCOLORS);

        api.getServer().getPluginManager().registerEvents(new PlayerChatEvent(), api);
    }
}
