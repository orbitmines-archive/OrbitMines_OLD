package com.orbitmines.api.spigot;

import com.orbitmines.api.*;
import com.orbitmines.api.spigot.commands.Command;
import com.orbitmines.api.spigot.commands.CommandServers;
import com.orbitmines.api.spigot.commands.CommandTopVoters;
import com.orbitmines.api.spigot.commands.ConsoleCommandExecuter;
import com.orbitmines.api.spigot.commands.moderator.*;
import com.orbitmines.api.spigot.commands.owner.*;
import com.orbitmines.api.spigot.commands.perks.*;
import com.orbitmines.api.spigot.commands.vip.CommandAfk;
import com.orbitmines.api.spigot.commands.vip.CommandNick;
import com.orbitmines.api.spigot.enablers.*;
import com.orbitmines.api.spigot.events.*;
import com.orbitmines.api.spigot.events.npc.*;
import com.orbitmines.api.spigot.handlers.ConfigHandler;
import com.orbitmines.api.spigot.handlers.NewsHologram;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.SchematicGenerator;
import com.orbitmines.api.spigot.handlers.currency.Currency;
import com.orbitmines.api.spigot.handlers.currency.CurrencyTokens;
import com.orbitmines.api.spigot.handlers.currency.CurrencyVipPoints;
import com.orbitmines.api.spigot.handlers.playerdata.GeneralData;
import com.orbitmines.api.spigot.handlers.playerdata.PlayerData;
import com.orbitmines.api.spigot.handlers.podium.PodiumPlayer;
import com.orbitmines.api.spigot.handlers.worlds.WorldLoader;
import com.orbitmines.api.spigot.nms.Nms;
import com.orbitmines.api.spigot.runnable.DatabaseRunnable;
import com.orbitmines.api.spigot.runnable.runnables.*;
import com.orbitmines.api.spigot.runnable.runnables.player.ActionBarCooldownRunnable;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 26-6-2017
*/
public class OrbitMinesApi extends JavaPlugin {

    private static OrbitMinesApi api;

    public static Currency VIP_POINTS = new CurrencyVipPoints();
    public static Currency TOKENS = new CurrencyTokens();

    private ConfigHandler configHandler;
    private WorldLoader worldLoader;

    private Nms nms;
    private OrbitMinesServer server;

    private PodiumPlayer[] topVoters;

    private List<Chunk> chunks;

    private List<Data> dataToRead;

    private ScrollerList<String> scoreboardTitles;

    private ChatColorEnabler chatColorEnabler;
    private DisguiseEnabler disguiseEnabler;
    private GadgetEnabler gadgetEnabler;
    private HatEnabler hatEnabler;
    private PetEnabler petEnabler;
    private TrailEnabler trailEnabler;
    private WardrobeEnabler wardrobeEnabler;

    private boolean useNewsHolgrams;
    private boolean useInteractiveKits;

    @Override
    public void onEnable() {
        api = this;

        configHandler = new ConfigHandler(this);
        configHandler.setup("settings", SchematicGenerator.CONFIG);

        new Nms();

        topVoters = new PodiumPlayer[5];

        chunks = new ArrayList<>();

        dataToRead = new ArrayList<>();
        enableData(Data.GENERAL, new Data.Register() {
            @Override
            public PlayerData getData(OMPlayer omp) {
                return new GeneralData(omp);
            }
        });

        useNewsHolgrams = false;
        useInteractiveKits = false;

        /* Setup Bungee messaging */
        Messenger messenger = getServer().getMessenger();
        BungeeMessageEvent event = new BungeeMessageEvent();
        messenger.registerOutgoingPluginChannel(this, "BungeeCord");
        messenger.registerIncomingPluginChannel(this, "BungeeCord", event);
        messenger.registerOutgoingPluginChannel(this, PluginMessageType.CHANNEL);
        messenger.registerIncomingPluginChannel(this, PluginMessageType.CHANNEL, event);

        /* Setup Database */
        new Database(configHandler.get("settings").getString("host"), 3306, Database.NAME, configHandler.get("settings").getString("user"), configHandler.get("settings").getString("password"));
        new DatabaseRunnable().runTaskAsynchronously(this);
    }

