package com.orbitmines.api.spigot;

import com.orbitmines.api.Data;
import com.orbitmines.api.Database;
import com.orbitmines.api.spigot.commands.CommandServers;
import com.orbitmines.api.spigot.commands.CommandTopVoters;
import com.orbitmines.api.spigot.commands.moderator.*;
import com.orbitmines.api.spigot.commands.owner.*;
import com.orbitmines.api.spigot.commands.perks.*;
import com.orbitmines.api.spigot.commands.vip.CommandAfk;
import com.orbitmines.api.spigot.commands.vip.CommandNick;
import com.orbitmines.api.spigot.enablers.*;
import com.orbitmines.api.spigot.events.*;
import com.orbitmines.api.spigot.events.npc.NpcChunkEvent;
import com.orbitmines.api.spigot.events.npc.NpcDamageEvent;
import com.orbitmines.api.spigot.events.npc.NpcInteractAtEntityEvent;
import com.orbitmines.api.spigot.events.npc.NpcInteractEntityEvent;
import com.orbitmines.api.spigot.handlers.ConfigHandler;
import com.orbitmines.api.spigot.handlers.currency.Currency;
import com.orbitmines.api.spigot.handlers.currency.CurrencyTokens;
import com.orbitmines.api.spigot.handlers.currency.CurrencyVipPoints;
import com.orbitmines.api.spigot.handlers.podium.PodiumPlayer;
import com.orbitmines.api.spigot.nms.Nms;
import com.orbitmines.api.spigot.runnable.DatabaseRunnable;
import com.orbitmines.api.spigot.runnable.runnables.NpcMovingRunnable;
import com.orbitmines.api.spigot.runnable.runnables.PodiumRunnable;
import com.orbitmines.api.spigot.runnable.runnables.ServerCountRunnable;
import com.orbitmines.api.spigot.runnable.runnables.TopVoterRunnable;
import com.orbitmines.api.spigot.runnable.runnables.player.ActionBarCooldownRunnable;
import com.orbitmines.api.spigot.runnable.runnables.player.ScoreboardRunnable;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 26-6-2017
*/
public class OrbitMinesApi extends JavaPlugin {

    private static OrbitMinesApi api;

    public static Currency VIP_POINTS = new CurrencyVipPoints();
    public static Currency TOKENS = new CurrencyTokens();

    private ConfigHandler configHandler;

    private Nms nms;
    private OrbitMinesServer server;

    private PodiumPlayer[] topVoters;

    private List<Chunk> chunks;

    private List<Data> dataToRead;

    private ChatColorEnabler chatColorEnabler;
    private DisguiseEnabler disguiseEnabler;
    private GadgetEnabler gadgetEnabler;
    private HatEnabler hatEnabler;
    private PetEnabler petEnabler;
    private TrailEnabler trailEnabler;
    private WardrobeEnabler wardrobeEnabler;

    @Override
    public void onEnable() {
        api = this;

        configHandler = new ConfigHandler(this);
        configHandler.setup("settings");

        nms = new Nms();

        topVoters = new PodiumPlayer[5];

        chunks = new ArrayList<>();

        dataToRead = new ArrayList<>();
        dataToRead.add(Data.GENERAL);

        /* Setup Bungee messaging */
        Messenger messenger = getServer().getMessenger();
        BungeeMessageEvent event = new BungeeMessageEvent();
        messenger.registerOutgoingPluginChannel(this, "BungeeCord");
        messenger.registerIncomingPluginChannel(this, "BungeeCord", event);
        messenger.registerOutgoingPluginChannel(this, "OrbitMinesApi");
        messenger.registerIncomingPluginChannel(this, "OrbitMinesApi", event);

        /* Setup Database */
        new Database(configHandler.get("settings").getString("host"), 3306, Database.NAME, configHandler.get("settings").getString("user"), configHandler.get("settings").getString("password"));
        new DatabaseRunnable().runTaskAsynchronously(this);
    }

