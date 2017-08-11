package com.orbitmines.kitpvp.handlers;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.chat.ComponentMessage;
import com.orbitmines.api.spigot.utils.LocationUtils;
import com.orbitmines.api.utils.RandomUtils;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
* OrbitMines - @author Fadi Shawki - 11-8-2017
*/
@Deprecated
public class KitPvPMap {

    private static List<KitPvPMap> maps = new ArrayList<>();

    private String mapName;
    private String builders;
    private Location spectatorSpawn;
    private List<Location> spawns;
    private List<UUID> votes;
    private Location voteSign;
    private int maxY;
    private int minutes;
    private int seconds;

    public KitPvPMap(String mapName) {
        maps.add(this);

        this.mapName = mapName;
        this.votes = new ArrayList<>();
    }

    public String getMapName() {
        return mapName;
    }

    public String getBuilders() {
        return builders;
    }

    public void setBuilders(String builders) {
        this.builders = builders;
    }

    public Location getSpectatorSpawn() {
        return spectatorSpawn;
    }

    public void setSpectatorSpawn(Location spectatorSpawn) {
        this.spectatorSpawn = spectatorSpawn;
    }

    public void setSpawns(List<Location> spawns) {
        this.spawns = spawns;
    }

    public List<UUID> getVotes() {
        return votes;
    }

    public Location getVoteSign() {
        return voteSign;
    }

    public void setVoteSign(Location voteSign) {
        this.voteSign = voteSign;
    }

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void resetTimer() {
        this.minutes = 30;
        this.seconds = 0;
    }

    public void tickTimer() {
        if (seconds != 0) {
            seconds = seconds - 1;
        } else {
            minutes = minutes - 1;
            seconds = 59;
        }
    }

    public boolean nextSwitch() {
        return this.minutes == 0 && this.seconds == 0;
    }

    public Location getRandomSpawn() {
        return this.spawns.get(RandomUtils.RANDOM.nextInt(this.spawns.size()));
    }

    public void sendJoinMessage(KitPvPPlayer omp) {
        Player p = omp.getPlayer();
        p.sendMessage("§7§m----------------------------------------");
        p.sendMessage(" §6§lOrbitMines§4§lNetwork §7- §c§lKitPvP");
        p.sendMessage(" ");
        p.sendMessage(" §7§lMap: §9§l§n" + getMapName());
        p.sendMessage(" ");

        ComponentMessage cm = new ComponentMessage();
        cm.addPart(" §7§l" + omp.getMessage(new Message("Gebouwd door", "Built by")) + ": ");
        cm.addPart("§e§l[" + omp.getMessage(new Message("Bekijk Builders", "View")) + "]", HoverEvent.Action.SHOW_TEXT, getBuilders());
        cm.send(p);

        p.sendMessage("§7§m----------------------------------------");
    }

    public void updateVoteSign(KitPvPPlayer omp) {
        String[] lines = new String[4];

        lines[0] = "§l" + mapName;
        lines[1] = votes.size() + " " + (votes.size() == 1 ? "Vote" : "Votes");
        lines[2] = "";
        if (omp.hasVoted())
            lines[3] = (votes.contains(omp.getUUID()) ? "§2" : "§4") + "§l" + omp.getMessage(new Message("Gevoten", "Voted"));
        else
            lines[3] = "§n" + omp.getMessage(new Message("Klik om te Voten", "Click to Vote"));

        omp.getPlayer().sendSignChange(getVoteSign(), lines);
    }

    public static KitPvPMap getKitPvPMap(Location signLocation) {
        for (KitPvPMap map : maps) {
            if (LocationUtils.equalsLoc(map.getVoteSign(), signLocation))
                return map;
        }
        return null;
    }

    public static List<KitPvPMap> getMaps() {
        return maps;
    }
}
