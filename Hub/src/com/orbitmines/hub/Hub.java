package com.orbitmines.hub;

import com.orbitmines.api.Server;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.OrbitMinesServer;
import com.orbitmines.api.spigot.enablers.*;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.PreventionSet;
import com.orbitmines.api.spigot.handlers.WorldLoader;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

/*
* OrbitMines - @author Fadi Shawki - 2-8-2017
*/
public class Hub extends JavaPlugin implements OrbitMinesServer {

    private static Hub instance;
    private OrbitMinesApi api;

    private World lobby;

    private String[] voteMessages = { "§e§l1 OrbitMines Token" };

    @Override
    public void onEnable() {
        instance = this;
        api = OrbitMinesApi.getApi();

        api.setup(this);

        lobby = api.getWorldLoader().loadWorld("Hub", true);
        lobby.setGameRuleValue("doDaylightCycle", "false");
        lobby.setTime(20000);

        PreventionSet set = new PreventionSet();
        set.prevent(lobby,
                PreventionSet.Prevention.BLOCK_BREAK,
                PreventionSet.Prevention.BLOCK_INTERACTING,
                PreventionSet.Prevention.BLOCK_PLACE,
                PreventionSet.Prevention.FALL_DAMAGE,
                PreventionSet.Prevention.FOOD_CHANGE,
                PreventionSet.Prevention.MOB_SPAWN,
                PreventionSet.Prevention.MONSTER_EGG_USAGE,
                PreventionSet.Prevention.PVP,
                PreventionSet.Prevention.SWAP_HAND_ITEMS,
                PreventionSet.Prevention.WEATHER_CHANGE,
                PreventionSet.Prevention.ITEM_DROP,
                PreventionSet.Prevention.ITEM_PICKUP);
    }

    public static Hub getInstance() {
        return instance;
    }

    @Override
    public Server getServerType() {
        return Server.HUB;
    }

    @Override
    public String getDevelopedBy() {
        return StaffRank.OWNER.getPrefix() + "O_o_Fadi_o_O";
    }

    @Override
    public Location getSpawnLocation() {
        return null;
    }

    @Override
    public String[] getVoteMessages(OMPlayer omp) {
        return voteMessages;
    }

    @Override
    public WorldLoader registerWorldLoader() {
        return new WorldLoader(true);
    }

    @Override
    public void registerEvents() {

    }

    @Override
    public void registerCommands() {

    }

    @Override
    public void registerRunnables() {

    }

    @Override
    public void format(AsyncPlayerChatEvent event, OMPlayer omp) {
        event.setFormat(omp.getChatFormat());
    }

    @Override
    public boolean useActionBarCooldownTimer() {
        return true;
    }

    @Override
    public boolean useNpcMoving() {
        return false;
    }

    @Override
    public boolean usePodia() {
        return true;
    }

    @Override
    public DisguiseEnabler disguises() {
        return new DisguiseEnabler();
    }

    @Override
    public GadgetEnabler gadgets() {
        return new GadgetEnabler() {
            @Override
            public int getGadgetSlot() {
                return 5;
            }
        };
    }

    @Override
    public HatEnabler hats() {
        return new HatEnabler();
    }

    @Override
    public PetEnabler pets() {
        return new PetEnabler();
    }

    @Override
    public TrailEnabler trails() {
        return new TrailEnabler();
    }

    @Override
    public WardrobeEnabler wardrobe() {
        return new WardrobeEnabler();
    }
}