    @Override
    public void onDisable() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.kickPlayer("§6§lOrbitMines§4§lNetwork\n    §7Restarting " + server().getServerType().getName() + "§7 Server...");
        }
    }

    public static OrbitMinesApi getApi() {
        return api;
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    public Nms getNms() {
        return nms;
    }

    public OrbitMinesServer server() {
        if (server == null)
            throw new NullPointerException("OrbitMinesServer is not setup correctly!");

        return server;
    }

    public void setup(OrbitMinesServer server) {
        this.server = server;

        registerEvents();
        server.registerEvents();

        Location spawnLocation = server.getSpawnLocation();
        if (spawnLocation != null)
            getServer().getPluginManager().registerEvents(new SpawnLocationEvent(spawnLocation), this);

        registerCommands();
        server.registerCommands();

        chatColorEnabler = new ChatColorEnabler();
        chatColorEnabler.onEnable();

        disguiseEnabler = server.disguises();
        if (disguiseEnabler != null)
            disguiseEnabler.onEnable();

        gadgetEnabler = server.gadgets();
        if (gadgetEnabler != null)
            gadgetEnabler.onEnable();

        hatEnabler = server.hats();
        if (hatEnabler != null)
            hatEnabler.onEnable();

        petEnabler = server.pets();
        if (petEnabler != null)
            petEnabler.onEnable();

        trailEnabler = server.trails();
        if (trailEnabler != null)
            trailEnabler.onEnable();

        wardrobeEnabler = server.wardrobe();
        if (wardrobeEnabler != null)
            wardrobeEnabler.onEnable();

        registerRunnables();
        server.registerRunnables();
    }

    public void enableData(Data data) {
        this.dataToRead.add(data);
    }

    public List<Data> getDataToRead() {
        return dataToRead;
    }

    public PodiumPlayer[] getTopVoters() {
        return topVoters;
    }

    public List<Chunk> getChunks() {
        return chunks;
    }

    private void registerEvents() {
        PluginManager pluginManager = getServer().getPluginManager();
        /* Afk */
        pluginManager.registerEvents(new AfkEvents(), this);
        /* OMInventory */
        pluginManager.registerEvents(new ClickEvent(), this);
        /* Commands */
        pluginManager.registerEvents(new CommandPreprocessEvent(), this);
        /* Login */
        pluginManager.registerEvents(new LoginEvent(), this);
        /* Quit */
        pluginManager.registerEvents(new QuitEvent(), this);
        /* Color Signs */
        pluginManager.registerEvents(new SignEvent(), this);
        /* Npcs */
        pluginManager.registerEvents(new NpcChunkEvent(), this);
        pluginManager.registerEvents(new NpcDamageEvent(), this);
        pluginManager.registerEvents(new NpcInteractAtEntityEvent(), this);
        pluginManager.registerEvents(new NpcInteractEntityEvent(), this);
    }

    private void registerCommands() {
        /* owner */
        new CommandAddEnchantment();
        new CommandClearEntities();
        new CommandDisguise();
        new CommandFeed();
        new CommandGameMode();
        new CommandGive();
        new CommandHeal();
        new CommandOpMode();
        new CommandSkull();
        new CommandUndisguise();

        /* moderator */
        new CommandFly();
        new CommandInvSee();
        new CommandLookUp();
        new CommandSilent();
        new CommandTeleport();

        /* vip */
        new CommandAfk();
        new CommandNick();

        /* perks */
        new CommandChatColors();
        new CommandDisguises();
        new CommandFireworks();
        new CommandGadgets();
        new CommandHats();
        new CommandPerks();
        new CommandPets();
        new CommandTrails();
        new CommandWardrobe();

        /* normal */
        new CommandServers();
        new CommandTopVoters();
    }

    private void registerRunnables() {
        if (server.useActionBarCooldownTimer())
            new ActionBarCooldownRunnable();

        if (server.useNpcMoving())
            new NpcMovingRunnable();

        if (server.usePodia())
            new PodiumRunnable();

        new ScoreboardRunnable();
        new ServerCountRunnable();
        new TopVoterRunnable();
    }

    public ChatColorEnabler chatcolors() {
        return chatColorEnabler;
    }

    public boolean isDisguiseEnabled() {
        return disguiseEnabler != null;
    }

    public DisguiseEnabler disguises() {
        return disguiseEnabler;
    }

    public boolean isGadgetEnabled() {
        return gadgetEnabler != null;
    }

    public GadgetEnabler gadgets() {
        return gadgetEnabler;
    }

    public boolean isHatEnabled() {
        return hatEnabler != null;
    }

    public HatEnabler hats() {
        return hatEnabler;
    }

    public boolean isPetEnabled() {
        return petEnabler != null;
    }

    public PetEnabler pets() {
        return petEnabler;
    }

    public boolean isTrailEnabled() {
        return trailEnabler != null;
    }

    public TrailEnabler trails() {
        return trailEnabler;
    }

    public boolean isWardrobeEnabled() {
        return wardrobeEnabler != null;
    }

    public WardrobeEnabler wardrobe() {
        return wardrobeEnabler;
    }
}
