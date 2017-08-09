package com.orbitmines.api.spigot.handlers.kit;

import com.orbitmines.api.utils.RandomUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class KitRandomItems extends Kit {

    private List<List<ItemStack>> randomItems;

    public KitRandomItems(String name) {
        super(name);

        this.randomItems = new ArrayList<>();
        for (int i = 0; i < 36; i++) {
            this.randomItems.add(new ArrayList<>());
        }
    }

    public List<List<ItemStack>> getRandomItems() {
        return randomItems;
    }

    public void setRandomItems(List<List<ItemStack>> randomItems) {
        this.randomItems = randomItems;
    }

    public void setRandomItem(int index, List<ItemStack> content) {
        this.randomItems.set(index, content);
    }

    public List<ItemStack> getRandomItem(int index) {
        return this.randomItems.get(index);
    }

    @Override
    public void setItems(Player p) {
        super.setItems(p);

        randomize(p);
    }

    @Override
    public void addItems(Player p) {
        super.addItems(p);

        randomize(p);
    }

    @Override
    public void replaceItems(Player p) {
        super.replaceItems(p);

        randomize(p);
    }

    private void randomize(Player p) {
        int index = 0;
        for (List<ItemStack> items : getRandomItems()) {
            if (items != null && items.size() > 0)
                p.getInventory().setItem(index, items.get(RandomUtils.RANDOM.nextInt(items.size())));

            index++;
        }
    }
}
