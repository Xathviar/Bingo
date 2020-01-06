package github.xathviar.plugins.bingo;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

public class HelperClass {

    public static void sendMessage(Player player, String message) {
        player.sendMessage(ChatColor.AQUA + "[Bingo] " + ChatColor.WHITE + message);
    }

    public static void sendMessage(HumanEntity entity, String message) {
        sendMessage((Player) entity, message);
    }

    public static void broadcastMessage(String message) {
        Bukkit.getServer().broadcastMessage(ChatColor.AQUA + "[Bingo] " + ChatColor.WHITE + message);
    }
}
