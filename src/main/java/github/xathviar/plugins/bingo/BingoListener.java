package github.xathviar.plugins.bingo;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

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
            Player player = (Player) craftItemEvent.getInventory().getHolder();
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
    public void cancelPlayerMovement(PlayerMoveEvent event) {
        if (data.getStartup().isPaused())
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (data.getStartup().isPaused()) {
            event.getPlayer().setGameMode(GameMode.SPECTATOR);
        }
    }
}