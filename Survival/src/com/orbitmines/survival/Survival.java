package com.orbitmines.survival;

import com.orbitmines.api.Data;
import com.orbitmines.api.Server;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.OrbitMinesServer;
import com.orbitmines.api.spigot.commands.Command;
import com.orbitmines.api.spigot.enablers.*;
import com.orbitmines.api.spigot.handlers.ConfigHandler;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.PreventionSet;
import com.orbitmines.api.spigot.handlers.currency.Currency;
import com.orbitmines.api.spigot.handlers.inventory.ServerSelectorInventory;
import com.orbitmines.api.spigot.handlers.npc.FloatingItem;
import com.orbitmines.api.spigot.handlers.npc.NpcArmorStand;
import com.orbitmines.api.spigot.handlers.playerdata.PlayerData;
import com.orbitmines.api.spigot.handlers.worlds.voidgenerator.WorldCreatorVoid;
import com.orbitmines.api.spigot.utils.LocationUtils;
import com.orbitmines.survival.commands.*;
import com.orbitmines.survival.commands.vip.CommandBack;
import com.orbitmines.survival.commands.vip.CommandEnderchest;
import com.orbitmines.survival.commands.vip.CommandTpHere;
import com.orbitmines.survival.commands.vip.CommandWorkbench;
import com.orbitmines.survival.events.*;
import com.orbitmines.survival.handlers.SurvivalPlayer;
import com.orbitmines.survival.handlers.Warp;
import com.orbitmines.survival.handlers.WorldPortal;
import com.orbitmines.survival.handlers.currency.Money;
import com.orbitmines.survival.handlers.inventory.RegionInventory;
import com.orbitmines.survival.handlers.playerdata.SurvivalData;
import com.orbitmines.survival.handlers.region.Region;
import com.orbitmines.survival.handlers.region.RegionBuilder;
import com.orbitmines.survival.runnables.MobHeadRunnable;
import com.orbitmines.survival.runnables.TeleportingRunnable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.EulerAngle;

import java.util.*;

/*
* OrbitMines - @author Fadi Shawki - 8-8-2017
*/
public class Survival extends JavaPlugin implements OrbitMinesServer {

    private static Survival instance;
    private OrbitMinesApi api;

    public static Currency MONEY = new Money();

    private ConfigHandler configHandler;

    private World lobby;
    private World world;

    private Location spawnLocation;

    private String[] voteMessages = {"§8100 Claimblocks", "§250$"};

    private Map<VipRank, List<UUID>> mobHeads;

    private List<Player> noFall;

    @Override
    public void onEnable() {
        instance = this;
        api = OrbitMinesApi.getApi();

        configHandler = new ConfigHandler(this);
        configHandler.setup("playerdata", "chestshops", "warps", "regions");

        api.enableData(Data.SURVIVAL, new Data.Register() {
            @Override
            public PlayerData getData(OMPlayer omp) {
                return new SurvivalData(omp);
            }
        });

        Warp.setup();

        api.setup(this);

        lobby = api.getWorldLoader().fromZip(this, "SurvivalLobby", true, WorldCreatorVoid.class);
        spawnLocation = new Location(lobby, 0.5, 74, 0.5, 0, 0);

        world = Bukkit.getWorld("world");

//        api.setupHolograms();

        mobHeads = new HashMap<>();
        for (VipRank vipRank : VipRank.values()) {
            mobHeads.put(vipRank, new ArrayList<>());
        }

        noFall = new ArrayList<>();

        PreventionSet set = new PreventionSet();
        set.prevent(lobby,
                PreventionSet.Prevention.BLOCK_BREAK,
                PreventionSet.Prevention.BLOCK_INTERACTING,
                PreventionSet.Prevention.BLOCK_PLACE,
                PreventionSet.Prevention.FOOD_CHANGE,
                PreventionSet.Prevention.MOB_SPAWN,
                PreventionSet.Prevention.SWAP_HAND_ITEMS,
                PreventionSet.Prevention.WEATHER_CHANGE,
                PreventionSet.Prevention.ITEM_DROP,
                PreventionSet.Prevention.ITEM_PICKUP,
                PreventionSet.Prevention.PHYSICAL_INTERACTING,
                PreventionSet.Prevention.CLICK_PLAYER_INVENTORY,
                PreventionSet.Prevention.PLAYER_DAMAGE);
        set.prevent(world,
                PreventionSet.Prevention.PVP);

        setupRegions();
        setupWorldPortals();

        spawnNpcs();
    }

