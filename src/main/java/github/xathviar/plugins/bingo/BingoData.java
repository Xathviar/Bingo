package github.xathviar.plugins.bingo;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static github.xathviar.plugins.bingo.HelperClass.broadcastMessage;

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
        Set<Material> items = new HashSet<>();
        List<Material> materials = Arrays.asList(Material.class.getEnumConstants());
        materials.remove(Material.BARRIER);
        materials.remove(Material.BEDROCK);
        materials.remove(Material.AIR);
        materials.remove(Material.END_CRYSTAL);
        materials.remove(Material.END_GATEWAY);
        materials.remove(Material.END_PORTAL);
        materials.remove(Material.NETHER_PORTAL);
        materials.remove(Material.END_PORTAL_FRAME);
        materials.remove(Material.END_ROD);
        materials.remove(Material.ENDER_CHEST);
        materials.remove(Material.LAVA);
        materials.remove(Material.WATER);
        materials.remove(Material.DIAMOND_ORE);
        materials.remove(Material.COAL_ORE);
        materials.remove(Material.SPAWNER);
        materials.remove(Material.BAT_SPAWN_EGG);

        while (items.size() < 9) {
            items.add(materials.get(new Random().nextInt(materials.size())));
        }

        bingoItems.clear();
        bingoItems.addAll(items);
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
                broadcastMessage(ChatColor.YELLOW + item.getType().toString() + ChatColor.RESET + " has been registered" + " (" + entityBingoMap.get(entity).size() + "/9)");
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
                    broadcastMessage(ChatColor.YELLOW + bingoItem.toString() + ChatColor.RESET + " has been registered by " + entity.getDisplayName() + " (" + entityBingoMap.get(entity).size() + "/9)");
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
                itemStack.setLore(Collections.singletonList(ChatColor.RED + "Item not found" + ChatColor.RESET));
                if (entityBingoMap.get(entity) != null && entityBingoMap.get(entity).contains(bingoItems.get(counter))) {
                    itemStack.setLore(Collections.singletonList(ChatColor.GREEN + "Item found" + ChatColor.RESET));
                    itemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
                    itemStack.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }
                inventory.setItem(9 * i + j, itemStack);
                counter++;
            }
        }
        entity.openInventory(inventory);
    }

}
