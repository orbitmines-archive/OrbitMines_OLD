package com.orbitmines.api.spigot.nms.entity;

import com.orbitmines.api.spigot.OrbitMinesApi;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Fadi on 11-5-2016.
 */
public class EntityNms_1_12_R1 implements EntityNms {

    @Override
    public void setInvisible(Player player, boolean bl) {
        ((CraftPlayer) player).getHandle().setInvisible(true);
    }

    @Override
    public void destoryEntity(Player player, Entity entity) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(((CraftEntity) entity).getHandle().getId()));
    }

    @Override
    public void setSpeed(Entity entity, double d) {
        AttributeInstance currentSpeed = ((CraftLivingEntity) entity).getHandle().getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
        currentSpeed.setValue(d);
    }

    @Override
    public double getSpeed(Entity entity) {
        return ((CraftLivingEntity) entity).getHandle().getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).b();
    }

    @Override
    public void disguiseAsBlock(Player player, int Id, Player... players) {
        EntityFallingBlock disguise = new EntityFallingBlock(((CraftPlayer) player).getHandle().world, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), new BlockLog1().getBlockData());
        disguise.locX = player.getLocation().getX();
        disguise.locY = player.getLocation().getY();
        disguise.locZ = player.getLocation().getZ();
        disguise.yaw = player.getLocation().getYaw();
        disguise.pitch = player.getLocation().getPitch();
        disguise.h(((CraftPlayer) player).getHandle().getId());

        for (Player p : players) {
            if (player == p)
                continue;

            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy((((CraftPlayer) player).getHandle().getId())));
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutSpawnEntity(disguise, 70, Id));
        }
    }

    @Override
    public Entity disguiseAsMob(Player player, EntityType type, boolean baby, Player... players) {
        return disguiseAsMob(player, type, baby, null, players);
    }

    @Override
    public Entity disguiseAsMob(Player player, EntityType type, boolean baby, String displayName, Player... players) {
        EntityLiving disguise = null;
        World world = ((CraftPlayer) player).getHandle().world;

        switch (type) {
            case BAT:
                disguise = new EntityBat(world);
                break;
            case BLAZE:
                disguise = new EntityBlaze(world);
                break;
            case ARMOR_STAND:
                disguise = new EntityArmorStand(world);
                break;
            case ARROW:
                break;
            case BOAT:
                break;
            case CAVE_SPIDER:
                disguise = new EntityCaveSpider(world);
                break;
            case CHICKEN:
                disguise = new EntityChicken(world);
                break;
            case COMPLEX_PART:
                break;
            case COW:
                disguise = new EntityCow(world);
                break;
            case CREEPER:
                disguise = new EntityCreeper(world);
                break;
            case DROPPED_ITEM:
                break;
            case EGG:
                break;
            case ENDERMAN:
                disguise = new EntityEnderman(world);
                break;
            case ENDERMITE:
                disguise = new EntityEndermite(world);
                break;
            case ENDER_CRYSTAL:
                break;
            case ENDER_DRAGON:
                disguise = new EntityEnderDragon(world);
                break;
            case ENDER_PEARL:
                break;
            case ENDER_SIGNAL:
                break;
            case EXPERIENCE_ORB:
                break;
            case FALLING_BLOCK:
                break;
            case FIREBALL:
                break;
            case FIREWORK:
                break;
            case FISHING_HOOK:
                break;
            case GHAST:
                disguise = new EntityGhast(world);
                break;
            case GIANT:
                disguise = new EntityGiantZombie(world);
                break;
            case GUARDIAN:
                disguise = new EntityGuardian(world);
                break;
            case HORSE:
                disguise = new EntityHorse(world);
                break;
            case IRON_GOLEM:
                disguise = new EntityIronGolem(world);
                break;
            case ITEM_FRAME:
                break;
            case LEASH_HITCH:
                break;
            case LIGHTNING:
                break;
            case MAGMA_CUBE:
                disguise = new EntityMagmaCube(world);
                break;
            case MINECART:
                break;
            case MINECART_CHEST:
                break;
            case MINECART_COMMAND:
                break;
            case MINECART_FURNACE:
                break;
            case MINECART_HOPPER:
                break;
            case MINECART_MOB_SPAWNER:
                break;
            case MINECART_TNT:
                break;
            case MUSHROOM_COW:
                disguise = new EntityMushroomCow(world);
                break;
            case OCELOT:
                disguise = new EntityOcelot(world);
                break;
            case PAINTING:
                break;
            case PIG:
                disguise = new EntityPig(world);
                break;
            case PIG_ZOMBIE:
                disguise = new EntityPigZombie(world);
                break;
            case PLAYER:
                break;
            case PRIMED_TNT:
                break;
            case RABBIT:
                disguise = new EntityRabbit(world);
                break;
            case SHEEP:
                disguise = new EntitySheep(world);
                break;
            case SILVERFISH:
                disguise = new EntitySilverfish(world);
                break;
            case SKELETON:
                disguise = new EntitySkeleton(world);
                break;
            case SLIME:
                disguise = new EntitySlime(world);
                break;
            case SMALL_FIREBALL:
                break;
            case SNOWBALL:
                break;
            case SNOWMAN:
                disguise = new EntitySnowman(world);
                break;
            case SPIDER:
                disguise = new EntitySpider(world);
                break;
            case SPLASH_POTION:
                break;
            case SQUID:
                disguise = new EntitySquid(world);
                break;
            case THROWN_EXP_BOTTLE:
                break;
            case UNKNOWN:
                break;
            case VILLAGER:
                disguise = new EntityVillager(world);
                break;
            case WEATHER:
                break;
            case WITCH:
                disguise = new EntityWitch(world);
                break;
            case WITHER:
                disguise = new EntityWither(world);
                break;
            case WITHER_SKULL:
                break;
            case WOLF:
                disguise = new EntityWolf(world);
                break;
            case ZOMBIE:
                disguise = new EntityZombie(world);
                break;
            case POLAR_BEAR:
                disguise = new EntityPolarBear(world);
                break;
            case SHULKER:
            default:
                break;
        }

        if (disguise == null)
            return null;

        disguise.locX = player.getLocation().getX();
        disguise.locY = player.getLocation().getY();
        disguise.locZ = player.getLocation().getZ();
        disguise.yaw = player.getLocation().getYaw();
        disguise.pitch = player.getLocation().getPitch();
        disguise.h(((CraftPlayer) player).getHandle().getId());

        if (baby && disguise.getBukkitEntity() instanceof Ageable)
            ((Ageable) disguise.getBukkitEntity()).setBaby();

        for (Player p : players) {
            if (player == p)
                continue;

            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy((((CraftPlayer) player).getHandle().getId())));
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving(disguise));
        }

        if (displayName != null) {
            disguise.setCustomName(displayName);
            disguise.setCustomNameVisible(true);
        }

        return disguise.getBukkitEntity();
    }

    @Override
    public void mountUpdate(final Entity vehicle) {
        new BukkitRunnable() {
            @Override
            public void run() {
                PacketPlayOutMount packet = new PacketPlayOutMount(((CraftPlayer) vehicle).getHandle());
                ((CraftPlayer) vehicle).getHandle().playerConnection.sendPacket(packet);
            }
        }.runTaskLater(OrbitMinesApi.getApi(), 1);
    }

    @Override
    public void navigate(LivingEntity le, Location location, double v) {
        ((EntityInsentient) ((CraftLivingEntity) le).getHandle()).getNavigation().a(location.getX(), location.getY(), location.getZ(), v);
    }
}
