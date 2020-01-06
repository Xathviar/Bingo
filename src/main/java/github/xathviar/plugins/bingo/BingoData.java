package github.xathviar.plugins.bingo;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BingoData {

    public static HashMap<Player, List<Material>> entityBingoMap;

    public static List<Material> bingoItems;

    public BingoData() {
        entityBingoMap = new HashMap<>();
        bingoItems = new ArrayList<>();
        genItems();
    }


    public void genItems() {
        //TODO generator
        bingoItems.add(Material.OAK_BOAT);
        bingoItems.add(Material.ACACIA_FENCE);
        bingoItems.add(Material.LILY_PAD);
        bingoItems.add(Material.WOODEN_PICKAXE);
        bingoItems.add(Material.DIAMOND);
        bingoItems.add(Material.EMERALD_BLOCK);
        bingoItems.add(Material.ENDER_EYE);
        bingoItems.add(Material.IRON_CHESTPLATE);
        bingoItems.add(Material.WHEAT_SEEDS);
    }

    public void checkItem(HumanEntity entity, ItemStack item) {
        checkItem((Player) entity, item);
    }

    public void checkItems(HumanEntity entity, Inventory inventory) {
        checkItems((Player) entity, inventory);
    }

    public void checkItem(Player entity, ItemStack item) {
        if (bingoItems.contains(item.getType())
                && !entityBingoMap.getOrDefault(entity, new ArrayList<Material>() {{
            add(Material.BARRIER);
        }}).contains(item.getType())) {
            if (entityBingoMap.get(entity) != null) {
                List<Material> itemstacks = entityBingoMap.get(entity);
                itemstacks.add(item.getType());
                entityBingoMap.put(entity, itemstacks);
            } else {
                List<Material> items = new ArrayList<>();
                items.add(item.getType());
                entityBingoMap.put(entity, items);
            }
            entity.sendMessage(item.getType().toString() + " has been registered");
        }
        checkWin(entity);
    }

    public void checkItems(Player entity, Inventory inventory) {
        for (Material bingoItem : bingoItems) {
            if (inventory.contains(bingoItem) && !entityBingoMap.getOrDefault(entity, new ArrayList<Material>() {{
                add(Material.BARRIER);
            }}).contains(bingoItem)) {
                if (entityBingoMap.get(entity) != null) {
                    List<Material> itemStacks = entityBingoMap.get(entity);
                    itemStacks.add(bingoItem);
                    entityBingoMap.put(entity, itemStacks);
                } else {
                    List<Material> itemstacks = new ArrayList<>();
                    itemstacks.add(bingoItem);
                    entityBingoMap.put(entity, itemstacks);
                }
                entity.sendMessage(bingoItem.toString() + " has been registered");
            }
        }
        checkWin(entity);
    }

    private void checkWin(Player entity) {
        if (entityBingoMap.get(entity) != null && entityBingoMap.get(entity).size() == 9) {
            Bukkit.getServer().broadcastMessage(entity.getDisplayName() + " won the Bingo");
            genItems();
            entityBingoMap.clear();
        }
    }

    public void resetEntity(Player entity) {
        entityBingoMap.remove(entity);
    }

}
