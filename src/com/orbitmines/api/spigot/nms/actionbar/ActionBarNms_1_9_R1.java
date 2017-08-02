package com.orbitmines.api.spigot.nms.actionbar;

import com.orbitmines.api.spigot.handlers.chat.ActionBar;
import net.minecraft.server.v1_9_R1.IChatBaseComponent;
import net.minecraft.server.v1_9_R1.PacketPlayOutChat;
import net.minecraft.server.v1_9_R1.PlayerConnection;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * Created by Fadi on 30-4-2016.
 */
public class ActionBarNms_1_9_R1 implements ActionBarNms {

    public void send(Player player, ActionBar actionBar) {
        IChatBaseComponent a = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + actionBar.getMessage() + "\"}");
        PacketPlayOutChat actionBarPacket = new PacketPlayOutChat(a, (byte) 2);

        PlayerConnection c = ((CraftPlayer) player).getHandle().playerConnection;
        c.sendPacket(actionBarPacket);
    }
}
