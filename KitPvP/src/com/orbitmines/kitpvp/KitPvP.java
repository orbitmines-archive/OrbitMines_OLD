package com.orbitmines.kitpvp;

import com.orbitmines.api.Data;
import com.orbitmines.api.Language;
import com.orbitmines.api.Server;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.spigot.Direction;
import com.orbitmines.api.spigot.Mob;
import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.OrbitMinesServer;
import com.orbitmines.api.spigot.enablers.*;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.PreventionSet;
import com.orbitmines.api.spigot.handlers.currency.Currency;
import com.orbitmines.api.spigot.handlers.inventory.ServerSelectorInventory;
import com.orbitmines.api.spigot.handlers.inventory.perks.CosmeticPerksInventory;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.kit.Kit;
import com.orbitmines.api.spigot.handlers.kit.KitInteractive;
import com.orbitmines.api.spigot.handlers.npc.FloatingItem;
import com.orbitmines.api.spigot.handlers.npc.Npc;
import com.orbitmines.api.spigot.handlers.npc.NpcArmorStand;
import com.orbitmines.api.spigot.handlers.playerdata.PetData;
import com.orbitmines.api.spigot.handlers.playerdata.PlayerData;
import com.orbitmines.api.spigot.handlers.playerdata.TrailData;
import com.orbitmines.api.spigot.handlers.podium.Podium;
import com.orbitmines.api.spigot.handlers.podium.PodiumPlayer;
import com.orbitmines.api.spigot.handlers.worlds.voidgenerator.WorldCreatorVoid;
import com.orbitmines.api.utils.RandomUtils;
import com.orbitmines.kitpvp.commands.CommandKit;
import com.orbitmines.kitpvp.commands.owner.CommandFreeKit;
import com.orbitmines.kitpvp.events.*;
import com.orbitmines.kitpvp.handlers.ActiveBooster;
import com.orbitmines.kitpvp.handlers.KitPvPMap;
import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import com.orbitmines.kitpvp.handlers.currency.CoinsCurrency;
import com.orbitmines.kitpvp.handlers.inventory.BoosterInventory;
import com.orbitmines.kitpvp.handlers.inventory.KitSelectorInventory;
import com.orbitmines.kitpvp.handlers.inventory.MasteryInventory;
import com.orbitmines.kitpvp.handlers.inventory.OMTShopInventory;
import com.orbitmines.kitpvp.handlers.kits.archer.Archer;
import com.orbitmines.kitpvp.handlers.kits.bunny.Bunny;
import com.orbitmines.kitpvp.handlers.kits.drunk.Drunk;
import com.orbitmines.kitpvp.handlers.kits.knight.Knight;
import com.orbitmines.kitpvp.handlers.kits.pyro.Pyro;
import com.orbitmines.kitpvp.handlers.kits.soldier.Soldier;
import com.orbitmines.kitpvp.handlers.kits.tank.Tank;
import com.orbitmines.kitpvp.handlers.kits.wizard.Wizard;
import com.orbitmines.kitpvp.handlers.playerdata.KitPvPData;
import com.orbitmines.kitpvp.handlers.playerdata.MasteriesData;
import com.orbitmines.kitpvp.runnables.*;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.EulerAngle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 2-8-2017
*/
public class KitPvP extends JavaPlugin implements OrbitMinesServer {

    public static Currency COINS = new CoinsCurrency();

    private static KitPvP instance;
    private OrbitMinesApi api;

    private boolean freeKitEnabled;

    private World lobby;
    @Deprecated
    private World arena;

    private Location spawnLocation;

    private KitPvPMap currentMap;
    private ActiveBooster booster;

    private String[] voteMessages = {"§6§l500 Coins"};

    private PodiumPlayer[] topKills;

    private FloatingItem masteryNpc;

    private FloatingItem regenNpc;
    private FloatingItem healthNpc;
    private FloatingItem knockbackNpc;
    private FloatingItem damageNpc;

