package com.orbitmines.api.spigot.handlers.npc;

import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import java.util.*;

/**
 * Created by Fadi on 5-9-2016.
 */
public class NpcArmorStand {

    private static List<NpcArmorStand> npcArmorStands = new ArrayList<>();

    private static OrbitMinesApi api;

    private Location location;
    private ArmorStand armorStand;
    private boolean arms;
    private boolean basePlate;
    private EulerAngle bodyPose;
    private EulerAngle headPose;
    private EulerAngle leftArmPose;
    private EulerAngle leftLegPose;
    private EulerAngle rightArmPose;
    private EulerAngle rightLegPose;
    private ItemStack itemInHand;
    private ItemStack helmet;
    private ItemStack chestPlate;
    private ItemStack leggings;
    private ItemStack boots;
    private String customName;
    private boolean customNameVisible;
    private int fireTicks;
    private boolean gravity;
    private boolean small;
    private boolean visible;

    private boolean hideOnJoin;
    private List<Player> watchers;

    private ClickAction clickAction;

    public NpcArmorStand(Location location, boolean hideOnJoin) {
        this(location, hideOnJoin, null);
    }

    public NpcArmorStand(Location location, boolean hideOnJoin, ClickAction clickAction) {
        npcArmorStands.add(this);
        api = OrbitMinesApi.getApi();

        this.location = location;
        this.hideOnJoin = hideOnJoin;
        this.watchers = new ArrayList<>();

        this.arms = false;
        this.basePlate = false;
        this.bodyPose = EulerAngle.ZERO;
        this.headPose = EulerAngle.ZERO;
        this.leftArmPose = EulerAngle.ZERO;
        this.leftLegPose = EulerAngle.ZERO;
        this.rightArmPose = EulerAngle.ZERO;
        this.rightLegPose = EulerAngle.ZERO;
        this.customNameVisible = false;
        this.fireTicks = 0;
        this.gravity = true;
        this.small = false;
        this.visible = true;
        this.clickAction = clickAction;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ArmorStand getArmorStand() {
        return armorStand;
    }

    public boolean hasArms() {
        return arms;
    }

    public void setArms(boolean arms) {
        this.arms = arms;
    }

    public boolean hasBasePlate() {
        return basePlate;
    }

    public void setBasePlate(boolean basePlate) {
        this.basePlate = basePlate;
    }

    public EulerAngle getBodyPose() {
        return bodyPose;
    }

    public void setBodyPose(EulerAngle bodyPose) {
        this.bodyPose = bodyPose;
    }

    public EulerAngle getHeadPose() {
        return headPose;
    }

    public void setHeadPose(EulerAngle headPose) {
        this.headPose = headPose;
    }

    public EulerAngle getLeftArmPose() {
        return leftArmPose;
    }

    public void setLeftArmPose(EulerAngle leftArmPose) {
        this.leftArmPose = leftArmPose;
    }

    public EulerAngle getLeftLegPose() {
        return leftLegPose;
    }

    public void setLeftLegPose(EulerAngle leftLegPose) {
        this.leftLegPose = leftLegPose;
    }

    public EulerAngle getRightArmPose() {
        return rightArmPose;
    }

    public void setRightArmPose(EulerAngle rightArmPose) {
        this.rightArmPose = rightArmPose;
    }

    public EulerAngle getRightLegPose() {
        return rightLegPose;
    }

    public void setRightLegPose(EulerAngle rightLegPose) {
        this.rightLegPose = rightLegPose;
    }

    public ItemStack getItemInHand() {
        return itemInHand;
    }

    public void setItemInHand(ItemStack itemInHand) {
        this.itemInHand = itemInHand;
    }

    public ItemStack getHelmet() {
        return helmet;
    }

    public void setHelmet(ItemStack helmet) {
        this.helmet = helmet;
    }

    public ItemStack getChestPlate() {
        return chestPlate;
    }

    public void setChestPlate(ItemStack chestPlate) {
        this.chestPlate = chestPlate;
    }

    public ItemStack getLeggings() {
        return leggings;
    }

    public void setLeggings(ItemStack leggings) {
        this.leggings = leggings;
    }

    public ItemStack getBoots() {
        return boots;
    }

    public void setBoots(ItemStack boots) {
        this.boots = boots;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public boolean isCustomNameVisible() {
        return customNameVisible;
    }

    public void setCustomNameVisible(boolean customNameVisible) {
        this.customNameVisible = customNameVisible;
    }

    public int getFireTicks() {
        return fireTicks;
    }

    public void setFireTicks(int fireTicks) {
        this.fireTicks = fireTicks;
    }

    public boolean isGravity() {
        return gravity;
    }

    public void setGravity(boolean gravity) {
        this.gravity = gravity;
    }

    public boolean isSmall() {
        return small;
    }

    public void setSmall(boolean small) {
        this.small = small;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean hideOnJoin() {
        return hideOnJoin;
    }

    public boolean canClick() {
        return clickAction != null;
    }

    public void setClickAction(ClickAction clickAction) {
        this.clickAction = clickAction;
    }

    public void spawn() {
        spawn((Collection<? extends Player>) null);
    }

    public void spawn(Player... createFor) {
        spawn(Arrays.asList(createFor));
    }

    public void spawn(Collection<? extends Player> createFor) {
        if (watchers.size() != 0)
            createFor = watchers;

        clear();

        Chunk chunk = location.getChunk();
        chunk.load();
        api.getChunks().add(chunk);

        armorStand = (ArmorStand) getLocation().getWorld().spawnEntity(getLocation(), EntityType.ARMOR_STAND);

        update();

        if (createFor == null)
            return;

        watchers.addAll(createFor);

        createForWatchers();
    }

    public void createForWatchers() {
        List<Player> hideFor = new ArrayList<>();
        for (Player player : location.getWorld().getPlayers()) {
            if (!watchers.contains(player))
                hideFor.add(player);
        }

        hideFor(hideFor);
    }

    public List<Player> getWatchers() {
        return watchers;
    }

    public void clear() {
        if (armorStand != null)
            armorStand.remove();
    }

    public void hideFor(Player player) {
        hideFor(Collections.singletonList(player));
    }

    public void hideFor(Collection<? extends Player> players) {
        api.getNms().entity().destroyEntityFor(players, armorStand);
    }

    public void update() {
        armorStand.setRemoveWhenFarAway(false);
        armorStand.setArms(arms);
        armorStand.setBasePlate(basePlate);
        armorStand.setBodyPose(bodyPose);
        armorStand.setBoots(boots);
        armorStand.setChestplate(chestPlate);
        armorStand.setFireTicks(fireTicks);
        armorStand.setGravity(gravity);
        armorStand.setHeadPose(headPose);
        armorStand.setHelmet(helmet);
        armorStand.setItemInHand(itemInHand);
        armorStand.setLeftArmPose(leftArmPose);
        armorStand.setLeftLegPose(leftLegPose);
        armorStand.setLeggings(leggings);
        armorStand.setRightArmPose(rightArmPose);
        armorStand.setRightLegPose(rightLegPose);
        armorStand.setSmall(small);
        armorStand.setVisible(visible);

        updateName();
    }

    public void updateName() {
        if (armorStand == null)
            return;

        armorStand.setCustomName(customName);
        armorStand.setCustomNameVisible(customNameVisible);
    }

    public void delete() {
        clear();
        npcArmorStands.remove(this);
    }

    public void click(PlayerInteractAtEntityEvent event, OMPlayer player) {
        clickAction.click(event, player, this);
    }

    public static NpcArmorStand getNpcArmorStand(ArmorStand armorStand) {
        for (NpcArmorStand npcArmorStand : npcArmorStands) {
            if (npcArmorStand.getArmorStand().getEntityId() == armorStand.getEntityId())
                return npcArmorStand;
        }

        return null;
    }

    public static List<NpcArmorStand> getNpcArmorStands() {
        return npcArmorStands;
    }

    public static abstract class ClickAction {

        public abstract void click(PlayerInteractAtEntityEvent event, OMPlayer player, NpcArmorStand item);

    }
}
