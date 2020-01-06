package github.xathviar.plugins.bingo;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import static github.xathviar.plugins.bingo.HelperClass.sendMessage;

public final class Startup extends JavaPlugin {
    private final int[] h = {0};
    private final int[] m = {0};
    private final int[] s = {0};
    private BingoData bingoData;
    private boolean started;
    private BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
    private BukkitTask task;


    @Override
    public void onEnable() {
        bingoData = new BingoData();
        Bukkit.getServer().getPluginManager().registerEvents(new BingoListener(bingoData), this);
    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.AQUA + "[Bingo]" + ChatColor.WHITE + "What do you want to do? See '/bingo help' for more info");
            } else if (args[0].equals("help")) {
                sendMessage((Player) sender, "'/bingo start' starts a new game of bingo");
                sendMessage((Player) sender, "'/bingo reset' reset the items you already have");
                sendMessage((Player) sender, "'/bingo join' join the game of bingo");
                sendMessage((Player) sender, "'/bingo create' create a game of bingo");
            } else if (args[0].equals("reset")) {
                bingoData.resetEntity((Player) sender);
                sendMessage((Player) sender, "Your Items have been resetet");
            } else if (args[0].equals("start")) {
                started = true;
                task = scheduler.runTaskTimer(this, () -> {
                    s[0]++;
                    if (s[0] == 60) {
                        s[0] = 0;
                        m[0]++;
                    }
                    if (m[0] == 60) {
                        m[0] = 0;
                        h[0]++;
                    }
                    Bukkit.getOnlinePlayers().forEach(n -> n.sendActionBar(String.format("Seit %s%02d:%02d:%02d%s in der Challenge", ChatColor.YELLOW, h[0], m[0], s[0], ChatColor.WHITE)));
                }, 0, 20);
            } else if (args[0].equals("pause")) {
                scheduler.cancelTask(task.getTaskId());
            }

        } else {
            System.out.println("You need to be a player to execute this command.");
        }
        return false;
    }
}
