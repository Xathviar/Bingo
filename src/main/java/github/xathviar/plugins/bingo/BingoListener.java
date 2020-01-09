package github.xathviar.plugins.bingo;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

import static github.xathviar.plugins.bingo.HelperClass.sendMessage;

public class BingoListener implements Listener {
    private BingoData data;

    public BingoListener(BingoData data) {
        this.data = data;
    }

    @EventHandler
    public void onPlayerPickup(EntityPickupItemEvent entityPickupItemEvent) {
        if (entityPickupItemEvent.getEntity() instanceof Player) {
            Player player = (Player) entityPickupItemEvent.getEntity();
            data.checkItem(player, entityPickupItemEvent.getItem().getItemStack());
        }
    }

    @EventHandler
    public void onCraftEvent(CraftItemEvent craftItemEvent) {
        if (craftItemEvent.getInventory().getHolder() instanceof Player) {
            data.checkItem(craftItemEvent.getWhoClicked(), craftItemEvent.getRecipe().getResult());
        }
    }

    @EventHandler
    public void onChestShifting(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Bingo Board")) {
            event.setCancelled(true);
        }
        data.checkItems(event.getWhoClicked(), event.getWhoClicked().getInventory());
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        data.checkItems(event.getPlayer(), event.getPlayer().getInventory());
    }

    @EventHandler
    public void onCustomBingoBoardClose(InventoryCloseEvent event) {
        if (event.getPlayer().getOpenInventory().getTitle().equals("Custom Bingo Board")) {
            Inventory inventory = event.getPlayer().getOpenInventory().getTopInventory();
            if (!inventory.contains(Material.AIR)) {
                Set<Material> items = new HashSet<>();
                for (ItemStack customItem : inventory.getContents()) {
                    if (customItem == null) {
                        sendMessage((Player) event.getPlayer(), "You have to insert 9 items.");
                        return;
                    }
                    items.add(customItem.getType());
                }
                if (items.size() == 9) {
                    data.customBingoBoard(inventory);
                    sendMessage((Player) event.getPlayer(), "You successfully set a custom bingo board.");
                } else {
                    sendMessage((Player) event.getPlayer(), "You have to insert 9 different items.");
                }
            }
        }
    }
}