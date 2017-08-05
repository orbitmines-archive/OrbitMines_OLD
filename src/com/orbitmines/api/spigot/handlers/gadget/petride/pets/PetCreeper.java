package com.orbitmines.api.spigot.handlers.gadget.petride.pets;


import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.Mob;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.gadget.petride.PetHandler;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.particle.Particle;
import com.orbitmines.api.spigot.handlers.playerdata.PetData;
import com.orbitmines.api.spigot.perks.Pet;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class PetCreeper extends PetHandler {

    public PetCreeper() {
        super(Pet.CREEPER);

        Particle particle = new Particle(org.bukkit.Particle.EXPLOSION_HUGE);
        particle.setAmount(4);

        add(2, new ItemInstance() {
            @Override
            public void onInteract(PlayerInteractEvent event, OMPlayer omp, PetData petData) {
                event.setCancelled(true);
                omp.updateInventory();

                Creeper c = (Creeper) petData.getPet();

                particle.setLocation(c.getLocation());
                particle.send();

                c.getWorld().playSound(c.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 5, 1);
                petData.setPet(null);
                petData.setPetEnabled(null);

                omp.getPlayer().leaveVehicle();

                for (Entity en : c.getNearbyEntities(3, 3, 3)) {
                    if (en instanceof Player)
                        en.setVelocity(en.getLocation().getDirection().multiply(-1).add(new Vector(0, 1.3, 0)));
                }

                c.remove();
            }

            @Override
            public ItemStack getItem(OMPlayer omp, PetData petData) {
                return new ItemBuilder(Material.TNT, 1, 0, omp.getMessage(new Message("§c§nExplodeer", "§c§nExplode"))).build();
            }
        });

        add(6, new ItemInstance() {
            @Override
            public void onInteract(PlayerInteractEvent event, OMPlayer omp, PetData petData) {
                event.setCancelled(true);
                omp.updateInventory();

                Creeper pet = (Creeper) petData.getPet();
                pet.setPowered(!pet.isPowered());

                ItemMeta meta = event.getItem().getItemMeta();
                meta.setDisplayName(getItem(omp, petData).getItemMeta().getDisplayName());
                event.getItem().setItemMeta(meta);

                omp.getPlayer().playSound(omp.getPlayer().getLocation(), Sound.UI_BUTTON_CLICK, 5, 1);
            }

            @Override
            public ItemStack getItem(OMPlayer omp, PetData petData) {
                return api.getNms().customItem().setEggId(new ItemBuilder(Material.MONSTER_EGG, 1, 0, omp.getMessage(new Message("§a§nVerander Type", "§a§nChange Type")) + (((Creeper) petData.getPet()).isPowered() ? "§7 (§e§lLIGHTNING§7)" : omp.getMessage(new Message("§7 (§6§lNORMAAL§7)", "§7 (§6§lNORMAL§7)")))).build(), Mob.CREEPER);
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
