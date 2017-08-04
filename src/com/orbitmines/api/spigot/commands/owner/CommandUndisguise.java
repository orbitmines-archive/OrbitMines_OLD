package com.orbitmines.api.spigot.commands.owner;

import com.orbitmines.api.Message;
import com.orbitmines.api.StaffRank;
import com.orbitmines.api.spigot.commands.StaffCommand;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.playerdata.DisguiseData;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandUndisguise extends StaffCommand {

    private final String[] alias = {"/undisguise", "/undis", "/und"};

    public CommandUndisguise() {
        super(StaffRank.OWNER);
    }

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void onDispatch(OMPlayer omp, String[] a) {
        DisguiseData data = omp.disguises();

        if (data.isDisguised()) {
            data.undisguise();
            omp.sendMessage(new Message("§7Je bent niet meer vermomd.", "§7You are no longer §6disguised§7."));
        } else {
            omp.sendMessage(new Message("§7Je bent niet vermomd.", "§7You aren't §6disguised§7!"));
        }
    }
}
