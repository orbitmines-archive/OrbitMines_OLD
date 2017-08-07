package com.orbitmines.bungee.runnable.runnables;

import com.orbitmines.api.Message;
import com.orbitmines.bungee.handlers.BungeePlayer;
import com.orbitmines.bungee.handlers.Login;
import com.orbitmines.bungee.runnable.BungeeRunnable;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 6-8-2017
*/
public class LoginRunnable extends BungeeRunnable {

    public LoginRunnable() {
        super(TimeUnit.SECOND, 1);
    }

    @Override
    public void run() {
        List<Login> logins = Login.getLogins();

        if (logins.size() == 0)
            return;

        for (Login login : new ArrayList<>(logins)) {
            login.tickLoginTimer();

            BungeePlayer omp = login.getPlayer();
            if (login.getLoginTime() != 0) {
                Title title = ProxyServer.getInstance().createTitle();
                title.fadeIn(0);
                title.stay(40);
                title.fadeOut(0);
                title.title(new TextComponent("§4§lSTAFF PROTECTION"));
                title.subTitle(new TextComponent("§7" + login.getLoginTime() + "..."));
                title.send(omp.getPlayer());
            } else {
                omp.getPlayer().disconnect(new TextComponent("§4§lSTAFF PROTECTION\n" + omp.getMessage(new Message("§cJe bent §4§lGEKICKT§c!", "§cYou've been §4§lKICKED§c!"))));
            }
        }
    }
}