    public static Survival getInstance() {
        return instance;
    }

    public OrbitMinesApi getApi() {
        return api;
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    public World getLobby() {
        return lobby;
    }

    public World getWorld() {
        return world;
    }

    public Map<VipRank, List<UUID>> getMobHeads() {
        return mobHeads;
    }

    public List<Player> getNoFall() {
        return noFall;
    }

    private void setupRegions() {
        Region.TELEPORTABLE = configHandler.get("regions").getInt("teleportable");

        for (int i = 0; i < Region.REGION_COUNT; i++) {
            RegionBuilder builder = new RegionBuilder(world, i);
            builder.build();

            new Region(i, builder.getFixedSpawnLocation(), builder.getInventoryX(), builder.getInventoryY());
        }
    }

    private void setupWorldPortals() {
        new WorldPortal(lobby, LocationUtils.getBlocksBetween(new Location(lobby, 10, 72, -13), new Location(lobby, 14, 68, -13)));
    }

    private void spawnNpcs() {
        {
            FloatingItem item = new FloatingItem(new Location(lobby, 5, 72, 14, 180, 0), new FloatingItem.ClickAction() {
                @Override
                public void click(PlayerInteractAtEntityEvent event, OMPlayer player, FloatingItem item) {
                    new ServerSelectorInventory().open(player);
                }
            });
            new FloatingItem.ItemInstance(item, false) {
                @Override
                public ItemStack getItemStack() {
                    return new ItemStack(Material.ENDER_PEARL, 1);
                }

                @Override
                public String getDisplayName() {
                    return "§3§lServer Selector";
                }

                @Override
                public boolean isDisplayNameVisible() {
                    return true;
                }
            };
            item.spawn();
        }
        {
            FloatingItem item = new FloatingItem(new Location(lobby, 13, 68, -8, 0, 0), new FloatingItem.ClickAction() {
                @Override
                public void click(PlayerInteractAtEntityEvent event, OMPlayer player, FloatingItem item) {
                    new RegionInventory(0, 0).open(player);
                }
            });
            new FloatingItem.ItemInstance(item, false) {
                @Override
                public ItemStack getItemStack() {
                    return new ItemStack(Material.EYE_OF_ENDER, 1);
                }

                @Override
                public String getDisplayName() {
                    return "§a§lRegion Teleporter";
                }

                @Override
                public boolean isDisplayNameVisible() {
                    return true;
                }
            };
            item.spawn();
        }
//        {
//            NpcArmorStand npc = new NpcArmorStand(new Location(getLobby(), -5.5, 74, 7.5, -90, 0), new NpcArmorStand.InteractAction() {
//                @Override
//                public void click(Player player, NpcArmorStand npcArmorStand) {
//                    SurvivalPlayer omp = SurvivalPlayer.getSurvivalPlayer(player);
//                    omp.resetCooldown(SurvivalCooldowns.PVP_CONFIRM);
//                }
//            });
//            npc.setGravity(false);
//            npc.setVisible(false);
//            npc.setRightArmPose(EulerAngle.ZERO.setX(-0.5));
//            npc.setHelmet(ItemUtils.itemstack(Material.SKULL_ITEM, 1, 2));
//            npc.setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
//            npc.setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
//            npc.setBoots(new ItemStack(Material.DIAMOND_BOOTS));
//            npc.setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
//            npc.setCustomName("§2§lPvP Area");
//            npc.setCustomNameVisible(true);
//            npc.spawn();
//
//            getApi().registerNpcArmorStand(npc);
//        }
        {
            NpcArmorStand npc = new NpcArmorStand(new Location(lobby, -5, 74.5, 5.5, -45, 0), false);
            npc.setGravity(false);
            npc.setVisible(false);
            npc.setSmall(true);
            npc.setHeadPose(EulerAngle.ZERO.setX(-0.1));
            npc.setLeftLegPose(EulerAngle.ZERO.setX(0.5));
            npc.setRightArmPose(EulerAngle.ZERO.setX(-0.75));
            npc.setRightLegPose(EulerAngle.ZERO.setX(-0.5));
            npc.setHelmet(new ItemStack(Material.SKULL_ITEM, 1, (short) 2));
            npc.setChestPlate(new ItemStack(Material.GOLD_CHESTPLATE));
            npc.setLeggings(new ItemStack(Material.GOLD_LEGGINGS));
            npc.setBoots(new ItemStack(Material.GOLD_BOOTS));
            npc.setItemInHand(new ItemStack(Material.GOLD_SWORD));
            npc.spawn();
        }
        {
            NpcArmorStand npc = new NpcArmorStand(new Location(lobby, -4.2, 77, 11.2, -145, 25), false);
            npc.setGravity(false);
            npc.setVisible(false);
            npc.setSmall(true);
            npc.setHeadPose(EulerAngle.ZERO.setX(-0.1));
            npc.setLeftLegPose(EulerAngle.ZERO.setX(-0.5));
            npc.setRightArmPose(EulerAngle.ZERO.setX(-0.75));
            npc.setRightLegPose(EulerAngle.ZERO.setX(0.5));
            npc.setHelmet(new ItemStack(Material.SKULL_ITEM, 1, (short) 2));
            npc.setChestPlate(new ItemStack(Material.GOLD_CHESTPLATE));
            npc.setLeggings(new ItemStack(Material.GOLD_LEGGINGS));
            npc.setBoots(new ItemStack(Material.GOLD_BOOTS));
            npc.setItemInHand(new ItemStack(Material.GOLD_SWORD));
            npc.spawn();
        }
//        {
//            Npc npc = new Npc(Mob.BLAZE, new Location(getLobby(), 7, 71, 0.5, 0, 0), "§e§lOMT Shop", new Npc.InteractAction() {
//                @Override
//                public void click(PlayerInteractEntityEvent event, OMPlayer omp) {
//                    //TODO
//                }
//            });
//        }
//        {
//            Npc npc = new Npc(Mob.VILLAGER, new Location(getLobby(), 16.5, 70, 0.5, 90, 0), "§f§lTutorials", new Npc.InteractAction() {
//                @Override
//                public void click(PlayerInteractEntityEvent event, OMPlayer omp) {
//                    //TODO
//                }
//            });
//            npc.setVillagerProfession(Villager.Profession.LIBRARIAN);
//        }
//        {
//            Npc npc = new Npc(Mob.VILLAGER, new Location(getLobby(), -48.5, 79, -10.5, 0, 0), "§f§lTerug naar Spawn", new Npc.InteractAction() {
//                @Override
//                public void click(PlayerInteractEntityEvent event, OMPlayer omp) {
//                    omp.getPlayer().teleport(spawnLocation);
//                }
//            });
//            npc.setVillagerProfession(Villager.Profession.LIBRARIAN);
//        }
//        {
//            NpcMoving npc = new NpcMoving(Mob.SKELETON, new Location(getLobby(), -48.5, 79, -3.5, 0, 0), "§7§lClaim Tutorial", new NpcMoving.ArriveAction() {
//                @Override
//                public void arrive(NpcMoving npc, int index, int seconds) {
//                    World w = getLobby();
//
//                    if (index == 0) {
//                        if (seconds == 9) {
//                            npc.setItemInHand(null);
//
//                            w.getBlockAt(new Location(w, -53, 78, -7)).setType(Material.GRASS);
//                            w.getBlockAt(new Location(w, -53, 78, -8)).setType(Material.GRASS);
//                            w.getBlockAt(new Location(w, -52, 78, -8)).setType(Material.GRASS);
//                            w.getBlockAt(new Location(w, -53, 78, -11)).setType(Material.GRASS);
//                            w.getBlockAt(new Location(w, -53, 78, -14)).setType(Material.GRASS);
//                            w.getBlockAt(new Location(w, -53, 78, -15)).setType(Material.GRASS);
//                            w.getBlockAt(new Location(w, -52, 78, -15)).setType(Material.GRASS);
//                            w.getBlockAt(new Location(w, -49, 78, -15)).setType(Material.GRASS);
//                            w.getBlockAt(new Location(w, -46, 78, -15)).setType(Material.GRASS);
//                            w.getBlockAt(new Location(w, -45, 78, -15)).setType(Material.GRASS);
//                            w.getBlockAt(new Location(w, -45, 78, -14)).setType(Material.GRASS);
//                            w.getBlockAt(new Location(w, -45, 78, -14)).setType(Material.GRASS);
//                            w.getBlockAt(new Location(w, -45, 78, -11)).setType(Material.GRASS);
//                            w.getBlockAt(new Location(w, -45, 78, -8)).setType(Material.GRASS);
//                            w.getBlockAt(new Location(w, -45, 78, -7)).setType(Material.GRASS);
//                            w.getBlockAt(new Location(w, -46, 78, -7)).setType(Material.GRASS);
//                            w.getBlockAt(new Location(w, -46, 78, -7)).setType(Material.GRASS);
//                            w.getBlockAt(new Location(w, -49, 78, -7)).setType(Material.GRASS);
//                            w.getBlockAt(new Location(w, -52, 78, -7)).setType(Material.GRASS);
//                        } else if (seconds == 5) {
//                            final Hologram h = new Hologram(new Location(w, -48.5, 79.25, -3.5));
//                            h.addLine("§7Neem een Stone Hoe in je hand");
//                            h.create();
//
//                            new BukkitRunnable() {
//                                public void run() {
//                                    h.delete();
//                                }
//                            }.runTaskLater(getInstance(), 120);
//
//                            npc.setItemInHand(new ItemStack(Material.STONE_HOE));
//                        }
//                    } else if (index == 1) {
//                        if (seconds == 5) {
//                            final Hologram h = new Hologram(new Location(w, -53.5, 79.25, -5.5));
//                            h.addLine("§6Rechtermuisklik§7 op de hoek van je huis");
//                            h.create();
//
//                            final Hologram h2 = new Hologram(new Location(w, -52.5, 77, -6.5));
//                            h2.addLine("§bRechtermuisklik");
//                            h2.create();
//
//                            new BukkitRunnable() {
//                                public void run() {
//                                    h.delete();
//                                    h2.delete();
//                                }
//                            }.runTaskLater(getInstance(), 120);
//
//                            w.getBlockAt(new Location(w, -53, 78, -7)).setType(Material.SOIL);
//                        }
//                    } else if (index == 3) {
//                        if (seconds == 5) {
//                            final Hologram h = new Hologram(new Location(w, -43.5, 79.25, -15.5));
//                            h.addLine("§6Rechtermuisklik§7 op de tegenovergestelde hoek van je huis");
//                            h.create();
//
//                            final Hologram h2 = new Hologram(new Location(w, -44.5, 77, -14.5));
//                            h2.addLine("§bRechtermuisklik");
//                            h2.create();
//
//                            new BukkitRunnable() {
//                                public void run() {
//                                    h.delete();
//                                    h2.delete();
//                                }
//                            }.runTaskLater(getInstance(), 120);
//
//                            w.getBlockAt(new Location(w, -53, 78, -7)).setType(Material.GLOWSTONE);
//                            w.getBlockAt(new Location(w, -53, 78, -8)).setType(Material.GOLD_BLOCK);
//                            w.getBlockAt(new Location(w, -52, 78, -8)).setType(Material.GOLD_BLOCK);
//                            w.getBlockAt(new Location(w, -53, 78, -11)).setType(Material.GOLD_BLOCK);
//                            w.getBlockAt(new Location(w, -53, 78, -14)).setType(Material.GOLD_BLOCK);
//                            w.getBlockAt(new Location(w, -53, 78, -15)).setType(Material.GLOWSTONE);
//                            w.getBlockAt(new Location(w, -52, 78, -15)).setType(Material.GOLD_BLOCK);
//                            w.getBlockAt(new Location(w, -49, 78, -15)).setType(Material.GOLD_BLOCK);
//                            w.getBlockAt(new Location(w, -46, 78, -15)).setType(Material.GOLD_BLOCK);
//                            w.getBlockAt(new Location(w, -45, 78, -15)).setType(Material.GLOWSTONE);
//                            w.getBlockAt(new Location(w, -45, 78, -14)).setType(Material.GOLD_BLOCK);
//                            w.getBlockAt(new Location(w, -45, 78, -14)).setType(Material.GOLD_BLOCK);
//                            w.getBlockAt(new Location(w, -45, 78, -11)).setType(Material.GOLD_BLOCK);
//                            w.getBlockAt(new Location(w, -45, 78, -8)).setType(Material.GOLD_BLOCK);
//                            w.getBlockAt(new Location(w, -45, 78, -7)).setType(Material.GLOWSTONE);
//                            w.getBlockAt(new Location(w, -46, 78, -7)).setType(Material.GOLD_BLOCK);
//                            w.getBlockAt(new Location(w, -46, 78, -7)).setType(Material.GOLD_BLOCK);
//                            w.getBlockAt(new Location(w, -49, 78, -7)).setType(Material.GOLD_BLOCK);
//                            w.getBlockAt(new Location(w, -52, 78, -7)).setType(Material.GOLD_BLOCK);
//                        }
//                    }
//                }
//            });
//            npc.addMoveLocation(new Location(getLobby(), -48.5, 79, -3.5, 180, 0), 10);
//            npc.addMoveLocation(new Location(getLobby(), -53.5, 79, -5.5, 135, 45), 10);
//            npc.addMoveLocation(new Location(getLobby(), -53, 79, -13.5, 180, 0), 0);
//            npc.addMoveLocation(new Location(getLobby(), -46, 79, -15.5, -65, 45), 10);
//            npc.addMoveLocation(new Location(getLobby(), -44, 79, -7.5, 0, 0), 0);
//        }
//        {
//            Hologram h = new Hologram(new Location(getLobby(), -42.5, 79, -4.5));
//            h.addLine("§6§lChest Shop Tutorial");
//            h.create();
//        }
    }

    @Override
    public Server getServerType() {
        return Server.SURVIVAL;
    }

    @Override
    public int getMaxPlayers() {
        return 100;
    }

    @Override
    public OMPlayer newPlayerInstance(Player player) {
        return new SurvivalPlayer(player);
    }

    @Override
    public String getDevelopedBy() {
        return StaffRank.OWNER.getPrefix() + "O_o_Fadi_o_O";
    }

    @Override
    public Location getSpawnLocation() {
        return spawnLocation;
    }

    @Override
    public boolean teleportToSpawn(Player player) {
        return !configHandler.get("playerdata").contains("players." + player.getUniqueId().toString() + ".LastLocation");
    }

    @Override
    public String[] getVoteMessages(OMPlayer omp) {
        return voteMessages;
    }

    @Override
    public boolean cleanUpPlayerData() {
        return false;
    }

    @Override
    public List<String> getScoreboardTitles() {
        return Arrays.asList("§6§lOrbitMines§a§lSurvival", "§e§lO§6§lrbitMines§a§lSurvival", "§e§lOr§6§lbitMines§a§lSurvival", "§e§lOrb§6§litMines§a§lSurvival", "§e§lOrbi§6§ltMines§a§lSurvival", "§e§lOrbit§6§lMines§a§lSurvival", "§e§lOrbitM§6§lines§a§lSurvival", "§e§lOrbitMi§6§lnes§a§lSurvival", "§e§lOrbitMin§6§les§a§lSurvival", "§e§lOrbitMine§6§ls§a§lSurvival", "§e§lOrbitMines§a§lSurvival", "§e§lOrbitMines§2§lS§a§lurvival", "§e§lOrbitMines§2§lSu§a§lrvival", "§e§lOrbitMines§2§lSur§a§lvival", "§e§lOrbitMines§2§lSurv§a§lival", "§e§lOrbitMines§2§lSurvi§a§lval", "§e§lOrbitMines§2§lSurviv§a§lal", "§e§lOrbitMines§2§lSurviva§a§ll", "§e§lOrbitMines§2§lSurvival", "§6§lO§e§lrbitMines§2§lSurvival", "§6§lOr§e§lbitMines§2§lSurvival", "§6§lOrb§e§litMines§2§lSurvival", "§6§lOrbi§e§ltMines§2§lSurvival", "§6§lOrbit§e§lMines§2§lSurvival", "§6§lOrbitM§e§lines§2§lSurvival", "§6§lOrbitMi§e§lnes§2§lSurvival", "§6§lOrbitMin§e§les§2§lSurvival", "§6§lOrbitMine§e§ls§2§lSurvival", "§6§lOrbitMines§2§lSurvival", "§6§lOrbitMines§a§lS§2§lurvival", "§6§lOrbitMines§a§lSu§2§lrvival", "§6§lOrbitMines§a§lSur§2§lvival", "§6§lOrbitMines§a§lSurv§2§lival", "§6§lOrbitMines§a§lSurvi§2§lval", "§6§lOrbitMines§a§lSurviv§2§lal", "§6§lOrbitMines§a§lSurviva§2§ll", "§6§lOrbitMines§a§lSurvival");}

    @Override
    public void registerEvents() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new BreakEvent(), this);
        pluginManager.registerEvents(new DamageEvent(), this);
        pluginManager.registerEvents(new DeathEvent(), this);
        pluginManager.registerEvents(new InteractEvent(), this);
        pluginManager.registerEvents(new MoveEvent(), this);
        pluginManager.registerEvents(new PlaceEvent(), this);
        pluginManager.registerEvents(new SignEvent(), this);
        pluginManager.registerEvents(new SpawnEvent(), this);
    }

    @Override
    public void registerCommands() {
        Command.getCommand("/fly").unregister();
        Command.getCommand("/invsee").unregister();
        Command.getCommand("/teleport").unregister();
        /* Vip */
        new CommandBack();
        new CommandEnderchest();
        new CommandTpHere();
        new CommandWorkbench();

        /* Vip & Moderator */
        new CommandFly();
        new CommandInvsee();
        new CommandTeleport();

        /* normal */
        new CommandAccept();
        new CommandDelHome();
        new CommandEditWarp();
        new CommandHome();
        new CommandHomes();
        new CommandInvAccept();
        new CommandMyWarps();
        new CommandRegion();
        new CommandSetHome();
        new CommandSetWarp();
        new CommandSpawn();
        new CommandWarps();

        SurvivalConsoleCommandExecuter ccE = new SurvivalConsoleCommandExecuter();
        List<String> list = Arrays.asList("money", "addhomes", "addshops", "addwarps");

        for(String command : list){
            getCommand(command).setExecutor(ccE);
        }
    }

    @Override
    public void registerRunnables() {
        new MobHeadRunnable();
        new TeleportingRunnable();
    }

    @Override
    public void format(AsyncPlayerChatEvent event, OMPlayer omp) {
        event.setFormat(omp.getChatFormat());
    }

    @Override
    public boolean useActionBarCooldownTimer() {
        return false;
    }

    @Override
    public boolean useNpcMoving() {
        return false;
    }

    @Override
    public boolean usePodia() {
        return false;
    }

    @Override
    public DisguiseEnabler disguises() {
        return new DisguiseEnabler();
    }

    @Override
    public GadgetEnabler gadgets() {
        return null;
    }

    @Override
    public HatEnabler hats() {
        return null;
    }

    @Override
    public PetEnabler pets() {
        return null;
    }

    @Override
    public TrailEnabler trails() {
        return new TrailEnabler();
    }

    @Override
    public WardrobeEnabler wardrobe() {
        return null;
    }
}
