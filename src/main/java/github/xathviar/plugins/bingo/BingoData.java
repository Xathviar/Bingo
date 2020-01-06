package github.xathviar.plugins.bingo;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BingoData {

    public static HashMap<Player, List<ItemStack>> entityBingoMap;

    public static List<ItemStack> bingoItems;

    public BingoData() {
        entityBingoMap = new HashMap<>();
        bingoItems = new ArrayList<>();
        genItems(9);
    }


    public void genItems(int itemSize) {
        //TODO generator
        bingoItems.add(new ItemStack(Material.OAK_BOAT));
        bingoItems.add(new ItemStack(Material.ACACIA_FENCE));
        bingoItems.add(new ItemStack(Material.LILY_PAD));
        bingoItems.add(new ItemStack(Material.WOODEN_PICKAXE));
        bingoItems.add(new ItemStack(Material.DIAMOND));
        bingoItems.add(new ItemStack(Material.EMERALD_BLOCK));
        bingoItems.add(new ItemStack(Material.ENDER_EYE));
        bingoItems.add(new ItemStack(Material.IRON_CHESTPLATE));
        bingoItems.add(new ItemStack(Material.WHEAT_SEEDS));
    }

    public void checkItem(HumanEntity entity, ItemStack item) {
        checkItem((Player) entity, item);
    }

    public void checkItems(HumanEntity entity, Inventory inventory) {
        checkItems((Player)entity, inventory);
    }
    public void checkItem(Player entity, ItemStack item) {
        if (bingoItems.contains(item) && !entityBingoMap.get(entity).contains(item)) {
            List<ItemStack> itemstacks = entityBingoMap.get(entity);
            itemstacks.add(item);
            entityBingoMap.put(entity, itemstacks);
            entity.sendMessage(item.getType().toString() + " has been registered");
        }
    }

    public void checkItems(Player entity, Inventory inventory) {
        for (ItemStack bingoItem : bingoItems) {
            if (inventory.contains(bingoItem) && !entityBingoMap.get(entity).contains(bingoItem)) {
                List<ItemStack> itemStacks = entityBingoMap.get(entity);
                itemStacks.add(bingoItem);
                entityBingoMap.put(entity, itemStacks);
                entity.sendMessage(bingoItem.getType().toString() + " has been registered");
            }
        }
    }

}
