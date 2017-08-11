package com.orbitmines.hub;

import com.orbitmines.api.*;
import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.OrbitMinesServer;
import com.orbitmines.api.spigot.enablers.*;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.PreventionSet;
import com.orbitmines.api.spigot.handlers.inventory.ServerSelectorInventory;
import com.orbitmines.api.spigot.handlers.inventory.perks.CosmeticPerksInventory;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.kit.KitInteractive;
import com.orbitmines.api.spigot.handlers.playerdata.PlayerData;
import com.orbitmines.api.spigot.handlers.worlds.voidgenerator.WorldCreatorVoid;
import com.orbitmines.hub.events.MoveEvent;
import com.orbitmines.hub.handlers.HubPlayer;
import com.orbitmines.hub.handlers.inventory.SettingsInventory;
import com.orbitmines.hub.handlers.playerdata.HubData;
import com.orbitmines.hub.runnables.PortalRunnable;
import com.orbitmines.hub.runnables.SpawnBoundariesRunnable;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.PluginManager;
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
    private Location spawnLocation;

    private String[] voteMessages = { "§e§l1 OrbitMines Token" };

    @Override
    public void onEnable() {
        instance = this;
        api = OrbitMinesApi.getApi();

        api.enableData(Data.HUB, new Data.Register() {
            @Override
            public PlayerData getData(OMPlayer omp) {
                return new HubData(omp);
            }
        });

        api.setup(this);

        lobby = api.getWorldLoader().fromZip(this, "Hub", true, WorldCreatorVoid.class);
        lobby.setGameRuleValue("doDaylightCycle", "false");
        lobby.setTime(20000);

        spawnLocation = new Location(lobby, 0.5, 74, 0.5);

        /* Setup Holograms after world is loaded */
        api.setupHolograms();

        PreventionSet set = new PreventionSet();
        set.prevent(lobby,
                PreventionSet.Prevention.BLOCK_BREAK,
                PreventionSet.Prevention.BLOCK_INTERACTING,
                PreventionSet.Prevention.BLOCK_PLACE,
                PreventionSet.Prevention.FOOD_CHANGE,
                PreventionSet.Prevention.MOB_SPAWN,
                PreventionSet.Prevention.MONSTER_EGG_USAGE,
                PreventionSet.Prevention.SWAP_HAND_ITEMS,
                PreventionSet.Prevention.WEATHER_CHANGE,
                PreventionSet.Prevention.ITEM_DROP,
                PreventionSet.Prevention.ITEM_PICKUP,
                PreventionSet.Prevention.PHYSICAL_INTERACTING,
                PreventionSet.Prevention.BUCKET_USAGE,
                PreventionSet.Prevention.CLICK_PLAYER_INVENTORY,
                PreventionSet.Prevention.PLAYER_DAMAGE);

        registerLobbyKit();
    }

    public static Hub getInstance() {
        return instance;
    }

    public OrbitMinesApi getApi() {
        return api;
    }

    public World getLobby() {
        return lobby;
    }

    private void registerLobbyKit() {
        KitInteractive.InteractAction serverSelector = new KitInteractive.InteractAction(new ItemBuilder(Material.ENDER_PEARL, 1, 0, "§3§nServer Selector").build()) {
            @Override
            public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
                event.setCancelled(true);
                omp.updateInventory();

                new ServerSelectorInventory().open(omp);
            }
        };
        KitInteractive.InteractAction cosmeticPerks = new KitInteractive.InteractAction(new ItemBuilder(Material.ENDER_CHEST, 1, 0, "§9§nCosmetic Perks").build()) {
            @Override
            public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
                if (omp.canReceiveVelocity())
                    new CosmeticPerksInventory().open(omp);
            }
        };
        KitInteractive.InteractAction fly = new KitInteractive.InteractAction(new ItemBuilder(Material.FEATHER, 1, 0, "§f§nFly").build()) {
            @Override
            public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
                if (omp.isEligible(VipRank.IRON)) {
                    if (!omp.canReceiveVelocity())
                        return;

                    Player p = omp.getPlayer();
                    p.playEffect(p.getLocation(), Effect.STEP_SOUND, 80);
                    p.setAllowFlight(!p.getAllowFlight());
                    p.setFlying(p.getAllowFlight());
                    omp.sendMessage(new Message("§7Je §fFly§7 mode staat nu " + omp.statusString(p.getAllowFlight()) + "§7.", omp.statusString(p.getAllowFlight()) + " §7your §fFly§7 mode!"));
                } else {
                    omp.msgRequiredVipRank(VipRank.IRON);
                }
            }
        };

        {
            KitInteractive kit = new KitInteractive(Language.DUTCH.toString());
            {
                ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
                BookMeta meta = (BookMeta) item.getItemMeta();
                meta.setDisplayName("§4§nServer Regels");
                meta.addPage("1");
                meta.setPage(1, "   §6§lOrbitMines§4§lRegels" + "\n" + "§0§m-------------------" + "\n" + "§4NIET§0 Adverteren!" + "\n" + "§0Let op je taalgebruik!" + "\n" + "Luister naar de Staff!" + "\n" + "§4GEEN§0 Bugs gebruiken!" + "\n" + "§4NIET§0 hacken!" + "\n" + "§4NIET§0 spammen!" + "\n" + "§4NIET§0 spelers pesten!" + "\n" + "§0\n" + "§0§lVeel Plezier!");
                meta.setAuthor("§6§lOrbitMines §4§lNetwork");
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
            kit.setItem(3, new KitInteractive.InteractAction(new ItemBuilder(Material.REDSTONE_TORCH_ON, 1, 0, "§c§nInstellingen").build()) {
                @Override
                public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
                    event.setCancelled(true);
                    omp.updateInventory();

                    if (!omp.canReceiveVelocity())
                        return;

                    omp.getPlayer().playEffect(omp.getPlayer().getLocation(), Effect.STEP_SOUND, 152);
                    new SettingsInventory().open(omp);
                }
            });
            kit.setItem(4, serverSelector);
            kit.setItem(7, cosmeticPerks);
            kit.setItem(8, fly);
        }
        {
            KitInteractive kit = new KitInteractive(Language.ENGLISH.toString());
            {
                ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
                BookMeta meta = (BookMeta) item.getItemMeta();
                meta.setDisplayName("§4§nServer Rules");
                meta.addPage("1");
                meta.setPage(1, "   §6§lOrbitMines§4§lRules" + "\n" + "§0§m-------------------" + "\n" + "§4DO NOT§0 Advertise!" + "\n" + "§0Watch your Language!" + "\n" + "Listen to Staff!" + "\n" + "§4DO NOT§0 Abuse Bugs!" + "\n" + "§4DO NOT§0 Hack!" + "\n" + "§4DO NOT§0 Spam!" + "\n" + "§4DO NOT§0 Bully Players!" + "\n" + "§0\n" + "§0§lHave Fun!");
                meta.setAuthor("§6§lOrbitMines §4§lNetwork");
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
            kit.setItem(3, new KitInteractive.InteractAction(new ItemBuilder(Material.REDSTONE_TORCH_ON, 1, 0, "§c§nSettings").build()) {
                @Override
                public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
                    event.setCancelled(true);
                    omp.updateInventory();

                    if (!omp.canReceiveVelocity())
                        return;

                    omp.getPlayer().playEffect(omp.getPlayer().getLocation(), Effect.STEP_SOUND, 152);
                    new SettingsInventory().open(omp);
                }
            });
            kit.setItem(4, serverSelector);
            kit.setItem(7, cosmeticPerks);
            kit.setItem(8, fly);
        }
    }

    /* OrbitMinesServer */

    @Override
    public Server getServerType() {
        return Server.HUB;
    }

    @Override
    public int getMaxPlayers() {
        return 100;
    }

    @Override
    public OMPlayer newPlayerInstance(Player player) {
        return new HubPlayer(player);
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
        return Arrays.asList("§6§lOrbitMines§4§lNetwork", "§e§lO§6§lrbitMines§4§lNetwork", "§e§lOr§6§lbitMines§4§lNetwork", "§e§lOrb§6§litMines§4§lNetwork", "§e§lOrbi§6§ltMines§4§lNetwork", "§e§lOrbit§6§lMines§4§lNetwork", "§e§lOrbitM§6§lines§4§lNetwork", "§e§lOrbitMi§6§lnes§4§lNetwork", "§e§lOrbitMin§6§les§4§lNetwork", "§e§lOrbitMine§6§ls§4§lNetwork", "§e§lOrbitMines§4§lNetwork", "§e§lOrbitMines§c§lN§4§letwork", "§e§lOrbitMines§c§lNe§4§ltwork", "§e§lOrbitMines§c§lNet§4§lwork", "§e§lOrbitMines§c§lNetw§4§lork", "§e§lOrbitMines§c§lNetwo§4§lrk", "§e§lOrbitMines§c§lNetwor§4§lk", "§e§lOrbitMines§c§lNetwork", "§6§lO§e§lrbitMines§c§lNetwork", "§6§lOr§e§lbitMines§c§lNetwork", "§6§lOrb§e§litMines§c§lNetwork", "§6§lOrbi§e§ltMines§c§lNetwork", "§6§lOrbit§e§lMines§c§lNetwork", "§6§lOrbitM§e§lines§c§lNetwork", "§6§lOrbitMi§e§lnes§c§lNetwork", "§6§lOrbitMin§e§les§c§lNetwork", "§6§lOrbitMine§e§ls§c§lNetwork", "§6§lOrbitMines§c§lNetwork", "§6§lOrbitMines§4§lN§c§letwork", "§6§lOrbitMines§4§lNe§c§ltwork", "§6§lOrbitMines§4§lNet§c§lwork", "§6§lOrbitMines§4§lNetw§c§lork", "§6§lOrbitMines§4§lNetwo§c§lrk", "§6§lOrbitMines§4§lNetwor§c§lk", "§6§lOrbitMines§4§lNetwork");
    }

    @Override
    public void registerEvents() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new MoveEvent(), this);
    }

    @Override
    public void registerCommands() {

    }

    @Override
    public void registerRunnables() {
        new PortalRunnable();
        new SpawnBoundariesRunnable();
    }

    @Override
    public void format(AsyncPlayerChatEvent event, OMPlayer omp) {
        if (((HubPlayer) omp).canChat()) {
            event.setFormat(omp.getChatFormat());
        } else {
            event.setCancelled(true);
        }
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
