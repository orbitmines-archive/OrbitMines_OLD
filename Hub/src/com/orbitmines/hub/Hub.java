package com.orbitmines.hub;

import com.orbitmines.api.Server;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.OrbitMinesServer;
import com.orbitmines.api.spigot.enablers.*;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.PreventionSet;
import com.orbitmines.api.spigot.handlers.worlds.voidgenerator.WorldCreatorVoid;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

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

        lobby = api.getWorldLoader().fromZip("Hub", true, WorldCreatorVoid.class);
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

    public OrbitMinesApi getApi() {
        return api;
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
    public boolean cleanUpPlayerData() {
        return true;
    }

    @Override
    public List<String> getScoreboardTitles() {
        return Arrays.asList("§6§lOrbitMines§4§lNetwork", "§e§lO§6§lrbitMines§4§lNetwork", "§e§lOr§6§lbitMines§4§lNetwork", "§e§lOrb§6§litMines§4§lNetwork", "§e§lOrbi§6§ltMines§4§lNetwork", "§e§lOrbit§6§lMines§4§lNetwork", "§e§lOrbitM§6§lines§4§lNetwork", "§e§lOrbitMi§6§lnes§4§lNetwork", "§e§lOrbitMin§6§les§4§lNetwork", "§e§lOrbitMine§6§ls§4§lNetwork", "§e§lOrbitMines§4§lNetwork", "§e§lOrbitMines§c§lN§4§letwork", "§e§lOrbitMines§c§lNe§4§ltwork", "§e§lOrbitMines§c§lNet§4§lwork", "§e§lOrbitMines§c§lNetw§4§lork", "§e§lOrbitMines§c§lNetwo§4§lrk", "§e§lOrbitMines§c§lNetwor§4§lk", "§e§lOrbitMines§c§lNetwork", "§6§lO§e§lrbitMines§c§lNetwork", "§6§lOr§e§lbitMines§c§lNetwork", "§6§lOrb§e§litMines§c§lNetwork", "§6§lOrbi§e§ltMines§c§lNetwork", "§6§lOrbit§e§lMines§c§lNetwork", "§6§lOrbitM§e§lines§c§lNetwork", "§6§lOrbitMi§e§lnes§c§lNetwork", "§6§lOrbitMin§e§les§c§lNetwork", "§6§lOrbitMine§e§ls§c§lNetwork", "§6§lOrbitMines§c§lNetwork", "§6§lOrbitMines§4§lN§c§letwork", "§6§lOrbitMines§4§lNe§c§ltwork", "§6§lOrbitMines§4§lNet§c§lwork", "§6§lOrbitMines§4§lNetw§c§lork", "§6§lOrbitMines§4§lNetwo§c§lrk", "§6§lOrbitMines§4§lNetwor§c§lk", "§6§lOrbitMines§4§lNetwork");
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
