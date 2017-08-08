package com.orbitmines.api.spigot;

import com.orbitmines.api.Server;
import com.orbitmines.api.spigot.enablers.*;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.WorldLoader;
import org.bukkit.Location;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public interface OrbitMinesServer {

    /* Server Type */
    Server getServerType();

    /* Displayed in OMPlayer#msgJoin */
    String getDevelopedBy();

    /* Return null if player should stay at their place */
    Location getSpawnLocation();

    /* Messages displayed when voting */
    String[] getVoteMessages(OMPlayer omp);

    /* WorldLoader for lobbies & others, return null if not used, use OrbitMines#getApi()#getWorldLoad() in order to use the WorldLoader */
    WorldLoader registerWorldLoader();

    void registerEvents();

    void registerCommands();

    void registerRunnables();

    void format(AsyncPlayerChatEvent event, OMPlayer omp);

    /* Disable certain features of the Api that use unnecessary data if not used */
    boolean useActionBarCooldownTimer();

    boolean useNpcMoving();

    boolean usePodia();

    /* Return null to disable */
    DisguiseEnabler disguises();

    GadgetEnabler gadgets();

    HatEnabler hats();

    PetEnabler pets();

    TrailEnabler trails();

    WardrobeEnabler wardrobe();

}
