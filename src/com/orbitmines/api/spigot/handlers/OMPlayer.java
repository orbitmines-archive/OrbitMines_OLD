package com.orbitmines.api.spigot.handlers;

import com.orbitmines.api.*;
import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.handlers.chat.Title;
import com.orbitmines.api.spigot.handlers.inventory.OMInventory;
import com.orbitmines.api.spigot.handlers.itembuilders.PotionBuilder;
import com.orbitmines.api.spigot.handlers.playerdata.*;
import com.orbitmines.api.spigot.handlers.scoreboard.Scoreboard;
import com.orbitmines.api.spigot.handlers.scoreboard.ScoreboardSet;
import com.orbitmines.api.spigot.runnable.OMRunnable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.*;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public abstract class OMPlayer {

    private static List<OMPlayer> players = new ArrayList<>();

    protected OrbitMinesApi api;

    protected final Player player;

    protected Scoreboard scoreboard;
    protected OMInventory lastInventory;

    protected List<PlayerData> data;

    private boolean opMode;

    /* Important Data, not stored in PlayerData */
    protected StaffRank staffRank;
    protected VipRank vipRank;
    protected int votes;
    protected boolean receivedMonthlyBonus;

    /* Data that does not save cross-servers */
    protected Location tpLocation;

    protected String afk;

    protected Map<Cooldown, Long> cooldowns;

    protected List<Player> hiddenPlayers;

    public OMPlayer(Player player) {
        api = OrbitMinesApi.getApi();
        players.add(this);

        /* Load Defaults */
        this.player = player;
        this.scoreboard = new Scoreboard(this);

        this.data = new ArrayList<>();
        this.opMode = false;
        this.staffRank = StaffRank.NONE;
        this.vipRank = VipRank.NONE;
        this.votes = 0;
        this.receivedMonthlyBonus = false;

        this.cooldowns = new HashMap<>();
        this.hiddenPlayers = new ArrayList<>();
    }

    /* Called on player chat */
    public abstract String getPrefix();

    /* Called when a players votes */
    public abstract void vote();

    /* Called after the player logs in */
    protected abstract void onLogin();

    /* Called before the player logs out */
    protected abstract void onLogout();

    /* Called when a player receives knock back, example: gadgets */
    public abstract boolean canReceiveVelocity();

    public void login() {
        player.setOp(false);

        //TODO

        onLogin();
    }

    public void logout() {
        onLogout();

        player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);

        //TODO

        players.remove(this);
    }

    /* Getters & Setters */
    public UUID getUUID() {
        return player.getUniqueId();
    }

    public Player getPlayer() {
        return player;
    }

    public String getChatFormat() {
        String nickName = general().getNickName();

        ChatColorData data = chatcolors();
        return getPrefix() + (nickName == null ? getName() : getChatPrefix() + getRankColor() + "*" + nickName + getRankColor() + "*") + "§7 » " + data.getChatColor().color().getChatColor() + data.type() + "%2$s";
    }

    /* Returns [COLOR][BOLD][RANK] [COLOR][NAME] - NONE; [COLOR][NAME] */
    public String getName() {
        return getChatPrefix() + player.getName();
    }

    /* Returns [COLOR] */
    public String getRankColor() {
        return staffRank == StaffRank.NONE ? vipRank.getColor() : staffRank.getColor();
    }

    /* Returns [COLOR][NAME] */
    public String getColorName() {
        return getRankColor() + player.getName();
    }

    /* Returns [COLOR][BOLD][RANK] [COLOR] - NONE; [COLOR]*/
    public String getChatPrefix() {
        return staffRank == StaffRank.NONE ? vipRank.getPrefix() : staffRank.getPrefix();
    }

    /* Returns [COLOR][BOLD][RANK] [WHITE] - NONE; 'No Rank' */
    public String getScoreboardPrefix() {
        return staffRank == StaffRank.NONE ? vipRank.getScoreboardPrefix() : staffRank.getScoreboardPrefix();
    }

    /* Returns [COLOR][BOLD][RANK] - NONE; 'No Rank' */
    public String getRankString() {
        return staffRank == StaffRank.NONE ? vipRank.getRankString() : staffRank.getRankString();
    }

    /* Sets Header&Footer in Tablist */
    public void setTabList(String header, String footer) {
        api.getNms().tabList().send(player, header, footer);
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public void resetScoreboard() {
        scoreboard.set(null);
    }

    public void clearScoreboard() {
        scoreboard.clear();
    }

    public void setScoreboard(ScoreboardSet set) {
        set.updateTitle();
        set.updateScores();
        set.updateTeams();

        scoreboard.set(set);
    }

    public void updateScoreboard() {
        scoreboard.update();
    }

    public OMInventory getLastInventory() {
        return lastInventory;
    }

    public void setLastInventory(OMInventory lastInventory) {
        this.lastInventory = lastInventory;
    }

    public List<PlayerData> getData() {
        return data;
    }

    public PlayerData data(Data type) {
        for (PlayerData playerData : data) {
            if (playerData.getType() == type)
                return playerData;
        }
        return null;
    }

    public boolean isOpMode() {
        return opMode;
    }

    public void setOpMode(boolean opMode) {
        this.opMode = opMode;
        getPlayer().getPlayer().setOp(opMode);
    }

    public StaffRank getStaffRank() {
        return staffRank;
    }

    public void setStaffRank(StaffRank staffRank) {
        this.staffRank = staffRank;

        Title t = new Title("", getMessage(new Message("§7Je bent nu een " + staffRank.getRankString() + "§7!", "§7You are now " + staffRank.getRankString() + " " + (staffRank == StaffRank.OWNER ? "an" : "a") + "§7!")), 20, 80, 20);
        t.send(player);

        Database.get().update(Database.Table.PLAYERS, new Database.Where(Database.Column.NAME, player.getName()), new Database.Set(Database.Column.STAFFRANK, staffRank.toString()));
    }

    public VipRank getVipRank() {
        return vipRank;
    }

    public void setVipRank(VipRank vipRank) {
        this.vipRank = vipRank;

        Title t = new Title("", getMessage(new Message("§7Je bent nu een " + vipRank.getRankString() + "§7!", "§7You are now " + vipRank.getRankString() + " " + (vipRank == VipRank.EMERALD || vipRank == VipRank.IRON ? "an" : "a") + "§7!")), 20, 80, 20);
        t.send(player);

        Database.get().update(Database.Table.PLAYERS, new Database.Where(Database.Column.NAME, player.getName()), new Database.Set(Database.Column.VIPRANK, vipRank.toString()));
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public void addVote() {
        this.votes += 1;
    }

    public boolean hasReceivedMonthlyBonus() {
        return receivedMonthlyBonus;
    }

    public void setReceivedMonthlyBonus(boolean receivedMonthlyBonus) {
        this.receivedMonthlyBonus = receivedMonthlyBonus;
    }

    public Location getTpLocation() {
        return tpLocation;
    }

    public void setTpLocation(Location tpLocation) {
        this.tpLocation = tpLocation;
    }

    public boolean isAfk() {
        return afk != null;
    }

    public String getAfk() {
        return afk;
    }

    public void setAfk() {
        setAfk("null");
    }

    public void noLongerAfk() {
        setAfk(null);
    }

    public void setAfk(String afk) {
        String playerName = getName();

        if (afk != null) {
            broadcastMessage(new Message("§7 " + playerName + "§7 is nu §6AFK§7. (§7" + afk + "§7)", "§7 " + playerName + "§7 is now §6AFK§7. (§7" + afk + "§7)"));
        } else if (afk.equals("null")) {
            broadcastMessage(new Message("§7 " + playerName + "§7 is nu §6AFK§7.", "§7 " + playerName + "§7 is now §6AFK§7."));
        } else {
            if (!this.afk.equals("null")) {
                broadcastMessage(new Message("§7 " + playerName + "§7 is niet meer §6AFK§7.", "§7 " + playerName + "§7 is no longer §6AFK§7."));
            } else {
                broadcastMessage(new Message("§7 " + playerName + "§7 is niet meer §6AFK§7. (§7" + this.afk + "§7)", "§7 " + playerName + "§7 is no longer §6AFK§7. (§7" + this.afk + "§7)"));
            }
        }
        this.afk = afk;
    }

    public Map<Cooldown, Long> getCooldowns() {
        return cooldowns;
    }

    public long getCooldown(Cooldown cooldown) {
        if (this.cooldowns.containsKey(cooldown))
            return this.cooldowns.get(cooldown);

        return -1;
    }

    public void resetCooldown(Cooldown cooldown) {
        this.cooldowns.put(cooldown, System.currentTimeMillis());
        Cooldown.doubles().remove(this);

        if (cooldown == Cooldown.TELEPORTING) {
            trails().setLastParticle(null);
            tpLocation = player.getLocation();
        }
    }

    public void removeCooldown(Cooldown cooldown) {
        this.cooldowns.remove(cooldown);
        Cooldown.doubles().remove(this);

        if (cooldown == Cooldown.TELEPORTING)
            tpLocation = player.getLocation();
    }

    public boolean onCooldown(Cooldown cooldown) {
        if (this.cooldowns.containsKey(cooldown))
            return System.currentTimeMillis() - this.cooldowns.get(cooldown) < cooldown.getCooldown(this);

        return false;
    }

    public void updateCooldownActionBar() {
        for (Cooldown cooldown : this.cooldowns.keySet()) {
            if (cooldown.updateActionBar(this))
                return;
        }
    }


    /* Others */

    public void updateVotes() {
        this.votes = Database.get().getInt(Database.Table.PLAYERS, Database.Column.VOTES, new Database.Where(Database.Column.NAME, player.getName()));
    }

    public boolean isEligible(StaffRank... staffRanks) {
        if (staffRanks == null || staffRanks.length == 0)
            return true;

        for (StaffRank staffRank : staffRanks) {
            if (this.staffRank == staffRank)
                return true;
        }
        return false;
    }

    public boolean isEligible(VipRank... vipRanks) {
        if (vipRanks == null || vipRanks.length == 0 || isOpMode() && isEligible(StaffRank.OWNER))
            return true;

        for (VipRank vipRank : vipRanks) {
            if (this.vipRank == vipRank)
                return true;
        }
        return false;
    }

    public void sendMessage(Message message) {
        player.sendMessage(message.lang(general().getLanguage()));
    }

    public void broadcastMessage(Message message) {
        for (OMPlayer player : players) {
            player.sendMessage(message);
        }
    }

    public String getMessage(Message message) {
        return message.lang(general().getLanguage());
    }

    public String statusString(boolean bl){
        return bl ? getMessage(new Message("§a§lAAN", "§a§lENABLED")) : getMessage(new Message("§c§lUIT", "§c§lDISABLED"));
    }

    public BukkitTask sendDelayedMessage(OMRunnable.Time time, Message... messages) {
        return sendDelayedMessage(time, null, messages);
    }

    public BukkitTask sendDelayedMessage(OMRunnable.Time time, Sound sound, Message... messages) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                if (messages != null) {
                    for (Message message : messages) {
                        sendMessage(message);
                    }
                }

                if (sound != null) {
                    player.playSound(player.getLocation(), sound, 5, 1);
                }
            }
        }.runTaskLater(api, time.getTicks());
    }

    public void msgUnknownCommand(String cmd) {
        sendMessage(new Message("§7Die command bestaat niet! (§6" + cmd + "§7). Gebruik §6/help§7 voor hulp.", "§7Unknown command (§6" + cmd + "§7). Use §6/help§7 for help."));
    }

    public void msgRequiredVipRank(VipRank vipRank) {
        Player p = getPlayer();

        sendMessage(new Message("§7Je moet een " + vipRank.getRankString() + " VIP§7 zijn om dit te doen!", "§7You have to be " + (vipRank == VipRank.IRON || vipRank == VipRank.EMERALD ? "an" : "a") + " " + vipRank.getRankString() + " VIP§7 to do this!"));
        p.playSound(p.getLocation(), Sound.BLOCK_LAVA_POP, 5, 1);
    }

    public void clearInventory() {
        PlayerInventory inventory = player.getInventory();
        inventory.clear();
        inventory.setHelmet(null);
        inventory.setChestplate(null);
        inventory.setLeggings(null);
        inventory.setBoots(null);
    }

    public void updateInventory() {
        new BukkitRunnable() {
            public void run() {
                player.updateInventory();
            }
        }.runTaskLater(api, 1);
    }

    public void clearExperience() {
        player.setLevel(0);
        player.setExp(0f);
    }

    public void clearPotionEffects() {
        for (PotionEffect effect : new ArrayList<>(player.getActivePotionEffects())) {
            clearPotionEffect(effect);
        }
    }

    public void clearPotionEffect(PotionEffect effect) {
        player.removePotionEffect(effect.getType());
    }

    public void addPotionEffect(PotionBuilder builder) {
        player.addPotionEffect(builder.build());
    }

    public void hidden() {
        hidden(players);
    }

    public void hidden(OMPlayer... players) {
        hidden(Arrays.asList(players));
    }

    public void hidden(Collection<? extends OMPlayer> players) {
        for (OMPlayer player : players) {
            player.hidePlayers(this);
        }
    }

    public void shown() {
        shown(players);
    }

    public void shown(OMPlayer... players) {
        shown(Arrays.asList(players));
    }

    public void shown(Collection<? extends OMPlayer> players) {
        for (OMPlayer player : players) {
            player.showPlayers(this);
        }
    }

    public void hidePlayers() {
        hidePlayers(players);
    }

    public void hidePlayers(OMPlayer... players) {
        hidePlayers(Arrays.asList(players));
    }

    public void hidePlayers(Collection<? extends OMPlayer> players) {
        for (OMPlayer player : players) {
            Player p = player.getPlayer();
            this.player.hidePlayer(p);

            if (!hiddenPlayers.contains(p))
                hiddenPlayers.add(p);
        }
    }

    public void showPlayers() {
        showPlayers(players);
    }

    public void showPlayers(OMPlayer... players) {
        showPlayers(Arrays.asList(players));
    }

    public void showPlayers(Collection<? extends OMPlayer> players) {
        for (OMPlayer player : players) {
            Player p = player.getPlayer();
            this.player.showPlayer(p);

            if (hiddenPlayers.contains(p))
                hiddenPlayers.remove(p);
        }
    }

    public List<Player> getHiddenPlayers() {
        return hiddenPlayers;
    }

    public List<Player> getAllExceptHidden() {
        List<Player> players = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!hiddenPlayers.contains(player))
                players.add(player);
        }

        return players;
    }

    public void connect(Server server) {

    }

    public static OMPlayer getPlayer(Player player) {
        for (OMPlayer omp : players) {
            if (omp.getPlayer() == player)
                return omp;
        }
        return null;
    }

    public static List<OMPlayer> getPlayers() {
        return players;
    }

    public ChatColorData chatcolors() {
        return (ChatColorData) data(Data.CHATCOLORS);
    }

    public DisguiseData disguises() {
        return (DisguiseData) data(Data.DISGUISES);
    }

    public GadgetData gadgets() {
        return (GadgetData) data(Data.GADGETS);
    }

    public GeneralData general() {
        return (GeneralData) data(Data.GENERAL);
    }

    public HatData hats() {
        return (HatData) data(Data.HATS);
    }

    public PetData pets() {
        return (PetData) data(Data.PETS);
    }

    public TrailData trails() {
        return (TrailData) data(Data.TRAILS);
    }

    public WardrobeData wardrobe() {
        return (WardrobeData) data(Data.WARDROBE);
    }
}
