package com.orbitmines.api.spigot.handlers.gadget.petride.pets;


import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.Mob;
import com.orbitmines.api.spigot.handlers.Cooldown;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.gadget.petride.PetHandler;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.particle.Particle;
import com.orbitmines.api.spigot.handlers.playerdata.PetData;
import com.orbitmines.api.spigot.perks.Pet;
import com.orbitmines.api.spigot.utils.RandomUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class PetOcelot extends PetHandler {

    private Cooldown COOLDOWN = new Cooldown(1000, "§e§lKitty Cannon", "§e§lKitty Cannon", Cooldown.Action.RIGHT_CLICK);

    public PetOcelot() {
        super(Pet.OCELOT);

        Particle particle = new Particle(org.bukkit.Particle.EXPLOSION_LARGE);
        particle.setAmount(1);

        Ocelot.Type[] values = Ocelot.Type.values();

        add(2, new ItemInstance() {
            @Override
            public void onInteract(PlayerInteractEvent event, OMPlayer omp, PetData petData) {
                event.setCancelled(true);
                omp.updateInventory();

                if (omp.onCooldown(COOLDOWN))
                    return;

                Player p = omp.getPlayer();
                Ocelot o = (Ocelot) p.getWorld().spawnEntity(p.getLocation(), EntityType.OCELOT);
                o.setBaby();
                o.setVelocity(p.getLocation().getDirection().multiply(2));
                o.setRemoveWhenFarAway(false);
                o.setCatType(values[RandomUtils.RANDOM.nextInt(values.length)]);

                new BukkitRunnable() {
                    public void run() {
                        particle.setLocation(o.getLocation());
                        particle.send();

                        o.getWorld().playSound(o.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 5, 1);
                        o.remove();
                    }
                }.runTaskLater(api, 20);

                omp.resetCooldown(COOLDOWN);
            }

            @Override
            public ItemStack getItem(OMPlayer omp, PetData petData) {
                return api.getNms().customItem().setEggId(new ItemBuilder(Material.MONSTER_EGG, 1, 0, "§e§nKitty Cannon").build(), Mob.OCELOT);
            }
        });

        add(6, new ItemInstance() {
            @Override
            public void onInteract(PlayerInteractEvent event, OMPlayer omp, PetData petData) {
                event.setCancelled(true);
                omp.updateInventory();

                Ocelot pet = (Ocelot) petData.getPet();
                int colorIndex = pet.getType().ordinal() + 1;
                if (values.length == colorIndex + 1)
                    colorIndex = 0;

                pet.setCatType(values[colorIndex]);

                omp.getPlayer().playSound(omp.getPlayer().getLocation(), Sound.UI_BUTTON_CLICK, 5, 1);
            }

            @Override
            public ItemStack getItem(OMPlayer omp, PetData petData) {
                return new ItemBuilder(Material.RAW_FISH, 1, 0, omp.getMessage(new Message("§9§nVerander Kleur", "§9§nChange Color"))).build();
            }
        });
    }

    @Override
    protected void onRun() {

    }

    @Override
    public void onLogout(OMPlayer omp) {

    }
}