    @Override
    public void onEnable() {
        instance = this;
        api = OrbitMinesApi.getApi();

        api.enableData(Data.KITPVP, new Data.Register() {
            @Override
            public PlayerData getData(OMPlayer omp) {
                return new KitPvPData(omp);
            }
        });
        api.enableData(Data.MASTERIES, new Data.Register() {
            @Override
            public PlayerData getData(OMPlayer omp) {
                return new MasteriesData(omp);
            }
        });

        api.setup(this);

        lobby = api.getWorldLoader().fromZip(this, "KitPvPLobby", true, WorldCreatorVoid.class);
        lobby.setGameRuleValue("doDaylightCycle", "false");
        lobby.setTime(20000);

        spawnLocation = new Location(lobby, -0.5, 74, -0.5, 90, 0);

        arena = api.getWorldLoader().fromZip(this, "KitPvPArenas", true, WorldCreatorVoid.class);

//        api.setupHolograms();
        this.topKills = new PodiumPlayer[3];

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
        set.prevent(arena,
                PreventionSet.Prevention.BLOCK_BREAK,
                PreventionSet.Prevention.BLOCK_INTERACTING,
                PreventionSet.Prevention.BLOCK_PLACE,
                PreventionSet.Prevention.FOOD_CHANGE,
                PreventionSet.Prevention.MOB_SPAWN,
                PreventionSet.Prevention.WEATHER_CHANGE,
                PreventionSet.Prevention.ITEM_DROP,
                PreventionSet.Prevention.ITEM_PICKUP,
                PreventionSet.Prevention.PHYSICAL_INTERACTING_EXCEPT_PLATES);

        registerKits();
        registerPodium();
        registerMaps();

        setRandomMap();

        spawnNpcs();
    }

    public static KitPvP getInstance() {
        return instance;
    }

    public OrbitMinesApi getApi() {
        return api;
    }

    public boolean isFreeKitEnabled() {
        return freeKitEnabled;
    }

    public void setFreeKitEnabled(boolean freeKitEnabled) {
        this.freeKitEnabled = freeKitEnabled;
    }

    public World getLobby() {
        return lobby;
    }

    public World getArena() {
        return arena;
    }

    public KitPvPMap getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(KitPvPMap currentMap) {
        this.currentMap = currentMap;
    }

    public ActiveBooster getBooster() {
        return booster;
    }

    public void setBooster(ActiveBooster booster) {
        this.booster = booster;
    }

    public boolean hasBooster(){
        return booster != null;
    }

    public PodiumPlayer[] getTopKills() {
        return topKills;
    }

    public FloatingItem getMasteryNpc() {
        return masteryNpc;
    }

    public FloatingItem getRegenNpc() {
        return regenNpc;
    }

    public FloatingItem getHealthNpc() {
        return healthNpc;
    }

    public FloatingItem getDamageNpc() {
        return damageNpc;
    }

    public FloatingItem getKnockbackNpc() {
        return knockbackNpc;
    }

    public KitInteractive getLobbyKit(KitPvPPlayer omp) {
        return (KitInteractive) Kit.getKit("Lobby_" + omp.getLanguage().toString());
    }

    public Kit getSpectatorKit(KitPvPPlayer omp) {
        return Kit.getKit("Spec_" + omp.getLanguage().toString());
    }

    public void setRandomMap() {
        List<KitPvPMap> maps = KitPvPMap.getMaps();
        currentMap = maps.get(RandomUtils.RANDOM.nextInt(maps.size()));
        currentMap.resetTimer();
    }

    public void nextMap() {
        List<KitPvPMap> maps = new ArrayList<>();
        int votes = 0;

        for (KitPvPMap map : KitPvPMap.getMaps()) {
            if (map.getVotes().size() > 0) {
                if (map.getVotes().size() > votes) {
                    votes = map.getVotes().size();
                    maps.clear();
                    maps.add(map);
                } else if (map.getVotes().size() == votes) {
                    maps.add(map);
                }
            }

            map.getVotes().clear();
        }

        if (votes == 0) {
            maps = new ArrayList<>(KitPvPMap.getMaps());
            if (currentMap != null)
                maps.remove(currentMap);
        }

        currentMap = maps.get(RandomUtils.RANDOM.nextInt(maps.size()));
        currentMap.resetTimer();
    }

