package com.orbitmines.api.spigot.handlers.gadget.petride.pets;


import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.Cooldown;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.firework.FireworkBuilder;
import com.orbitmines.api.spigot.handlers.gadget.petride.PetHandler;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.playerdata.PetData;
import com.orbitmines.api.spigot.perks.Pet;
import com.orbitmines.api.spigot.utils.LocationUtils;
import com.orbitmines.api.spigot.utils.WorldUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class PetCow extends PetHandler {

    private Cooldown COOLDOWN = new Cooldown(4000, "§f§lMilk Explosion", "§f§lMilk Explosion", Cooldown.Action.RIGHT_CLICK);

    public PetCow() {
        super(Pet.COW);

        add(2, new ItemInstance() {
            @Override
            public void onInteract(PlayerInteractEvent event, OMPlayer omp, PetData petData) {
                event.setCancelled(true);
                omp.updateInventory();

                if (omp.onCooldown(COOLDOWN))
                    return;

                Player p = omp.getPlayer();
                Item iEn = p.getWorld().dropItem(p.getLocation(), new ItemBuilder(event.getItem().getType(), 1, event.getItem().getDurability(), p.getName()).build());
                iEn.setVelocity(p.getLocation().getDirection().multiply(0.8));

                new BukkitRunnable() {
                    public void run() {
                        Location l = iEn.getLocation();

                        FireworkBuilder fw = new FireworkBuilder(l.subtract(0, 1, 0));
                        fw.getBuilder().withColor(Color.WHITE);
                        fw.getBuilder().withColor(Color.WHITE);
                        fw.getBuilder().withFade(Color.WHITE);
                        fw.getBuilder().with(FireworkEffect.Type.BALL_LARGE);
                        fw.getBuilder().withFlicker();
                        fw.getBuilder().withTrail();
                        fw.build();
                        fw.explode();

                        iEn.remove();

                        Location l1 = new Location(iEn.getWorld(), l.getBlockX() + 1, l.getBlockY() + 1, l.getBlockZ() + 1);
                        Location l2 = new Location(iEn.getWorld(), l.getBlockX() - 1, l.getBlockY() - 1, l.getBlockZ() - 1);

                        for (Block b : LocationUtils.getBlocksBetween(l1, l2)) {
                            if (b.isEmpty() || WorldUtils.CANNOT_TRANSFORM.contains(b.getType()))
                                continue;

                            for (Player player : b.getWorld().getPlayers()) {
                                player.sendBlockChange(b.getLocation(), Material.SNOW_BLOCK, (byte) 0);
                            }

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    for (Player p : b.getWorld().getPlayers()) {
                                        p.sendBlockChange(b.getLocation(), b.getType(), b.getData());
                                    }
                                }
                            }.runTaskLater(api, 80);
                        }
                    }
                }.runTaskLater(api, 60);

                omp.resetCooldown(COOLDOWN);
            }

            @Override
            public ItemStack getItem(OMPlayer omp, PetData petData) {
                return new ItemBuilder(Material.MILK_BUCKET, 1, 0, omp.getMessage(new Message("§f§nMelk Explosie", "§f§nMilk Explosion"))).build();
            }
        });

        add(6, new ItemInstance() {
            @Override
            public void onInteract(PlayerInteractEvent event, OMPlayer omp, PetData petData) {
                event.setCancelled(true);
                omp.updateInventory();

                Ageable pet = (Ageable) petData.getPet();

                if (pet.isAdult())
                    pet.setBaby();
                else
                    pet.setAdult();

                event.getItem().setAmount(getItem(omp, petData).getAmount());

                omp.getPlayer().playSound(omp.getPlayer().getLocation(), Sound.UI_BUTTON_CLICK, 5, 1);
            }

            @Override
            public ItemStack getItem(OMPlayer omp, PetData petData) {
                return new ItemBuilder(Material.RAW_BEEF, ((Ageable) petData.getPet()).isAdult() ? 2 : 1, 0, omp.getMessage(new Message("§c§nVerander Leeftijd", "§c§nChange Age"))).build();
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
