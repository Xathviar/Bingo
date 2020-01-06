package github.xathviar.plugins.bingo.commands;

import github.xathviar.plugins.bingo.BingoData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Bingo implements CommandExecutor {
    BingoData data;

    public Bingo(BingoData data) {
        this.data = data;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.AQUA + "[Bingo]" + ChatColor.WHITE + "What do you want to do? See '/bingo help' for more info");
            } else if (args[0].equals("help")) {
                sender.sendMessage(ChatColor.AQUA + "[Bingo]" + ChatColor.WHITE + "'/bingo start' starts a new game of bingo");
                sender.sendMessage(ChatColor.AQUA + "[Bingo]" + ChatColor.WHITE + "'/bingo reset' reset the items you already have");
                sender.sendMessage(ChatColor.AQUA + "[Bingo]" + ChatColor.WHITE + "'/bingo join' join the game of bingo");
                sender.sendMessage(ChatColor.AQUA + "[Bingo]" + ChatColor.WHITE + "'/bingo create' create a game of bingo");
            } else if (args[0].equals("reset")) {
                data.resetEntity((Player) sender);
                sender.sendMessage(ChatColor.AQUA + "[Bingo]" + ChatColor.WHITE + "Your Items have been resetet");
            }

        } else {
            System.out.println("You need to be a player to execute this command.");
        }
        return false;
    }
}