    private void registerKits() {
        new Knight();
        new Archer();
        new Soldier();
        new Wizard();
        new Tank();
        new Drunk();
        new Pyro();
        new Bunny();


        KitInteractive.InteractAction serverSelector = new KitInteractive.InteractAction(new ItemBuilder(Material.ENDER_PEARL, 1, 0, "§3§nServer Selector").build()) {
            @Override
            public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
                event.setCancelled(true);
                omp.updateInventory();

                new ServerSelectorInventory().open(omp);
            }
        };
        KitInteractive.InteractAction boosters = new KitInteractive.InteractAction(new ItemBuilder(Material.GOLD_NUGGET, 1, 0, "§a§nBoosters").build()) {
            @Override
            public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
                event.setCancelled(true);
                omp.updateInventory();

                new BoosterInventory().open(omp);
            }
        };
        KitInteractive.InteractAction cosmeticPerks = new KitInteractive.InteractAction(new ItemBuilder(Material.ENDER_CHEST, 1, 0, "§9§nCosmetic Perks").build()) {
            @Override
            public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
                if (omp.canReceiveVelocity())
                    new CosmeticPerksInventory().open(omp);
            }
        };
        KitInteractive.InteractAction kitSelector = new KitInteractive.InteractAction(new ItemBuilder(Material.DIAMOND_CHESTPLATE, 1, 0, "§b§nKit Selector").build()) {
            @Override
            public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
                event.setCancelled(true);
                omp.updateInventory();

                new KitSelectorInventory().open(omp);
            }
        };
        {
            KitInteractive kit = new KitInteractive("Lobby_" + Language.DUTCH.toString());
            {
                ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
                BookMeta meta = (BookMeta) item.getItemMeta();
                meta.setDisplayName("§c§nBook of Enchantments§8 | §aBinnenkort beschikbaar");
                meta.addPage("1");
                meta.setPage(1,
                        "\n\n          §0§lBook\n"
                                + "         §0§lof \n"
                                + "   §0§lEnchantments");
                meta.setAuthor("§6§lOrbitMines§c§lKitPvP");
                item.setItemMeta(meta);

                kit.setItem(0, item);
            }
            
            kit.setItem(1, new KitInteractive.InteractAction(new ItemBuilder(Material.EXP_BOTTLE, 1, 0, "§d§nAchievements").build()) {
                @Override
                public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
                    event.setCancelled(true);
                    omp.updateInventory();

                    omp.getPlayer().sendMessage("§a§oBinnenkort beschikbaar...");
                }
            });

            kit.setItem(4, serverSelector);
            kit.setItem(5, boosters);
            kit.setItem(7, cosmeticPerks);
            kit.setItem(8, kitSelector);
            
            kit.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000, 0));
        }
        {
            KitInteractive kit = new KitInteractive("Lobby_" + Language.ENGLISH.toString());
            {
                ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
                BookMeta meta = (BookMeta) item.getItemMeta();
                meta.setDisplayName("§c§nBook of Enchantments§8 | §aComing Soon");
                meta.addPage("1");
                meta.setPage(1,
                        "\n\n          §0§lBook\n"
                                + "         §0§lof \n"
                                + "   §0§lEnchantments");
                meta.setAuthor("§6§lOrbitMines§c§lKitPvP");
                item.setItemMeta(meta);

                kit.setItem(0, item);
            }
            
            kit.setItem(1, new KitInteractive.InteractAction(new ItemBuilder(Material.EXP_BOTTLE, 1, 0, "§d§nAchievements").build()) {
                @Override
                public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
                    event.setCancelled(true);
                    omp.updateInventory();

                    omp.getPlayer().sendMessage("§a§oComing Soon...");
                }
            });

            kit.setItem(4, serverSelector);
            kit.setItem(5, boosters);
            kit.setItem(7, cosmeticPerks);
            kit.setItem(8, kitSelector);

            kit.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000, 0));
        }
        {
            Kit kit = new Kit("Spec_" + Language.DUTCH.toString());
            kit.setItem(3, new ItemBuilder(Material.ENDER_PEARL, 1, 0, "§3§nTerug naar de Lobby").build());
            kit.setItem(5, new ItemBuilder(Material.NAME_TAG, 1, 0, "§e§nTeleporter").build());
        }
        {
            Kit kit = new Kit("Spec_" + Language.ENGLISH.toString());
            kit.setItem(3, new ItemBuilder(Material.ENDER_PEARL, 1, 0, "§3§nBack to the Lobby").build());
            kit.setItem(5, new ItemBuilder(Material.NAME_TAG, 1, 0, "§e§nTeleporter").build());
        }


    }
    
    private void registerPodium() {
        new Podium(new Location(lobby, 6, 74, -1), Direction.WEST) {
            @Override
            public String[] getLines(int index, PodiumPlayer stringInt) {
                String[] lines = new String[4];
                lines[0] = "§lTop Kills (" + (index + 1) + ")";
                lines[1] = "";

                if (stringInt == null || stringInt.getUuid() == null) {
                    lines[2] = "";
                    lines[3] = "";
                } else {
                    lines[2] = stringInt.getPlayerName();

                    if (stringInt.getVotes() == 1)
                        lines[3] = "§o" + stringInt.getVotes() + " Kill";
                    else
                        lines[3] = "§o" + stringInt.getVotes() + " Kills";
                }

                return lines;
            }

            @Override
            public PodiumPlayer[] getStringInts() {
                return topKills;
            }
        };
    }
    
    @Deprecated
    private void registerMaps(){
        {
            KitPvPMap map = new KitPvPMap("Snow Town");
            map.setBuilders("§4§lOwner §4O_o_Fadi_o_O\n§b§lMod §bAlderius");
            map.setSpawns(Arrays.asList(new Location(getArena(), -63.5, 9, -1182.5, -45, 0), new Location(getArena(), -92.5, 14, -1079.5, -166, 0), new Location(getArena(), -134.5, 9, -1131.5, -113, 0), new Location(getArena(), -115, 12.5, -1187.5, -70, 0), new Location(getArena(), -90.5, 9, -1171.5, 143, 0), new Location(getArena(), -62.5, 9, -1138.5, -71, 0), new Location(getArena(), -108.5, 11, -1150.5, -139, 0), new Location(getArena(), -91.5, 10, -1205.5, -30, 0), new Location(getArena(), -144.5, 10, -1165.5, -75, 0), new Location(getArena(), -66.5, 10, -1098.5, -126, 0)));
            map.setSpectatorSpawn(new Location(getArena(), -93.5, 22, -1154.5, 145, 0));
            map.setMaxY(30);
            map.setVoteSign(new Location(getLobby(), -21, 76, 0));
        }
        {
            KitPvPMap map = new KitPvPMap("Mountain Village");
            map.setBuilders("§b§lMod §bAlderius\n§b§lMod §bsharewoods\n§4§lOwner §4O_o_Fadi_o_O");
            map.setSpawns(Arrays.asList(new Location(getArena(), -352.5, 5, -1366.5, -54, 0), new Location(getArena(), -317.5, 4, -1329.5, 46, 0), new Location(getArena(), -283.5, 4, -1296.5, 165, 0), new Location(getArena(), -303.5, 5, -1315.5, 39, 0), new Location(getArena(), -284.5, 4, -1348.5, 168, 0), new Location(getArena(), -303.5, 4, -1345.5, 135, 0), new Location(getArena(), -316.5, 9, -1364.5, 30, 0), new Location(getArena(), -337.5, 5, -1344.5, -126, 0), new Location(getArena(), -349.5, 4, -1327.5, -113, 0), new Location(getArena(), -323.5, 5, -1296.5, -161, 0)));
            map.setSpectatorSpawn(new Location(getArena(), -308.5, 16, -1326.5, 137, 0));
            map.setMaxY(26);
            map.setVoteSign(new Location(getLobby(), -21, 76, 1));
        }
        {
            KitPvPMap map = new KitPvPMap("Desert");
            map.setBuilders("§b§lMod §bAlderius");
            map.setSpawns(Arrays.asList(new Location(getArena(), -463.5, 29.5, -1139, 20, 0), new Location(getArena(), -422.5, 36, -1140.5, 105, 0), new Location(getArena(), -484, 33, -1060, 155, 0), new Location(getArena(), -523, 40, -1084, -90, 0), new Location(getArena(), -516.5, 37, -1123.5, 140, 0), new Location(getArena(), -450, 29, -1042.5, 156, 0), new Location(getArena(), -430.5, 34, -1080.5, 93, 0), new Location(getArena(), -451.5, 29, -1098.5, 20, 0), new Location(getArena(), -430.5, 38, -1035.5, 135, 0), new Location(getArena(), -510.5, 36, -1099.5, -123, 0)));
            map.setSpectatorSpawn(new Location(getArena(), -465, 43.5, -1085, 89, 17));
            map.setMaxY(47);
            map.setVoteSign(new Location(getLobby(), -21, 76, -1));
        }
        {
            KitPvPMap map = new KitPvPMap("Arena");
            map.setBuilders("§b§lMod §bAlderius");
            map.setSpawns(Arrays.asList(
                    new Location(getArena(), 8.5, 34, -1577.5, -90, 0),
                    new Location(getArena(), 39.5, 34, -1608.5, -30, 0),
                    new Location(getArena(), 88.5, 34, -1578.5, 120, 0),
                    new Location(getArena(), 65.5, 36, -1572.5, 62, 0),
                    new Location(getArena(), 28.5, 39, -1539.5, -130, 0),
                    new Location(getArena(), 84.5, 34, -1556.5, 60, 0),
                    new Location(getArena(), 37.5, 34, -1556.5, -140, 0),
                    new Location(getArena(), 47.5, 34, -1534.5, -166, 0),
                    new Location(getArena(), 23.5, 34, -1551.5, 0, 0),
                    new Location(getArena(), 55.5, 34, -1618.5, 40, 0)));
            map.setSpectatorSpawn(new Location(getArena(), 48.5, 49, -1576.5, 90, 90));
            map.setMaxY(53);
            map.setVoteSign(new Location(getLobby(), -21, 75, 0));
        }
       /* {
            KitPvPMap map = new KitPvPMap("Sky Fortress");
            map.setBuilders("§d§lBuilder §dcasidas\n§6§lGold §6blackdeadgames\n§9§lDiamond §9gapisabelle\n§6§lGold §6Jurre101\n§b§lMod §bAlderius");
            map.setSpawns(Arrays.asList(
                    new Location(getArena(), 8.5, 34, -1577.5, -90, 0),
                    new Location(getArena(), 39.5, 34, -1608.5, -30, 0),
                    new Location(getArena(), 88.5, 34, -1578.5, 120, 0),
                    new Location(getArena(), 65.5, 36, -1572.5, 62, 0),
                    new Location(getArena(), 28.5, 39, -1539.5, -130, 0),
                    new Location(getArena(), 84.5, 34, -1556.5, 60, 0),
                    new Location(getArena(), 37.5, 34, -1556.5, -140, 0),
                    new Location(getArena(), 47.5, 34, -1534.5, -166, 0),
                    new Location(getArena(), 23.5, 34, -1551.5, 0, 0),
                    new Location(getArena(), 55.5, 34, -1618.5, 40, 0)));
            map.setSpectatorSpawn(new Location(getArena(), 48.5, 49, -1576.5, 90, 90));
            map.setMaxY(53);
            map.setVoteSign(new Location(getLobby(), -21, 75, 1));
            maps.add(map);
        }*/
    }


    private void spawnNpcs(){
        {
            healthNpc = new FloatingItem(new Location(lobby, -12, 74, 8.5, 0, 0));
            healthNpc.spawn();
            healthNpc.getArmorStand().setCustomName("§c§lHealth");
            healthNpc.getArmorStand().setCustomNameVisible(true);

            regenNpc = new FloatingItem(new Location(lobby, -12, 74, 6.5, 0, 0));
            regenNpc.spawn();
            regenNpc.getArmorStand().setCustomName("§d§lRegen");
            regenNpc.getArmorStand().setCustomNameVisible(true);

            damageNpc = new FloatingItem(new Location(lobby, -9.5, 74, 11, 0, 0));
            damageNpc.spawn();
            damageNpc.getArmorStand().setCustomName("§4§lDamage");
            damageNpc.getArmorStand().setCustomNameVisible(true);

            knockbackNpc = new FloatingItem(new Location(lobby, -7.5, 74, 11, 0, 0));
            knockbackNpc.spawn();
            knockbackNpc.getArmorStand().setCustomName("§6§lKnockback");
            knockbackNpc.getArmorStand().setCustomNameVisible(true);
        }
        {
            FloatingItem item = new FloatingItem(new Location(lobby, -5.5, 74, -7.5, -45, 0), new FloatingItem.ClickAction() {
                @Override
                public void click(PlayerInteractAtEntityEvent event, OMPlayer omp, FloatingItem item) {
                    new ServerSelectorInventory().open(omp);
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
            Npc npc = new Npc(Mob.WITHER_SKELETON, new Location(lobby, 10.5, 74, -11.5, 45, 0), "§e§lSpectate", new Npc.InteractAction() {
                @Override
                public void click(PlayerInteractEntityEvent event, OMPlayer player) {
                    KitPvPPlayer omp = (KitPvPPlayer) player; 
                    PetData petData = omp.pets();
                    TrailData trailData = omp.trails();
                    
//                    if(petData != null)
//                        petData.disablePet();
                    if(trailData.hasTrailEnabled())
                        trailData.disableTrail();

                    omp.setSpectator(true);
                    omp.teleportToMap();
                    omp.clearInventory();
                    omp.updateInventory();
                    omp.getPlayer().setGameMode(GameMode.SPECTATOR);
                    getSpectatorKit(omp).setItems(omp.getPlayer());
                }
            });
            npc.setItemInHand(new ItemStack(Material.ENDER_PEARL));
        }
        {
            Npc npc = new Npc(Mob.BLAZE, new Location(lobby, -4.5, 75, -11.5, 0, 0), "§e§lOMT Shop", new Npc.InteractAction() {
                @Override
                public void click(PlayerInteractEntityEvent event, OMPlayer omp) {
                    new OMTShopInventory().open(omp);
                }
            });
        }
        {
            NpcArmorStand npc = new NpcArmorStand(new Location(lobby, -2.5, 75, 1, -135, 0), false);
            npc.setBodyPose(EulerAngle.ZERO.setX(-0.1).setY(0).setZ(0));
            npc.setHeadPose(EulerAngle.ZERO.setX(-0.15).setY(0).setZ(0));
            npc.setLeftArmPose(EulerAngle.ZERO.setX(0).setY(0).setZ(0));
            npc.setLeftLegPose(EulerAngle.ZERO.setX(-0.5).setY(0).setZ(0));
            npc.setRightArmPose(EulerAngle.ZERO.setX(-0.75).setY(0).setZ(0));
            npc.setRightLegPose(EulerAngle.ZERO.setX(0.5).setY(0).setZ(0));
            npc.setGravity(false);
            npc.setVisible(false);
            npc.setItemInHand(new ItemStack(Material.IRON_SWORD, 1));
            npc.spawn();
        }
        {
            NpcArmorStand npc = new NpcArmorStand(new Location(lobby, -2, 75, -2.5, -45, 0), false);
            npc.setBodyPose(EulerAngle.ZERO.setX(-0.1).setY(0).setZ(0));
            npc.setHeadPose(EulerAngle.ZERO.setX(-0.15).setY(0).setZ(0));
            npc.setLeftArmPose(EulerAngle.ZERO.setX(0).setY(0).setZ(0));
            npc.setLeftLegPose(EulerAngle.ZERO.setX(-0.5).setY(0).setZ(0));
            npc.setRightArmPose(EulerAngle.ZERO.setX(-0.75).setY(0).setZ(0));
            npc.setRightLegPose(EulerAngle.ZERO.setX(0.5).setY(0).setZ(0));
            npc.setGravity(false);
            npc.setVisible(false);
            npc.setItemInHand(new ItemStack(Material.IRON_SWORD, 1));
            npc.spawn();
        }
        {
            NpcArmorStand npc = new NpcArmorStand(new Location(lobby, 1.5, 75, -2, 45, 0), false);
            npc.setBodyPose(EulerAngle.ZERO.setX(-0.1).setY(0).setZ(0));
            npc.setHeadPose(EulerAngle.ZERO.setX(-0.15).setY(0).setZ(0));
            npc.setLeftArmPose(EulerAngle.ZERO.setX(0).setY(0).setZ(0));
            npc.setLeftLegPose(EulerAngle.ZERO.setX(-0.5).setY(0).setZ(0));
            npc.setRightArmPose(EulerAngle.ZERO.setX(-0.75).setY(0).setZ(0));
            npc.setRightLegPose(EulerAngle.ZERO.setX(0.5).setY(0).setZ(0));
            npc.setGravity(false);
            npc.setVisible(false);
            npc.setItemInHand(new ItemStack(Material.IRON_SWORD, 1));
            npc.spawn();
        }
        {
            NpcArmorStand npc = new NpcArmorStand(new Location(lobby, 1, 75, 1.5, 135, 0), false);
            npc.setBodyPose(EulerAngle.ZERO.setX(-0.1).setY(0).setZ(0));
            npc.setHeadPose(EulerAngle.ZERO.setX(-0.15).setY(0).setZ(0));
            npc.setLeftArmPose(EulerAngle.ZERO.setX(0).setY(0).setZ(0));
            npc.setLeftLegPose(EulerAngle.ZERO.setX(-0.5).setY(0).setZ(0));
            npc.setRightArmPose(EulerAngle.ZERO.setX(-0.75).setY(0).setZ(0));
            npc.setRightLegPose(EulerAngle.ZERO.setX(0.5).setY(0).setZ(0));
            npc.setGravity(false);
            npc.setVisible(false);
            npc.setItemInHand(new ItemStack(Material.IRON_SWORD, 1));
            npc.spawn();
        }
        {
            NpcArmorStand npc = new NpcArmorStand(new Location(lobby, -9.25, 73.5, 11.92, 90, 0), false);
            npc.setBodyPose(EulerAngle.ZERO.setX(-0.1).setY(0).setZ(0));
            npc.setHeadPose(EulerAngle.ZERO.setX(-0.15).setY(0).setZ(0));
            npc.setLeftArmPose(EulerAngle.ZERO.setX(0).setY(0).setZ(0));
            npc.setLeftLegPose(EulerAngle.ZERO.setX(-0.5).setY(0).setZ(0));
            npc.setRightArmPose(EulerAngle.ZERO.setX(-2).setY(0).setZ(0.4));
            npc.setRightLegPose(EulerAngle.ZERO.setX(0.5).setY(0).setZ(0));
            npc.setGravity(false);
            npc.setVisible(false);
            npc.setItemInHand(new ItemStack(Material.DIAMOND_SWORD, 1));
            npc.spawn();
        }
        {
            NpcArmorStand npc = new NpcArmorStand(new Location(lobby, -10.25, 73.1, 9.875, 90, 0), false);
            npc.setBodyPose(EulerAngle.ZERO.setX(-0.1).setY(0).setZ(0));
            npc.setHeadPose(EulerAngle.ZERO.setX(-0.15).setY(0).setZ(0));
            npc.setLeftArmPose(EulerAngle.ZERO.setX(0).setY(0).setZ(0));
            npc.setLeftLegPose(EulerAngle.ZERO.setX(-0.5).setY(0).setZ(0));
            npc.setRightArmPose(EulerAngle.ZERO.setX(-3.1).setY(0).setZ(0));
            npc.setRightLegPose(EulerAngle.ZERO.setX(0.5).setY(0).setZ(0));
            npc.setGravity(false);
            npc.setVisible(false);
            npc.setItemInHand(new ItemStack(Material.GOLDEN_APPLE, 1));
            npc.spawn();
        }
        {
            NpcArmorStand npc = new NpcArmorStand(new Location(lobby, -10.1, 73.15, 9.8, 60, 0), false);
            npc.setBodyPose(EulerAngle.ZERO.setX(-0.1).setY(0).setZ(0));
            npc.setHeadPose(EulerAngle.ZERO.setX(-0.15).setY(0).setZ(0));
            npc.setLeftArmPose(EulerAngle.ZERO.setX(0).setY(0).setZ(0));
            npc.setLeftLegPose(EulerAngle.ZERO.setX(-0.5).setY(0).setZ(0));
            npc.setRightArmPose(EulerAngle.ZERO.setX(-3.1).setY(0).setZ(-0.25));
            npc.setRightLegPose(EulerAngle.ZERO.setX(0.5).setY(0).setZ(0));
            npc.setGravity(false);
            npc.setVisible(false);
            npc.setItemInHand(new ItemStack(Material.GOLDEN_APPLE, 1));
            npc.spawn();
        }
        {
            FloatingItem item = new FloatingItem(new Location(lobby, 9.5, 75, -12.5, 0, 0));
            new FloatingItem.ItemInstance(item, false) {
                @Override
                public ItemStack getItemStack() {
                    return new ItemStack(Material.ENDER_PEARL, 1);
                }

                @Override
                public String getDisplayName() {
                    return null;
                }

                @Override
                public boolean isDisplayNameVisible() {
                    return false;
                }
            };
            item.spawn();
        }
        {
            FloatingItem item = new FloatingItem(new Location(lobby, 10.5, 74.5, -9.5, 0, 0));
            new FloatingItem.ItemInstance(item, false) {
                @Override
                public ItemStack getItemStack() {
                    return new ItemStack(Material.ENDER_PEARL, 1);
                }

                @Override
                public String getDisplayName() {
                    return null;
                }

                @Override
                public boolean isDisplayNameVisible() {
                    return false;
                }
            };
            item.spawn();
        }
        {
            FloatingItem item = new FloatingItem(new Location(lobby, 2.5, 76, 8.5, -180, 0), new FloatingItem.ClickAction() {
                @Override
                public void click(PlayerInteractAtEntityEvent event, OMPlayer player, FloatingItem item) {
                    new MasteryInventory().open(player);
                }
            });
            new FloatingItem.ItemInstance(item, false) {
                @Override
                public ItemStack getItemStack() {
                    return new ItemStack(Material.DIAMOND_SWORD, 1);
                }

                @Override
                public String getDisplayName() {
                    return "§c§lMasteries";
                }

                @Override
                public boolean isDisplayNameVisible() {
                    return true;
                }
            };
            item.spawn();
            masteryNpc = item;
        }
        {
            FloatingItem item = new FloatingItem(new Location(lobby, -9.5, 72.5, 8.5, 0, 0), new FloatingItem.ClickAction() {
                @Override
                public void click(PlayerInteractAtEntityEvent event, OMPlayer player, FloatingItem item) {
                    new KitSelectorInventory().open(player);
                }
            });
            new FloatingItem.ItemInstance(item, false) {
                @Override
                public ItemStack getItemStack() {
                    return new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
                }

                @Override
                public String getDisplayName() {
                    return "§b§lKit Selector";
                }

                @Override
                public boolean isDisplayNameVisible() {
                    return true;
                }
            };
            item.spawn();
        }
    }

    @Override
    public Server getServerType() {
        return Server.KITPVP;
    }

    @Override
    public int getMaxPlayers() {
        return 100;
    }

    @Override
    public OMPlayer newPlayerInstance(Player player) {
        return new KitPvPPlayer(player);
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
    public String[] getVoteMessages(OMPlayer omp) {
        return voteMessages;
    }

    @Override
    public boolean cleanUpPlayerData() {
        return true;
    }

    @Override
    public List<String> getScoreboardTitles() {
        return Arrays.asList("§6§lOrbitMines§c§lKitPvP", "§e§lO§6§lrbitMines§c§lKitPvP", "§e§lOr§6§lbitMines§c§lKitPvP", "§e§lOrb§6§litMines§c§lKitPvP", "§e§lOrbi§6§ltMines§c§lKitPvP", "§e§lOrbit§6§lMines§c§lKitPvP", "§e§lOrbitM§6§lines§c§lKitPvP", "§e§lOrbitMi§6§lnes§c§lKitPvP", "§e§lOrbitMin§6§les§c§lKitPvP", "§e§lOrbitMine§6§ls§c§lKitPvP", "§e§lOrbitMines§c§lKitPvP", "§e§lOrbitMines§4§lK§c§litPvP", "§e§lOrbitMines§4§lKi§c§ltPvP", "§e§lOrbitMines§4§lKit§c§lPvP", "§e§lOrbitMines§4§lKitP§c§lvP", "§e§lOrbitMines§4§lKitPv§c§lP", "§e§lOrbitMines§4§lKitPvP", "§6§lO§e§lrbitMines§4§lKitPvP", "§6§lOr§e§lbitMines§4§lKitPvP", "§6§lOrb§e§litMines§4§lKitPvP", "§6§lOrbi§e§ltMines§4§lKitPvP", "§6§lOrbit§e§lMines§4§lKitPvP", "§6§lOrbitM§e§lines§4§lKitPvP", "§6§lOrbitMi§e§lnes§4§lKitPvP", "§6§lOrbitMin§e§les§4§lKitPvP", "§6§lOrbitMine§e§ls§4§lKitPvP", "§6§lOrbitMines§4§lKitPvP", "§6§lOrbitMines§c§lK§4§litPvP", "§6§lOrbitMines§c§lKi§4§ltPvP", "§6§lOrbitMines§c§lKit§4§lPvP", "§6§lOrbitMines§c§lKitP§4§lvP", "§6§lOrbitMines§c§lKitPv§4§lP", "§6§lOrbitMines§c§lKitPv§4§lP", "§6§lOrbitMines§c§lKitPvP");
    }

    @Override
    public void registerEvents() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new ClickEvent(), this);
        pluginManager.registerEvents(new DamageByEntityEvent(), this);
        pluginManager.registerEvents(new DamageEvent(), this);
        pluginManager.registerEvents(new DeathEvent(), this);
        pluginManager.registerEvents(new ExpChangeEvent(), this);
        pluginManager.registerEvents(new ExplodeEvent(), this);
        pluginManager.registerEvents(new FadeEvent(), this);
        pluginManager.registerEvents(new InteractEvent(), this);
        pluginManager.registerEvents(new MoveEvent(), this);
        pluginManager.registerEvents(new ProjectileEvents(), this);
        pluginManager.registerEvents(new RegainHealthEvent(), this);
        pluginManager.registerEvents(new SpreadEvent(), this);
        pluginManager.registerEvents(new TeleportEvent(), this);
    }

    @Override
    public void registerCommands() {
        /* owner */
        new CommandFreeKit();
        /* normal */
        new CommandKit();
    }

    @Override
    public void registerRunnables() {
        new ArrowRunnable();
        new BleedRunnable();
        new BoosterRunnable();
        new FreeKitRunnable();
        new LobbyRunnable();
        new MapRunnable();
        new MasteryNpcRunnable();
        new TopKillsRunnable();
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
        return null;
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