    @Override
    public void onDisable() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.kickPlayer("§6§lOrbitMines§4§lNetwork\n    §7Restarting " + server().getServerType().getName() + "§7 Server...");
        }

        if (worldLoader != null)
            worldLoader.cleanUp();

        server().getServerType().setStatus(Server.Status.OFFLINE);
    }

    public static OrbitMinesApi getApi() {
        return api;
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    public WorldLoader getWorldLoader() {
        return worldLoader;
    }

    public Nms getNms() {
        return nms;
    }

    public void setNms(Nms nms) {
        this.nms = nms;
    }

    public OrbitMinesServer server() {
        if (server == null)
            throw new NullPointerException("OrbitMinesServer is not setup correctly!");

        return server;
    }

    public void setup(OrbitMinesServer server) {
        this.server = server;
        setMaxPlayers(server.getMaxPlayers());
        server.getServerType().setMaxPlayers(server.getMaxPlayers());
        server.getServerType().setStatus(Server.Status.ONLINE);

        worldLoader = new WorldLoader(server.cleanUpPlayerData());

        registerEvents();
        server.registerEvents();

        registerCommands();
        server.registerCommands();
        updateCommandsForBungee();

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

        scoreboardTitles = new ScrollerList<>(server.getScoreboardTitles());

        registerRunnables();
        server.registerRunnables();
    }

    public void enableData(Data data, Data.Register register) {
        this.dataToRead.add(data);
        data.register(register);
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

    public ScrollerList<String> getScoreboardTitles() {
        return scoreboardTitles;
    }

    private void registerEvents() {
        PluginManager pluginManager = getServer().getPluginManager();
        /* Afk */
        pluginManager.registerEvents(new AfkEvents(), this);
        /* OMInventory */
        pluginManager.registerEvents(new ClickEvent(), this);
        /* Commands */
        pluginManager.registerEvents(new CommandPreprocessEvent(), this);
        /* Hide Advancements */
        pluginManager.registerEvents(new LoadEvent(), this);
        /* Login */
        pluginManager.registerEvents(new LoginEvent(), this);
        /* Quit */
        pluginManager.registerEvents(new QuitEvent(), this);
        /* Npcs */
        pluginManager.registerEvents(new NpcChunkEvent(), this);
        pluginManager.registerEvents(new NpcDamageEvent(), this);
        pluginManager.registerEvents(new NpcInteractAtEntityEvent(), this);
        pluginManager.registerEvents(new NpcInteractEntityEvent(), this);
        pluginManager.registerEvents(new WorldSwitchEvent(), this);
        /* Spawn Location */
        pluginManager.registerEvents(new SpawnLocationEvent(), this);
        /* Schematic Generator */
        pluginManager.registerEvents(new SchematicGeneratorEvent(), this);
    }

    private void registerCommands() {
        /* owner */
        new CommandAddEnchantment();
        new CommandClearEntities();
        new CommandDisguise();
        new CommandFeed();
        new CommandGameMode();
        new CommandGenerator();
        new CommandGive();
        new CommandHeal();
        new CommandOpMode();
        new CommandSkull();
        new CommandUndisguise();

        /* moderator */
        new CommandFly();
        new CommandHologram();
        new CommandInvSee();
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

        ConsoleCommandExecuter ccE = new ConsoleCommandExecuter();
        List<String> list = Arrays.asList("setvip", "setstaff", "vippoints", "omt");

        for(String command : list){
            getCommand(command).setExecutor(ccE);
        }
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
        new ServerCheckRunnable();

        new NpcRunnable();
    }

    /* Updates a list of commands in the database which allows tab completion for commands */
    private void updateCommandsForBungee() {
       String dataType = BungeeDatabaseType.COMMANDS.toString() + "_" + server().getServerType().toString();

       StringBuilder stringBuilder = new StringBuilder();
       for (Command command : Command.getCommands()) {
           for (String cmd : command.getAlias()) {
               stringBuilder.append(cmd);
               stringBuilder.append("|");
           }
       }

       String commandList = stringBuilder.toString().substring(0, stringBuilder.length() -1);

       if (Database.get().contains(Database.Table.BUNGEE, Database.Column.TYPE, new Database.Where(Database.Column.TYPE, dataType)))
           Database.get().update(Database.Table.BUNGEE, new Database.Where(Database.Column.TYPE, dataType), new Database.Set(Database.Column.DATA, commandList));
       else
           Database.get().insert(Database.Table.BUNGEE, Database.get().values(dataType, commandList));
    }

    /* Setup News Holograms */
    public void setupHolograms() {
        List<Map<Database.Column, String>> entries = Database.get().getEntries(Database.Table.HOLOGRAMS, new Database.Where(Database.Column.SERVER, api.server().getServerType().toString()),
                Database.Column.HOLOGRAMNAME);

        for (Map<Database.Column, String> entry : entries) {
            new NewsHologram(entry.get(Database.Column.HOLOGRAMNAME).substring(api.server().getServerType().toString().length() + 1));
        }

        useNewsHolgrams = true;
    }

    /* Done automatically when using the KitInteractive class */
    public void setupInteractiveKits() {
        if (useInteractiveKits)
            return;

        useInteractiveKits = true;
        getServer().getPluginManager().registerEvents(new InteractiveKitEvent(), this);
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

    public boolean useNewsHolgrams() {
        return useNewsHolgrams;
    }

    public void setMaxPlayers(int maxPlayers) {
        try {
            Object server = getOBCClass("CraftServer").getMethod("getHandle").invoke(Bukkit.getServer());
            getDeclaredField(server.getClass().getSuperclass(), "maxPlayers").set(server, maxPlayers);
        } catch (Exception ex) {
        }
    }

    private Class<?> getOBCClass(String name) {
        String version = OrbitMinesApi.getApi().getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("org.bukkit.craftbukkit." + version + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Field getDeclaredField(Class<?> clazz, String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
