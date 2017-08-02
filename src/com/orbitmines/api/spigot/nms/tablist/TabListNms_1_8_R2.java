package com.orbitmines.api.spigot.nms.tablist;

import net.minecraft.server.v1_8_R2.IChatBaseComponent;
import net.minecraft.server.v1_8_R2.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

/**
 * Created by Fadi on 30-4-2016.
 */
public class TabListNms_1_8_R2 implements TabListNms {

    public void send(Player player, String header, String footer) {
        IChatBaseComponent tab1 = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + header + "\"}");
        IChatBaseComponent tab2 = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + footer + "\"}");
        PacketPlayOutPlayerListHeaderFooter pack = new PacketPlayOutPlayerListHeaderFooter(tab1);

        try {
            Field field = pack.getClass().getDeclaredField("b");
            field.setAccessible(true);
            field.set(pack, tab2);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(pack);
    }
}
