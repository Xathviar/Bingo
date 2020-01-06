package github.xathviar.plugins.bingo;

import github.xathviar.plugins.bingo.commands.Bingo;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Startup extends JavaPlugin {
    BingoData bingoData;

    @Override
    public void onEnable() {
        bingoData = new BingoData();
        Bukkit.getServer().getPluginManager().registerEvents(new BingoListener(bingoData), this);
        getCommand("bingo").setExecutor(new Bingo(bingoData));
    }

    @Override
    public void onDisable() {

    }

}
