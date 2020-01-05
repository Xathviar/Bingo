package github.xathviar.plugins.bingo;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.management.BufferPoolMXBean;
import java.util.HashMap;

public class BingoListener implements Listener {
    HashMap<Player, ItemStack> itemStackHashMap = new HashMap<>();

    @EventHandler
    public void onPlayerPickup(EntityPickupItemEvent entityPickupItemEvent) {
        if (entityPickupItemEvent.getEntity() instanceof Player) {
            Player player = (Player) entityPickupItemEvent.getEntity();
            player.sendMessage(entityPickupItemEvent.getItem().getItemStack().toString());
        }
    }

    @EventHandler
    public void onCraftEvent(CraftItemEvent craftItemEvent) {
        if (craftItemEvent.getInventory().getHolder() instanceof Player) {
            Player player = (Player) craftItemEvent.getInventory().getHolder();
            player.sendMessage(craftItemEvent.getRecipe().getResult().toString());
        }
    }

    @EventHandler
    public void onChestShifting(InventoryClickEvent event) {
        Inventory top = event.getView().getTopInventory();
        Inventory bottom = event.getView().getBottomInventory();
        if (top.getType() == InventoryType.CHEST && bottom.getType() == InventoryType.PLAYER) {
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
                event.getWhoClicked().sendMessage(event.getCurrentItem().toString());
                itemStackHashMap.put((Player) event.getWhoClicked(), event.getCurrentItem());
            } else if (event.getCurrentItem() != null
                    && itemStackHashMap.getOrDefault((Player) event.getWhoClicked(), new ItemStack(Material.BARRIER)).getType() != Material.BARRIER
                    && event.getCurrentItem().getType() == Material.AIR) {
                event.getWhoClicked().sendMessage(itemStackHashMap.get((Player) event.getWhoClicked()).toString());
                itemStackHashMap.remove((Player) event.getWhoClicked());
            }
        }
    }
}