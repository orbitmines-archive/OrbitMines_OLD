package com.orbitmines.kitpvp.handlers;

import com.orbitmines.api.Data;
import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.chat.Title;
import com.orbitmines.api.spigot.handlers.itembuilders.PotionBuilder;
import com.orbitmines.api.spigot.handlers.itembuilders.PotionItemBuilder;
import com.orbitmines.api.spigot.handlers.kit.KitInteractive;
import com.orbitmines.api.spigot.handlers.npc.FloatingItem;
import com.orbitmines.api.spigot.handlers.npc.Hologram;
import com.orbitmines.api.spigot.handlers.npc.NpcArmorStand;
import com.orbitmines.api.spigot.nms.entity.EntityNms;
import com.orbitmines.kitpvp.KitPvP;
import com.orbitmines.kitpvp.handlers.playerdata.KitPvPData;
import com.orbitmines.kitpvp.handlers.playerdata.MasteriesData;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public class KitPvPPlayer extends OMPlayer {

    private static List<KitPvPPlayer> players = new ArrayList<>();

    private KitPvP kitPvP;

    private KitHandler kitActive;
    private boolean spectator;
    private boolean spawnProtection;

    private int currentStreak;

    private int bleedingTime;
    private int arrowSeconds;

    private NpcArmorStand npcLastSelected;

    private FloatingItem.ItemInstance healthItem;
    private FloatingItem.ItemInstance regenItem;
    private FloatingItem.ItemInstance damageItem;
    private FloatingItem.ItemInstance knockbackItem;

    public KitPvPPlayer(Player player) {
        super(player);

        players.add(this);
        kitPvP = KitPvP.getInstance();

        spectator = false;
        bleedingTime = -1;
    }

    @Override
    public String getPrefix() {
        return spectator ? "§eSpec §8| " : "";
    }

    @Override
    public void onVote(int votes) {
        kitPvP().addCoins(votes * 500);

        new Title("§b§lVote", "§6+" + (votes * 500) + " Coins", 20, 40, 20).send(player);
    }

    @Override
    protected void onLogin() {
        setScoreboard(new KitPvPScoreboard(this));

        clearInventory();
        player.setGameMode(GameMode.SURVIVAL);
        kitPvP.getLobbyKit(this).setItems(player);

        msgJoin();
        msgJoinTitle();
        broadcastJoinMessage();

        {
            healthItem = new FloatingItem.ItemInstance(kitPvP.getHealthNpc(), true) {
                @Override
                public ItemStack getItemStack() {
                    return new ItemStack(Material.BEETROOT, 1);
                }

                @Override
                public String getDisplayName() {
                    return "§7" + kitPvP().getLastKitHandler().getMaxHealth();
                }

                @Override
                public boolean isDisplayNameVisible() {
                    return true;
                }
            };
            healthItem.spawn(player);
        }
        {
            KitPvPPlayer omp = this;
            regenItem = new FloatingItem.ItemInstance(kitPvP.getRegenNpc(), true) {
                @Override
                public ItemStack getItemStack() {
                    return new PotionItemBuilder(PotionItemBuilder.Type.NORMAL, new PotionBuilder(PotionEffectType.REGENERATION, 0, 0, false, false, Color.FUCHSIA)).build();
                }

                @Override
                public String getDisplayName() {
                    return "§7" + omp.getMessage(kitPvP().getLastKitHandler().getHealthRegenType(omp).getMessage());
                }

                @Override
                public boolean isDisplayNameVisible() {
                    return true;
                }
            };
            regenItem.spawn(player);
        }
        {
            damageItem = new FloatingItem.ItemInstance(kitPvP.getDamageNpc(), true) {
                @Override
                public ItemStack getItemStack() {
                    return new ItemStack(Material.WOOD_SWORD, 1);
                }

                @Override
                public String getDisplayName() {
                    return "§7" + (kitPvP().getLastKitHandler().getDamageMultiplier() * 100) + "%";
                }

                @Override
                public boolean isDisplayNameVisible() {
                    return true;
                }
            };
            damageItem.spawn(player);
        }
        {
            knockbackItem = new FloatingItem.ItemInstance(kitPvP.getKnockbackNpc(), true) {
                @Override
                public ItemStack getItemStack() {
                    return new ItemStack(Material.SHIELD, 1);
                }

                @Override
                public String getDisplayName() {
                    return "§7" + (kitPvP().getLastKitHandler().getKnockbackMultiplier() * 100) + "%";
                }

                @Override
                public boolean isDisplayNameVisible() {
                    return true;
                }
            };
            knockbackItem.spawn(player);
        }
        {
            npcLastSelected = new NpcArmorStand(new Location(kitPvP.getLobby(), -11.5, 74, 10.5, -135, 0), true, new NpcArmorStand.ClickAction() {
                @Override
                public void click(PlayerInteractAtEntityEvent event, OMPlayer player, NpcArmorStand item) {
                    KitPvPData data = kitPvP();

                    if (data.getUnlockedLevel(data.getLastSelected()) >= data.getLastSelectedLevel()) {
                        giveKit(data.getLastSelected(), data.getLastSelectedLevel());
                        teleportToMap();
                    } else {
                        data.setLastSelected(KitPvPKit.Type.KNIGHT);
                        data.setLastSelectedLevel(1);
                        updateNpcs();
                    }
                }
            });
            npcLastSelected.setGravity(false);
            npcLastSelected.setVisible(false);
            npcLastSelected.spawn(player);
        }

        updateNpcs();
    }

    @Override
    protected void onLogout() {
        if (kitActive != null)
            kitPvP().addDeath();

        healthItem.delete();
        regenItem.delete();
        npcLastSelected.delete();

        players.remove(this);
    }

    @Override
    public boolean canReceiveVelocity() {
        return true;
    }

    private void updateNpcs() {
        healthItem.updateName();
        regenItem.updateName();
        damageItem.updateName();
        knockbackItem.updateName();

        KitHandler handler = kitPvP().getLastKitHandler();
        KitInteractive kit = handler.getKit();

        npcLastSelected.setHelmet(kit.getHelmet());
        npcLastSelected.setChestPlate(kit.getChestplate());
        npcLastSelected.setLeggings(kit.getLeggings());
        npcLastSelected.setBoots(kit.getBoots());
        npcLastSelected.setItemInHand(kit.getFirstItem());
        npcLastSelected.setCustomName(handler.getType().getDisplayName() + " §a§lLvL " + handler.getLevel());
        npcLastSelected.setCustomNameVisible(true);

        npcLastSelected.update();
        npcLastSelected.createForWatchers();
    }

    /* Getters & Setters */
    public KitHandler getKitActive() {
        return kitActive;
    }

    public void setKitActive(KitHandler kitActive) {
        this.kitActive = kitActive;
    }

    public boolean isSpectator() {
        return spectator;
    }

    public void setSpectator(boolean spectator) {
        this.spectator = spectator;
    }

    public boolean hasSpawnProtection() {
        return spawnProtection;
    }

    public boolean isPlayer() {
        return !spectator;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public int getBleedingTime() {
        return bleedingTime;
    }

    public void setBleedingTime(int bleedingTime) {
        this.bleedingTime = bleedingTime;
    }

    public void tickBleedingTime() {
        this.bleedingTime--;
    }

    public int getArrowSeconds() {
        return arrowSeconds;
    }

    public void setArrowSeconds(int arrowSeconds) {
        this.arrowSeconds = arrowSeconds;
    }

    public void tickArrowTimer() {
        this.arrowSeconds--;
    }

    public void giveKit(KitPvPKit.Type type, int level) {
        clearInventory();
        clearPotionEffects();

        KitHandler handler = type.getKit().getHandlers()[level - 1];
        setHandler(handler);

        EntityNms nms = kitPvP.getApi().getNms().entity();
        nms.setAttribute(player, EntityNms.Attribute.MAX_HEALTH, handler.getMaxHealth());
        player.setHealth(handler.getMaxHealth());
        nms.setAttribute(player, EntityNms.Attribute.KNOCKBACK_RESISTANCE, (1 - handler.getKnockbackMultiplier()));
        nms.setAttribute(player, EntityNms.Attribute.ATTACK_SPEED, 16.0D);

        handler.getKit().setItems(player);

        KitPvPData data = kitPvP();
        data.setLastSelected(type);
        data.setLastSelectedLevel(level);
        updateNpcs();

        Title t = new Title("", getMessage(new Message("§7Kit Geselecteerd: '§b§l" + type.getDisplayName() + "§7' §7§o(§a§oLvL " + level + "§7§o)", "§7Selected Kit: '§b§l" + type.getDisplayName() + "§7' §7§o(§a§oLvL " + level + "§7§o)")), 20, 40, 20);
        t.send(getPlayer());
    }

    public void setHandler(KitHandler handler) {
        this.kitActive = handler;
        this.arrowSeconds = -1;

        if (handler == null)
            return;

        cooldowns.clear();
    }

    public void teleportToMap() {
        KitPvPMap map = kitPvP.getCurrentMap();

        if (!spectator) {
            player.teleport(map.getRandomSpawn());

            spawnProtection = true;

            new BukkitRunnable() {
                @Override
                public void run() {
                    spawnProtection = false;
                }
            }.runTaskLater(api, 20);
        } else {
            player.teleport(map.getSpectatorSpawn());
        }

        map.sendJoinMessage(this);
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 5, 1);
    }

    public boolean hasVoted() {
        for (KitPvPMap map : KitPvPMap.getMaps()) {
            if (map.getVotes().contains(getUUID()))
                return true;
        }
        return false;
    }

    public void vote(KitPvPMap map) {
        map.getVotes().add(getUUID());

        for (KitPvPMap kitPvPMap : KitPvPMap.getMaps()) {
            kitPvPMap.updateVoteSign(this);
        }
    }

    public double getCoinBooster() {
        switch (vipRank) {
            case DIAMOND:
                return 1.20;
            case EMERALD:
                return 1.50;
            default:
                return 1.00;
        }
    }

    public double getExpBooster() {
        switch (vipRank) {
            case DIAMOND:
                return 2.00;
            case EMERALD:
                return 2.50;
            case GOLD:
                return 1.50;
            default:
                return 1.00;
        }
    }

    public void createKillHologram(KitPvPPlayer omp, int coinsAdded) {
        Hologram h = new Hologram(player.getLocation(), true);
        h.addLine(omp.getMessage(new Message("§7Je hebt §6" + getColorName() + "§7 gedood!", "§7You killed §6" + getColorName() + "§7!")));
        h.addLine("§6§l+" + coinsAdded + " Coins");
        h.create(omp.getPlayer());

        new BukkitRunnable() {
            public void run() {
                h.delete();
            }
        }.runTaskLater(kitPvP, 100);
    }

    public KitPvPData kitPvP() {
        return (KitPvPData) data(Data.KITPVP);
    }

    public MasteriesData masteries() {
        return (MasteriesData) data(Data.MASTERIES);
    }

    public static KitPvPPlayer getPlayer(Player player) {
        for (KitPvPPlayer omp : players) {
            if (omp.getPlayer() == player)
                return omp;
        }
        return null;
    }

    public static List<KitPvPPlayer> getKitPvPPlayers() {
        return players;
    }
}
