package github.xathviar.plugins.bingo;

import com.destroystokyo.paper.Title;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Random;

import static github.xathviar.plugins.bingo.HelperClass.broadcastMessage;
import static github.xathviar.plugins.bingo.HelperClass.sendMessage;

public final class Startup extends JavaPlugin {
    private final int[] h = {0};
    private final int[] m = {0};
    private final int[] s = {0};
    private BingoData bingoData;
    private boolean started;
    private boolean paused;
    private BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
    private BukkitTask task;


    @Override
    public void onEnable() {
        started = false;
        paused = false;
        bingoData = new BingoData(this);
        Bukkit.getServer().getPluginManager().registerEvents(new BingoListener(bingoData), this);
    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (command.getName().equalsIgnoreCase("bingo")) {
                if (args.length == 0) {
                    sender.sendMessage(ChatColor.AQUA + "[Bingo]" + ChatColor.WHITE + "What do you want to do? See '/bingo help' for more info");
                } else if (args[0].equalsIgnoreCase("help")) {
                    sendMessage((Player) sender, "'/bingo start' starts a new game of bingo");
                    sendMessage((Player) sender, "'/bingo reset' reset the items you already have");
                    sendMessage((Player) sender, "'/bingo board' displays the current bingo board");
                    sendMessage((Player) sender, "'/bingo custom' lets you create a custom bingo board");
                } else if (args[0].equalsIgnoreCase("reset") && sender.hasPermission("bingo.reset")) {
                    if (started) {
                        if (task != null) {
                            started = false;
                            paused = false;
                            Bukkit.getOnlinePlayers().forEach(n -> n.setGameMode(GameMode.SURVIVAL));
                            scheduler.cancelTask(task.getTaskId());
                            s[0] = 0;
                            m[0] = 0;
                            h[0] = 0;
                        }
                        bingoData.reset();
                        broadcastMessage("The bingo game has been reset");
                        Bukkit.getOnlinePlayers().forEach(n -> n.setBedSpawnLocation(Bukkit.getWorld("world").getSpawnLocation(), false));
                        Bukkit.getOnlinePlayers().forEach(n -> n.getInventory().clear());
                        Bukkit.getOnlinePlayers().forEach(n -> n.teleport(Bukkit.getWorld("world").getSpawnLocation()));
                        Bukkit.unloadWorld("BingoWorld", false);
                        deleteWorld(Bukkit.getWorld("BingoWorld").getWorldFolder().toPath());
                    } else {
                        broadcastMessage("There is no bingo game to reset");
                    }
                } else if (args[0].equalsIgnoreCase("start") && sender.hasPermission("bingo.start") && !paused) {
                    if (started) {
                        broadcastMessage("There is already a running game.");
                        return true;
                    }
                    started = true;
                    Player p = (Player) sender;
                    World bingoWorld = new WorldCreator("BingoWorld")
                            .generateStructures(true)
                            .seed(new Random().nextLong())
                            .type(WorldType.NORMAL)
                            .createWorld();
                    while (!bingoWorld.isChunkGenerated(bingoWorld.getSpawnLocation().getChunk().getChunkKey())) {
                        try {
                            Title title = new Title("Please wait while the Map is loading", "", 0, 1000, 0);
                            Bukkit.getOnlinePlayers().forEach(n -> n.sendTitle(title));
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        onlinePlayer.setHealth(20);
                        onlinePlayer.setFoodLevel(20);
                        onlinePlayer.setSaturation(1);
                        onlinePlayer.getInventory().clear();
                        onlinePlayer.setGameMode(GameMode.SURVIVAL);
                        onlinePlayer.teleport(bingoWorld.getSpawnLocation());
                        onlinePlayer.setBedSpawnLocation(bingoWorld.getSpawnLocation(), true);
                        onlinePlayer.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 255, 10));
                    }
                    broadcastMessage("The bingo game has been started. Good luck everyone");
                    startTask();
                } else if (args[0].equalsIgnoreCase("resume") && sender.hasPermission("bingo.resume")) {
                    if (paused && started) {
                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                            onlinePlayer.setGameMode(GameMode.SURVIVAL);
                        }
                        paused = false;
                        startTask();
                    } else {
                        sendMessage((Player) sender, "You cannot resume the game if the game is not paused");
                    }
                } else if (args[0].equalsIgnoreCase("pause") && sender.hasPermission("bingo.pause")) {
                    if (!paused && started) {
                        paused = true;
                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                            onlinePlayer.setGameMode(GameMode.SPECTATOR);
                        }
                        scheduler.cancelTask(task.getTaskId());
                    } else {
                        sendMessage((Player) sender, "You cannot pause the game if there is no game");
                    }
                } else if (args[0].equalsIgnoreCase("board")) {
                    if (started) {
                        bingoData.displayBoard((Player) sender);
                    } else {
                        sendMessage((Player) sender, "You cannot see the board if the game hasn't even started yet.");
                    }
                } else if (args[0].equalsIgnoreCase("custom")) {
                    if (!started) {
                        Player player = (Player) sender;
                        player.openInventory(Bukkit.createInventory((Player) sender, InventoryType.DROPPER, "Custom Bingo Board"));
                    } else {
                        sendMessage((Player) sender, "You cannot set the board if a game is running.");
                    }
                } else {
                    sendMessage((Player) sender, "That is not a valid bingo command");
                }
            } else if (command.getName().equalsIgnoreCase("bb")) {
                if (started) {
                    bingoData.displayBoard((Player) sender);
                } else {
                    sendMessage((Player) sender, "You cannot see the board if the game hasn't even started yet.");
                }
            }
        } else {
            System.out.println("You need to be a player to execute this command.");
        }
        return false;
    }

    private void startTask() {
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

    public void resetTimer() {
        h[0] = 0;
        m[0] = 0;
        s[0] = 0;
    }

    public boolean hasStarted() {
        return started;
    }

    public boolean isPaused() {
        return paused;
    }

    public static void deleteWorld(Path path) {
        try {
            Files.walk(path)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
