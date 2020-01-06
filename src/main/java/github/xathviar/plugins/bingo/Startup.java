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
        started = false;
        bingoData = new BingoData(this);
        Bukkit.getServer().getPluginManager().registerEvents(new BingoListener(bingoData), this);
        this.getCommand("wc").setExecutor(new UtilsCommandExecutor(this));
        this.getCommand("rc").setExecutor(new UtilsCommandExecutor(this));
        this.getCommand("gm1").setExecutor(new UtilsCommandExecutor(this));
        this.getCommand("gm0").setExecutor(new UtilsCommandExecutor(this));
        this.getCommand("heal").setExecutor(new UtilsCommandExecutor(this));
        this.getCommand("feed").setExecutor(new UtilsCommandExecutor(this));
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
                sendMessage((Player) sender, "'/bingo board' displays the current bingo board");
            } else if (args[0].equals("reset") && sender.hasPermission("bingo.reset")) {
                bingoData.resetEntity((Player) sender);
                sendMessage((Player) sender, "Your items have been reset");
            } else if (args[0].equals("start") && sender.hasPermission("bingo.start")) {
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
                    Bukkit.getOnlinePlayers().forEach(n -> n.sendActionBar(String.format("Seit %s%02d:%02d:%02d%s im Bingo", ChatColor.YELLOW, h[0], m[0], s[0], ChatColor.WHITE)));
                }, 0, 20);
            } else if (args[0].equals("pause") && sender.hasPermission("bingo.pause")) {
                started = false;
                scheduler.cancelTask(task.getTaskId());
            } else if (args[0].equals("board")) {
                if (started) {
                    bingoData.displayBoard((Player) sender);
                } else {
                    sendMessage((Player) sender, "You cannot see the board if the game hasn't even started yet.");
                }
            } else {
                sendMessage((Player) sender, "That is not a valid bingo command");
            }

        } else {
            System.out.println("You need to be a player to execute this command.");
        }
        return false;
    }

    public int[] stopScheduler() {
        int[] time = new int[3];
        scheduler.cancelTask(task.getTaskId());
        time[0] = h[0];
        time[1] = m[0];
        time[2] = s[0];
        started = false;
        return time;
    }

    public boolean hasStarted() {
        return started;
    }
}
