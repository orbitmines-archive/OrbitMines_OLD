package com.orbitmines.api.spigot.handlers;

import com.orbitmines.api.*;
import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.handlers.chat.Title;
import com.orbitmines.api.spigot.handlers.inventory.OMInventory;
import com.orbitmines.api.spigot.handlers.itembuilders.PotionBuilder;
import com.orbitmines.api.spigot.handlers.kit.KitInteractive;
import com.orbitmines.api.spigot.handlers.npc.FloatingItem;
import com.orbitmines.api.spigot.handlers.npc.Hologram;
import com.orbitmines.api.spigot.handlers.npc.NpcArmorStand;
import com.orbitmines.api.spigot.handlers.playerdata.*;
import com.orbitmines.api.spigot.handlers.scoreboard.Scoreboard;
import com.orbitmines.api.spigot.handlers.scoreboard.ScoreboardSet;
import com.orbitmines.api.spigot.runnable.OMRunnable;
import com.orbitmines.api.spigot.utils.BungeeMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public abstract class OMPlayer {

    protected static List<OMPlayer> players = new ArrayList<>();

    protected OrbitMinesApi api;

    protected final Player player;

    protected Scoreboard scoreboard;
    protected OMInventory lastInventory;
    protected KitInteractive lastInteractiveKit;

    protected List<PlayerData> data;
    private Map<Data, String> cachedData;

    private boolean opMode;

    /* Important Data, not stored in PlayerData */
    protected StaffRank staffRank;
    protected VipRank vipRank;
    protected Language language;
    protected boolean silent;
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
        this.cachedData = new HashMap<>();
        this.opMode = false;
        this.staffRank = StaffRank.NONE;
        this.vipRank = VipRank.NONE;
        this.language = Language.DUTCH;
        this.silent = false;
        this.votes = 0;
        this.receivedMonthlyBonus = false;

        this.cooldowns = new HashMap<>();
        this.hiddenPlayers = new ArrayList<>();
    }

    /* Called on player chat */
    public abstract String getPrefix();

    /* Called before a players votes, or when the players logs in and receives previous votes, this handles just the rewards */
    public abstract void onVote(int votes);

    /* Called after the player logs in */
    protected abstract void onLogin();

    /* Called before the player logs out */
    protected abstract void onLogout();

    /* Called when a player receives knock back, example: gadgets */
    public abstract boolean canReceiveVelocity();

    public void login() {
        player.setOp(false);
        player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);

        if (!Database.get().contains(Database.Table.PLAYERS, Database.Column.UUID, new Database.Where(Database.Column.UUID, getUUID().toString())))
            Database.get().insert(Database.Table.PLAYERS, Database.get().values(getUUID().toString(), player.getName(), StaffRank.NONE.toString(), VipRank.NONE.toString(), Language.DUTCH.toString(), "" + false, "" + false, "" + 0, "null", "" + 0, "null"));

        Map<Database.Column, String> values = Database.get().getValues(Database.Table.PLAYERS, new Database.Where(Database.Column.UUID, getUUID().toString()),
                Database.Column.STAFFRANK, Database.Column.VIPRANK, Database.Column.LANGUAGE, Database.Column.SILENT, Database.Column.MONTHLYBONUS, Database.Column.STATS);

        staffRank = StaffRank.valueOf(values.get(Database.Column.STAFFRANK));
        vipRank = VipRank.valueOf(values.get(Database.Column.VIPRANK));
        language = Language.valueOf(values.get(Database.Column.LANGUAGE));
        silent = Boolean.parseBoolean(values.get(Database.Column.SILENT));
        receivedMonthlyBonus = Boolean.parseBoolean(values.get(Database.Column.MONTHLYBONUS));

        /* DATA~STATS~DATA~STATS */
        String[] stats = values.get(Database.Column.STATS).split("~");

        if (!stats[0].equals("null")) {
            for (int i = 0; i < stats.length; i += 2) {
                cachedData.put(Data.valueOf(stats[i]), stats[i + 1]);
            }
        } else {
            /* New Player (Should only happen in the Hub) */
            new BukkitRunnable() {
                @Override
                public void run() {
                    broadcastMessage(new Message("§d" + player.getName() + " §7heeft §6§lOrbitMines§4§lNetwork§7 gejoind!", "§d" + player.getName() + " §7joined §6§lOrbitMines§4§lNetwork§7!"));
                }
            }.runTaskLater(api, 1);
        }

        for (Data data : api.getDataToRead()) {
            PlayerData playerData = data.getData(this);

            if (cachedData.containsKey(data)) {
                playerData.parse(cachedData.get(data));
                cachedData.remove(data);
            }
        }

        defaultTabList();

        for (PlayerData playerData : data) {
            playerData.onLogin();
        }

        updateVotes();

        onLogin();
    }

    public void logout() {
        onLogout();

        player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);

        updateStats();

        players.remove(this);
    }

    public void vote(int votes) {
        onVote(votes);

        Player p = getPlayer();
        p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 5, 1);
        p.sendMessage("");
        sendMessage(new Message("§7Dank je, §b§l" + player.getName() + " §7voor je §b§lVote§7!", "§7Thank you, §b§l" + player.getName() + " §7for your §b§lVote§7!"));
        sendMessage(new Message("§7Beloningen voor de " + api.server().getServerType().getName() + "§7 Server:", "§7Reward on the " + api.server().getServerType().getName() + "§7 Server:"));
        p.sendMessage("");

        String amount = votes == 1 ? "" : "§b" + votes + "x§7 ";
        for (String message : api.server().getVoteMessages(this)) {
            p.sendMessage("§7  - " + amount + message);
        }
        p.sendMessage("");
        sendMessage(new Message("§7Jouw votes deze maand: §b§l" + votes + "§7.", "§7Your total Votes this Month: §b§l" + votes + "§7."));

        broadcastMessage(new Message("§b§l" + player.getName() + "§7 heeft gevoten met §b§l/vote§7.", "§b§l" + player.getName() + "§7 has voted with §b§l/vote§7."));
    }

    public void defaultTabList() {
        setTabList("§6§lOrbitMines§4§lNetwork", "§7Website: §6www.orbitmines.com §8| §7Twitter: §b@OrbitMines §8| §7" + getMessage(new Message("Winkel", "Shop")) + ": §3shop.orbitmines.com");
    }

    public void updateStats() {
        StringBuilder stringBuilder = new StringBuilder();

        for (PlayerData playerData : data) {
            stringBuilder.append(playerData.getType().toString());
            stringBuilder.append("~");
            stringBuilder.append(playerData.serialize());
            stringBuilder.append("~");
        }

        for (Data data : cachedData.keySet()) {
            stringBuilder.append(data.toString());
            stringBuilder.append("~");
            stringBuilder.append(cachedData.get(data));
            stringBuilder.append("~");
        }

        Database.get().update(Database.Table.PLAYERS, new Database.Where(Database.Column.UUID, getUUID().toString()), new Database.Set(Database.Column.STATS, stringBuilder.toString().substring(0, stringBuilder.length() - 1)));
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
        if (set != null) {
            set.updateTitle();
            set.updateScores();
            set.updateTeams();
        }

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

    public KitInteractive getLastInteractiveKit() {
        return lastInteractiveKit;
    }

    public void setLastInteractiveKit(KitInteractive lastInteractiveKit) {
        this.lastInteractiveKit = lastInteractiveKit;
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

        Database.get().update(Database.Table.PLAYERS, new Database.Where(Database.Column.UUID, getUUID().toString()), new Database.Set(Database.Column.STAFFRANK, staffRank.toString()));
    }

    public VipRank getVipRank() {
        return vipRank;
    }

    public void setVipRank(VipRank vipRank) {
        this.vipRank = vipRank;

        Title t = new Title("", getMessage(new Message("§7Je bent nu een " + vipRank.getRankString() + "§7!", "§7You are now " + vipRank.getRankString() + " " + (vipRank == VipRank.EMERALD || vipRank == VipRank.IRON ? "an" : "a") + "§7!")), 20, 80, 20);
        t.send(player);

        Database.get().update(Database.Table.PLAYERS, new Database.Where(Database.Column.UUID, getUUID().toString()), new Database.Set(Database.Column.VIPRANK, vipRank.toString()));
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;

        Database.get().update(Database.Table.PLAYERS, new Database.Where(Database.Column.UUID, getUUID().toString()), new Database.Set(Database.Column.LANGUAGE, language.toString()));

        /* Also update on Bungeecord */
        BungeeMessage.send(PluginMessageType.SET_LANGUAGE, player, getUUID().toString(), language.toString());

        defaultTabList();
    }

    public boolean isSilent() {
        return silent;
    }

    /* Updated from bungeecord, so we don't update it here */
    public void setSilent(boolean silent) {
        this.silent = silent;
    }

    public int getVotes() {
        return votes;
    }

    public void addVote() {
        this.votes += 1;

        Database.get().update(Database.Table.PLAYERS, new Database.Where(Database.Column.UUID, getUUID().toString()), new Database.Set(Database.Column.VOTES, votes + ""));
    }

    public boolean hasReceivedMonthlyBonus() {
        return receivedMonthlyBonus;
    }

    public void setReceivedMonthlyBonus(boolean receivedMonthlyBonus) {
        this.receivedMonthlyBonus = receivedMonthlyBonus;

        Database.get().update(Database.Table.PLAYERS, new Database.Where(Database.Column.UUID, getUUID().toString()), new Database.Set(Database.Column.MONTHLYBONUS, receivedMonthlyBonus + ""));
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
            if (afk.equals("null")) {
                broadcastMessage(new Message("§7 " + playerName + "§7 is nu §6AFK§7.", "§7 " + playerName + "§7 is now §6AFK§7."));
            } else {
                broadcastMessage(new Message("§7 " + playerName + "§7 is nu §6AFK§7. (§7" + afk + "§7)", "§7 " + playerName + "§7 is now §6AFK§7. (§7" + afk + "§7)"));
            }
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
        Map<Database.Column, String> values = Database.get().getValues(Database.Table.PLAYERS, new Database.Where(Database.Column.UUID, getUUID().toString()),
                Database.Column.VOTES, Database.Column.CACHEDVOTES);

        this.votes = Integer.parseInt(values.get(Database.Column.VOTES));

        /* SERVER~10|SERVER~10 */
        String cachedVotesString = values.get(Database.Column.CACHEDVOTES);
        if (!cachedVotesString.equals("null")) {
            Map<Server, Integer> cachedVotes = new HashMap<>();

            for (String voteData : cachedVotesString.split("\\|")) {
                String[] data = voteData.split("~");

                cachedVotes.put(Server.valueOf(data[0]), Integer.parseInt(data[1]));
            }

            Server server = api.server().getServerType();
            if (cachedVotes.containsKey(server)) {
                int votes = cachedVotes.get(server);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        vote(votes);
                    }
                }.runTaskLater(api, 20);

                cachedVotes.remove(server);
            }

            updateCachedVotes(cachedVotes);
        }
    }

    private void updateCachedVotes(Map<Server, Integer> cachedVotes) {
        String value;

        if (cachedVotes != null && cachedVotes.size() != 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Server server : cachedVotes.keySet()) {
                stringBuilder.append(server.toString());
                stringBuilder.append("~");
                stringBuilder.append(cachedVotes.get(server));
                stringBuilder.append("|");
            }

            value = stringBuilder.toString().substring(0, stringBuilder.length() - 1);
        } else {
            value = "null";
        }

        Database.get().update(Database.Table.PLAYERS, new Database.Where(Database.Column.UUID, getUUID().toString()), new Database.Set(Database.Column.CACHEDVOTES, value));
    }

    public boolean isEligible(StaffRank staffRank) {
        return staffRank == null || this.staffRank.ordinal() >= staffRank.ordinal();
    }

    public boolean isEligible(VipRank vipRank) {
        return vipRank == null || isEligible(StaffRank.OWNER) || this.vipRank.ordinal() >= vipRank.ordinal();
    }

    public void sendMessage(Message message) {
        player.sendMessage(message.lang(language));
    }

    public void broadcastMessage(Message message) {
        for (OMPlayer player : players) {
            player.sendMessage(message);
        }
    }

    public String getMessage(Message message) {
        return message.lang(language);
    }

    public String statusString(boolean bl) {
        return bl ? getMessage(new Message("§a§lAAN", "§a§lENABLED")) : getMessage(new Message("§c§lUIT", "§c§lDISABLED"));
    }

    public String statusString(Language language, boolean bl) {
        return bl ? new Message("§a§lAAN", "§a§lENABLED").lang(language) : new Message("§c§lUIT", "§c§lDISABLED").lang(language);
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

    protected void broadcastQuitMessage() {
        if (silent) {
            Message message = new Message(" §c« " + getName() + "§c is weggegaan. §6[Silent Mode]", " §c« " + getName() + "§c left. §6[Silent Mode]");
            for (OMPlayer omp : players) {
                if (!omp.isEligible(StaffRank.MODERATOR))
                    continue;

                omp.sendMessage(message);
            }
        } else {
            broadcastMessage(new Message(" §c« " + getName() + "§c is weggegaan.", " §c« " + getName() + "§c left."));
        }
    }

    protected void broadcastJoinMessage() {
        if (silent) {
            Message message = new Message(" §a» " + getName() + "§a is gejoind. §6[Silent Mode]", " §a» " + getName() + "§a joined. §6[Silent Mode]");
            for (OMPlayer omp : players) {
                if (!omp.isEligible(StaffRank.MODERATOR))
                    continue;

                omp.sendMessage(message);
            }
        } else {
            broadcastMessage(new Message(" §a» " + getName() + "§a is gejoind.", " §a» " + getName() + "§a joined."));
        }
    }

    protected void msgJoinTitle() {
        Title t = new Title("§6§lOrbitMines§4§lNetwork", api.server().getServerType().getName(), 20, 40, 20);
        t.send(getPlayer());
    }

    protected void msgJoin() {
        new BukkitRunnable() {
            public void run() {
                Player p = getPlayer();
                p.sendMessage("§7§m----------------------------------------");
                p.sendMessage(" §6§lOrbitMines§4§lNetwork §7- " + api.server().getServerType().getName());
                p.sendMessage(" ");
                p.sendMessage(" §7§lWebsite: §6www.orbitmines.com");
                p.sendMessage(" §7§l" + getMessage(new Message("Winkel", "Shop")) + ": §3shop.orbitmines.com");
                p.sendMessage(" §7§l" + getMessage(new Message("Voten", "Vote")) + ": §b/vote");
                p.sendMessage(" ");

                //TODO
//                ComponentMessage cm = new ComponentMessage();
//                cm.addPart(" §7§l" + Messages.WORD_SPAWN_BUILT_BY.get(omPlayer) +  ": ");
//                cm.addPart("§e§l[" + Messages.WORD_VIEW.get(omPlayer) + "]", HoverEvent.Action.SHOW_TEXT, api.getServerPlugin() == null ? "" : api.getServerPlugin().getSpawnBuilders());
//                cm.send(p);

                p.sendMessage(" §7§l" + getMessage(new Message("Gemaakt door", "Developed by")) + ": " + api.server().getDevelopedBy());
                p.sendMessage("§7§m----------------------------------------");
            }
        }.runTaskLater(api, 20);
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

    public void destroyHiddenNpcs() {
        /* Destroy hidden items */
        for (FloatingItem floatingItem : FloatingItem.getFloatingItems()) {
            for (FloatingItem.ItemInstance itemInstance : floatingItem.getItemInstances()) {
                if (itemInstance.hideOnJoin() && itemInstance.getItem().getWorld().getName().equals(player.getWorld().getName()) && !itemInstance.getWatchers().contains(player))
                    itemInstance.hideFor(player);
            }
        }
            /* Destroy hidden ArmorStands */
        for (NpcArmorStand npcArmorStand : NpcArmorStand.getNpcArmorStands()) {
            if (npcArmorStand.hideOnJoin() && npcArmorStand.getArmorStand().getWorld().getName().equals(player.getWorld().getName()) && !npcArmorStand.getWatchers().contains(player))
                npcArmorStand.hideFor(player);
        }
            /* Destroy hidden Hologram */
        for (Hologram hologram : Hologram.getHolograms()) {
            if (hologram.hideOnJoin() && hologram.getLocation().getWorld().getName().equals(player.getWorld().getName()) && !hologram.getWatchers().contains(player))
                hologram.hideFor(player);
        }
    }

    public void connect(Server server) {
        switch (server.getStatus()) {

            case ONLINE:
                sendMessage(new Message("§7Verbinden met " + server.getColor() + server.getName() + "§7...", "§7Connecting to " + server.getColor() + server.getName() + "§7..."));
                forceConnect(server);
                break;
            case OFFLINE:
                sendMessage(new Message("§7De " + server.getColor() + server.getName() + "§7 Server is §c§lOffline§7!", "§7The " + server.getColor() + server.getName() + "§7 Server is §4§lOffline§7!"));
                break;
            case MAINTENANCE:
                if (isEligible(StaffRank.OWNER)) {
                    sendMessage(new Message(server.getColor() + server.getName() + " is in §d§lMaintenance Mode§7! Verbinden forceren...", server.getColor() + server.getName() + " is in §d§lMaintenance Mode§7! Forcing connection..."));
                    forceConnect(server);
                } else {
                    sendMessage(new Message(server.getColor() + server.getName() + " is in §d§lMaintenance Mode§7!", server.getColor() + server.getName() + " is in §d§lMaintenance Mode§7!"));
                }
                break;
        }
    }

    private void forceConnect(Server server) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("Connect");
            out.writeUTF(server.toString().toLowerCase());
        } catch (IOException e) {
        }

        getPlayer().sendPluginMessage(api, "BungeeCord", b.toByteArray());
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
