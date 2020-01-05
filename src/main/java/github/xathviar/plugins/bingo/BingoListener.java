package github.xathviar.plugins.bingo;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

public class BingoListener implements Listener {

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
    public void onChestShifting(InventoryInteractEvent inventoryInteractEvent) {
        Player player = (Player) inventoryInteractEvent.getWhoClicked();
        player.sendMessage("soad");
        //player.sendMessage(inventoryInteractEvent.get);
    }

}
