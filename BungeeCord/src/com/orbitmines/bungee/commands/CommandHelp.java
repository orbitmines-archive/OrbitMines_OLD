package com.orbitmines.bungee.commands;

import com.orbitmines.api.Server;
import com.orbitmines.bungee.handlers.BungeePlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
@Deprecated
public class CommandHelp extends Command {

    private final String[] alias = {"/help","/?"};

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void dispatch(BungeePlayer omp, String[] a) {
        //TODO THROUGH MYSQL
        ProxiedPlayer p = omp.getPlayer();
        Server server = omp.getServer();

        p.sendMessage("§7§m----------------------------------------");
        p.sendMessage(" §6§lOrbitMines§4§lNetwork §7- §e§lPlayer Commands");
        p.sendMessage(" ");
        switch(omp.getLanguage()){
            case DUTCH:
                p.sendMessage(" §7Gebruik je§9 Cosmetic Perks§7 met §9/perks§7.");
                p.sendMessage(" §cReport§7 een speler met §c/report§7.");
                p.sendMessage(" §bVote§7 voor beloningen met §b/vote§7.");
                p.sendMessage(" §7Bekijk je§2 vriendenlijst§7 met §2/friends§7.");
                p.sendMessage(" §7Voeg§2 vrienden§7 toe met §2/friend <player>§7.");
                p.sendMessage(" §3Doneer§7 voor VIP met §3/donate§7.");
                p.sendMessage(" §7Stuur§9 privé berichten§7 met §9/msg§7.");
                p.sendMessage(" §7Bekijk je§e OrbitMines Tokens§7 met §e/omt§7.");
                p.sendMessage(" §7Bekijk je§b VIP Points§7 met §b/vippoints§7.");
                p.sendMessage(" §7Open de§3 Server Selector§7 met §3/servers§7.");
                p.sendMessage(" §7Bekijk alle §aonline spelers§7 met §a/list§7.");
                p.sendMessage(" §7Bekijk onze §6website§7 met §6/website§7.");

                if(server == Server.HUB || server == Server.MINIGAMES || server == Server.FOG)
                    break;

                p.sendMessage(" " + server.getName() + " Commands");

                switch(server){
                    case KITPVP:
                        p.sendMessage(" §7Open de §bKit Selector§7 met §b/kit§7.");
                        break;
                    case PRISON:
                        p.sendMessage(" §7Bekijk je §6Gold§7 met §6/gold§7.");
                        p.sendMessage(" §7Geef iemand gold met §6/pay§7.");
                        p.sendMessage(" §7Teleporteer naar de mines met §9/mines§7.");
                        p.sendMessage(" §7Ga naar je cell met §c/cell§7.");
                        p.sendMessage(" §7Selecteer een kit met §9/kit§7.");
                        p.sendMessage(" §7Rankup met §c/rankup§7.");
                        p.sendMessage(" §7Bekijk de rijkste spelers met §6/topgold§7.");
                        p.sendMessage(" §7Teleporteer naar §aspawn§7 met §c/spawn§7.");
                        break;
                    case CREATIVE:
                        p.sendMessage(" §7Bekijk §dPlot Help§7 met §d/plot§7.");
                        break;
                    case SURVIVAL:
                        p.sendMessage(" §7Ze je §ahome§7 neer met §a/sethome <name>§7.");
                        p.sendMessage(" §7Teleporteer naar je §ahome§7 met §a/home <name>§7.");
                        p.sendMessage(" §7Bekijk je §ahomes§7 met §a/homes§7.");
                        p.sendMessage(" §7Verwijder je §ahome§7 met §a/delhome <name>§7.");
                        p.sendMessage(" §7Open de Region Selector met §a/regions§7.");
                        p.sendMessage(" §7Open de Warp Teleporter met §a/warps§7.");
                        p.sendMessage(" §7Bekijk de rijkste spelers met §a/topmoney§7.");
                        p.sendMessage(" §7Teleporteer naar §aspawn§7 met §a/spawn§7.");
                        break;
                    case SKYBLOCK:
                        p.sendMessage(" §7Bekijk de §dIsland Commands§7 met §d/is§7 of §d/island§7.");
                        p.sendMessage(" §7Selecteer een kit met §d/kit§7.");
                        p.sendMessage(" §7Teleporteer naar §aspawn§7 met §d/spawn§7.");
                        break;
                }
                break;
            case ENGLISH:
                p.sendMessage(" §7Use your§9 Cosmetic Perks§7 with §9/perks§7.");
                p.sendMessage(" §cReport§7 a Player with §c/report§7.");
                p.sendMessage(" §bVote§7 for Rewards with §b/vote§7.");
                p.sendMessage(" §7Show your§2 Friends§7 with §2/friends§7.");
                p.sendMessage(" §7Add§2 Friends§7 with §2/friend <player>§7.");
                p.sendMessage(" §3Donate§7 for VIP with §3/donate§7.");
                p.sendMessage(" §7Send§9 Private Messages§7 with §9/msg§7.");
                p.sendMessage(" §7Show your§e OrbitMines Tokens§7 with §e/omt§7.");
                p.sendMessage(" §7Show your§b VIP Points§7 with §b/vippoints§7.");
                p.sendMessage(" §7Open the§3 Server Selector§7 with §3/servers§7.");
                p.sendMessage(" §7View all §aOnline Players§7 with §a/list§7.");
                p.sendMessage(" §7View our §6website§7 with §6/website§7.");

                if(server == Server.HUB || server == Server.MINIGAMES || server == Server.FOG)
                    break;

                p.sendMessage(" " + server.getName() + " Commands");

                switch(server){
                    case KITPVP:
                        p.sendMessage(" §7Open the §bKit Selector§7 with §b/kit§7.");
                        break;
                    case PRISON:
                        p.sendMessage(" §7View your §6Gold§7 with §6/gold§7.");
                        p.sendMessage(" §7Pay someone with §6/pay§7.");
                        p.sendMessage(" §7Teleport to mines with §9/mines§7.");
                        p.sendMessage(" §7Go to your cell with §c/cell§7.");
                        p.sendMessage(" §7Select a kit with §9/kit§7.");
                        p.sendMessage(" §7Rankup with §c/rankup§7.");
                        p.sendMessage(" §7View the richest players with §6/topgold§7.");
                        p.sendMessage(" §7Teleport to §aspawn§7 with §c/spawn§7.");
                        break;
                    case CREATIVE:
                        p.sendMessage(" §7Show §dPlot Help§7 with §d/plot§7.");
                        break;
                    case SURVIVAL:
                        p.sendMessage(" §7Set your §ahome§7 with §a/sethome <name>§7.");
                        p.sendMessage(" §7Teleport to your §ahome§7 with §a/home <name>§7.");
                        p.sendMessage(" §7View your §ahomes§7 with §a/homes§7.");
                        p.sendMessage(" §7Delete your §ahome§7 with §a/delhome <name>§7.");
                        p.sendMessage(" §7Open the Region Selector with §a/regions§7.");
                        p.sendMessage(" §7Open the Warp Teleporter with §a/warps§7.");
                        p.sendMessage(" §7View the richest players with §a/topmoney§7.");
                        p.sendMessage(" §7Teleport to §aspawn§7 with §a/spawn§7.");
                        break;
                    case SKYBLOCK:
                        p.sendMessage(" §7Show §dIsland Commands§7 with §d/is§7 or §d/island§7.");
                        p.sendMessage(" §7Select a kit with §d/kit§7.");
                        p.sendMessage(" §7Teleport to §aspawn§7 with §d/spawn§7.");
                        break;
                }
                break;
        }
        p.sendMessage(" ");
        p.sendMessage("§7§m----------------------------------------");
    }
}
