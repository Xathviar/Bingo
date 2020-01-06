package github.xathviar.plugins.bingo;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static github.xathviar.plugins.bingo.HelperClass.broadcastMessage;
import static github.xathviar.plugins.bingo.HelperClass.sendMessage;

public class BingoData {

    private static HashMap<Player, List<Material>> entityBingoMap;

    private static List<Material> bingoItems;

    private Startup startup;

    public BingoData(Startup startup) {
        entityBingoMap = new HashMap<>();
        bingoItems = new ArrayList<>();
        this.startup = startup;
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
        if (startup.hasStarted()) {
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
                sendMessage(entity, ChatColor.YELLOW + item.getType().toString() + ChatColor.WHITE + " has been registered" + " (" + entityBingoMap.get(entity).size() + "/9)");
            }
            checkWin(entity);
        }
    }

    public void checkItems(Player entity, Inventory inventory) {
        if (startup.hasStarted()) {
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
                    sendMessage(entity, ChatColor.YELLOW + bingoItem.toString() + ChatColor.WHITE + " has been registered" + " (" + entityBingoMap.get(entity).size() + "/9)");
                }
            }
            checkWin(entity);
        }
    }

    private void checkWin(Player entity) {
        if (entityBingoMap.get(entity) != null && entityBingoMap.get(entity).size() == 9) {
            genItems();
            entityBingoMap.clear();
            int[] time = startup.stopScheduler();
            if (time[0] != 0)
                broadcastMessage(String.format("%s won the Bingo after %02d Hours, %02d Minutes, %02d Seconds.", entity.getDisplayName(), time[0], time[1], time[2]));
            else if (time[1] != 0)
                broadcastMessage(String.format("%s won the Bingo after %02d Minutes, %02d Seconds.", entity.getDisplayName(), time[1], time[2]));
            else
                broadcastMessage(String.format("%s won the Bingo after %02d Seconds.", entity.getDisplayName(), time[2]));
        }
    }

    public void resetEntity(Player entity) {
        entityBingoMap.remove(entity);
    }

    public void displayBoard(Player entity) {
        Inventory inventory = Bukkit.createInventory(entity, 5 * 9, "Bingo Board");
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, new ItemStack(Material.LIME_STAINED_GLASS_PANE));
        }
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i + 9 * 4, new ItemStack(Material.LIME_STAINED_GLASS_PANE));
        }

        int counter = 0;
        for (int i = 1; i < 4; i++) {
            for (int j = 3; j < 6; j++) {
                ItemStack itemStack = new ItemStack(bingoItems.get(counter));
                if (entityBingoMap.get(entity).contains(bingoItems.get(counter))) {
                    itemStack.setLore(Collections.singletonList(ChatColor.GREEN + "Item found" + ChatColor.RESET));
                    itemStack.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }
                inventory.setItem(9 * i + j, itemStack);
                counter++;
            }
        }
        entity.openInventory(inventory);
    }

}
