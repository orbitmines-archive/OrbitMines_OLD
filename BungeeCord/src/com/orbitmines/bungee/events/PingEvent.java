package com.orbitmines.bungee.events;

import com.orbitmines.api.utils.RandomUtils;
import com.orbitmines.bungee.OrbitMinesBungee;
import com.orbitmines.bungee.handlers.MotdHandler;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 6-8-2017
*/
public class PingEvent implements Listener {

    private OrbitMinesBungee bungee;
    private Favicon favicon;
    private MotdHandler motdHandler;

    public PingEvent() {
        bungee = OrbitMinesBungee.getBungee();
        try {
            favicon = Favicon.create(ImageIO.read(new File(bungee.getDataFolder().getPath() + "/server-icon.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        motdHandler = bungee.getMotdHandler();
    }

    @EventHandler
    public void onPing(ProxyPingEvent event) {
        ServerPing res = event.getResponse();
        ServerPing.Protocol protocolVersion = bungee.isInMaintenance() ? new ServerPing.Protocol("§d§lMaintenance Mode", 1) : res.getVersion();

        ServerPing.Players players = res.getPlayers();
        players.setMax(300);//TODO

        List<String> secondLines = motdHandler.getSecondLines();
        ServerPing BF = new ServerPing(protocolVersion, players, " " + motdHandler.getFirstLine() + "\n    " + motdHandler.getColor() + "» " + secondLines.get(RandomUtils.RANDOM.nextInt(secondLines.size())), favicon);
        event.setResponse(BF);
    }
}
