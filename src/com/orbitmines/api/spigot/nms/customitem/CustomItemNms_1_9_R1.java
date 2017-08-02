package com.orbitmines.api.spigot.nms.customitem;

import com.orbitmines.api.spigot.Mob;
import net.minecraft.server.v1_9_R1.NBTTagByte;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Fadi on 30-4-2016.
 */
public class CustomItemNms_1_9_R1 implements CustomItemNms {

    @Override
    public ItemStack hideFlags(ItemStack item, int... hideFlags) {
        if (item == null)
            return null;

        net.minecraft.server.v1_9_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

        NBTTagCompound tag = new NBTTagCompound();

        if (nmsStack.hasTag()) tag = nmsStack.getTag();

        int hide = 0;
        for (int i : hideFlags) {
            hide = hide + i;
        }

        if (hide != 0) {
            tag.set("HideFlags", new NBTTagByte((byte) hide));
            nmsStack.setTag(tag);
        }

        return CraftItemStack.asCraftMirror(nmsStack);
    }

    @Override
    public ItemStack setUnbreakable(ItemStack item) {
        if (item == null)
            return null;

        net.minecraft.server.v1_9_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

        NBTTagCompound tag = new NBTTagCompound();
        if (nmsStack.hasTag()) tag = nmsStack.getTag();
        tag.set("Unbreakable", new NBTTagByte((byte) 1));
        nmsStack.setTag(tag);

        return CraftItemStack.asCraftMirror(nmsStack);
    }

    @Override
    public ItemStack setEggId(ItemStack item, Mob mob) {
        if (item == null)
            return null;

        net.minecraft.server.v1_9_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound();
        NBTTagCompound eggId = new NBTTagCompound();
        eggId.setString("id", mob.getRawName());
        tag.set("EntityTag", eggId);
        nmsStack.setTag(tag);

        return CraftItemStack.asCraftMirror(nmsStack);
    }
}
