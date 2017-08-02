package com.orbitmines.api.spigot;

import org.bukkit.entity.EntityType;

/*
* OrbitMines - @author Fadi Shawki - 7/28/2017
*/
public enum Mob {

    /* Mob-ish */
    ARMOR_STAND(EntityType.ARMOR_STAND, "ArmorStand", "ArmorStand"),

    BAT(EntityType.BAT, "Bat", "Bat"),
    BLAZE(EntityType.BLAZE, "Blaze", "Blaze"),
    CAVE_SPIDER(EntityType.CAVE_SPIDER, "Cave Spider", "CaveSpider"),
    CHICKEN(EntityType.CHICKEN, "Chicken", "Chicken"),
    COW(EntityType.COW, "Cow", "Cow"),
    CREEPER(EntityType.CREEPER, "Creeper", "Creeper"),
    DONKEY(EntityType.DONKEY, "Donkey", "Donkey"),
    ENDER_DRAGON(EntityType.ENDER_DRAGON, "Ender Dragon", "EnderDragon"),
    ENDERMAN(EntityType.ENDERMAN, "Enderman", "Enderman"),
    ENDERMITE(EntityType.ENDERMITE, "Endermite", "Endermite"),
    EVOKER(EntityType.EVOKER, "Evoker", "Evocation_Illager"),
    GHAST(EntityType.GHAST, "Ghast", "Ghast"),
    GIANT(EntityType.GIANT, "Giant", "Giant"),
    GUARDIAN(EntityType.GUARDIAN, "Guardian", "Guardian"),
    HORSE(EntityType.HORSE, "Horse", "EntityHorse"),
    HUSK(EntityType.HUSK, "Husk", "Husk"),
    IRON_GOLEM(EntityType.IRON_GOLEM, "Iron Golem", "VillagerGolem"),
    LLAMA(EntityType.LLAMA, "Llama", "Llama"),
    MAGMA_CUBE(EntityType.MAGMA_CUBE, "Magma Cube", "LavaSlime"),
    MULE(EntityType.MULE, "Mule", "Mule"),
    MUSHROOM_COW(EntityType.MUSHROOM_COW, "Mushroom Cow", "MushroomCow"),
    OCELOT(EntityType.OCELOT, "Ocelot", "Ozelot"),
    PIG(EntityType.PIG, "Pig", "Pig"),
    PIG_ZOMBIE(EntityType.PIG_ZOMBIE, "Zombie Pigman", "PigZombie"),
    POLAR_BEAR(EntityType.POLAR_BEAR, "Polar Bear", "PolarBear"),
    RABBIT(EntityType.RABBIT, "Rabbit", "Rabbit"),
    SHEEP(EntityType.SHEEP, "Sheep", "Sheep"),
    SILVERFISH(EntityType.SILVERFISH, "Silverfish", "Silverfish"),
    SKELETON(EntityType.SKELETON, "Skeleton", "Skeleton"),
    SKELETON_HORSE(EntityType.SKELETON_HORSE, "Skeleton Horse", "SkeletonHorse"),
    SLIME(EntityType.SLIME, "Slime", "Slime"),
    SNOWMAN(EntityType.SNOWMAN, "Snowman", "SnowMan"),
    SPIDER(EntityType.SPIDER, "Spider", "Spider"),
    SQUID(EntityType.SQUID, "Squid", "Squid"),
    STRAY(EntityType.STRAY, "Stray", "Stray"),
    VEX(EntityType.VEX, "Vex", "Vex"),
    VILLAGER(EntityType.VILLAGER, "Villager", "Villager"),
    VINDICATOR(EntityType.VINDICATOR, "Vindicator", "Vindication_Illager"),
    WITCH(EntityType.WITCH, "Witch", "Witch"),
    WITHER(EntityType.WITHER, "Wither", "WitherBoss"),
    WITHER_SKELETON(EntityType.WITHER_SKELETON, "Wither Skeleton", "WitherSkeleton"),
    WOLF(EntityType.WOLF, "Wolf", "Wolf"),
    ZOMBIE(EntityType.ZOMBIE, "Zombie", "Zombie"),
    ZOMBIE_HORSE(EntityType.ZOMBIE_HORSE, "Zombie Horse", "ZombieHorse"),
    ZOMBIE_VILLAGER(EntityType.ZOMBIE_VILLAGER, "Zombie Villager", "ZombieVillager");

    private final EntityType type;
    private final String name;
    private final String rawName;

    Mob(EntityType type, String name, String rawName){
        this.type = type;
        this.name = name;
        this.rawName = rawName;
    }

    public EntityType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getRawName() {
        return rawName;
    }

    public short getEggId() {
        return type.getTypeId();
    }
}
