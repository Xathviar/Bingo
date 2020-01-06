package github.xathviar.plugins.bingo;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class BingoListener implements Listener {
    private BingoData data;

    public BingoListener(BingoData data) {
        this.data = data;
    }

    @EventHandler
    public void onPlayerPickup(EntityPickupItemEvent entityPickupItemEvent) {
        if (entityPickupItemEvent.getEntity() instanceof Player) {
            Player player = (Player) entityPickupItemEvent.getEntity();
            player.sendMessage(entityPickupItemEvent.getItem().getItemStack().toString());
            data.checkItem(player, entityPickupItemEvent.getItem().getItemStack());
        }
    }

    @EventHandler
    public void onCraftEvent(CraftItemEvent craftItemEvent) {
        if (craftItemEvent.getInventory().getHolder() instanceof Player) {
            Player player = (Player) craftItemEvent.getInventory().getHolder();
            player.sendMessage(craftItemEvent.getRecipe().getResult().toString());
            data.checkItem(craftItemEvent.getWhoClicked(), craftItemEvent.getRecipe().getResult());
        }
    }

    @EventHandler
    public void onChestShifting(InventoryClickEvent event) {
        data.checkItems(event.getWhoClicked(), event.getWhoClicked().getInventory());
    }

    @EventHandler
    public void onInvetoryClose(InventoryCloseEvent event) {
        data.checkItems(event.getPlayer(), event.getPlayer().getInventory());
    }
}