package com.orbitmines.survival.events;

import com.orbitmines.api.VipRank;
import com.orbitmines.api.spigot.handlers.itembuilders.PlayerSkullBuilder;
import com.orbitmines.api.utils.RandomUtils;
import com.orbitmines.api.utils.uuid.UUIDUtils;
import com.orbitmines.survival.Survival;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.EntityEquipment;

import java.util.List;
import java.util.UUID;

/*
* OrbitMines - @author Fadi Shawki - 12-8-2017
*/
public class SpawnEvent implements Listener {

    private Survival survival;

    public SpawnEvent() {
        survival = Survival.getInstance();
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Zombie) && !(entity instanceof Skeleton))
            return;

        double chance = RandomUtils.RANDOM.nextDouble();

        VipRank vipRank;

        if (chance <= 0.01)
            vipRank = VipRank.IRON;
        else if (chance <= 0.03)
            vipRank = VipRank.GOLD;
        else if (chance <= 0.06)
            vipRank = VipRank.DIAMOND;
        else if (chance <= 0.10)
            vipRank = VipRank.EMERALD;
        else
            return;

        List<UUID> heads = survival.getMobHeads().get(vipRank);
        if (heads.size() == 0)
            return;

        String name = UUIDUtils.getName(heads.get(RandomUtils.RANDOM.nextInt(heads.size())));
        if (name == null)
            return;

        EntityEquipment ee = ((LivingEntity) entity).getEquipment();
        ee.setHelmet(new PlayerSkullBuilder(name).build());
        ee.setHelmetDropChance(0.001F);
    }
}
