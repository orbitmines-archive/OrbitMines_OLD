package com.orbitmines.bungee.commands;

import com.orbitmines.bungee.handlers.BungeePlayer;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public abstract class Command {

    private static List<Command> commands = new ArrayList<>();

    public Command() {
        commands.add(this);
    }

    public abstract String[] getAlias();

    /* a[0] = '/<command>' */
    public abstract void dispatch(BungeePlayer omp, String[] a);

    public void unregister() {
        commands.remove(this);
    }

    public static Command getCommand(String cmd) {
        for(Command command : commands){
            for(String alias : command.getAlias()){
                if(cmd.equalsIgnoreCase(alias))
                    return command;
            }
        }

        return null;
    }

    public static List<Command> getCommands() {
        return commands;
    }
}
