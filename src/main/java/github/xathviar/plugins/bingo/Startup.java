package github.xathviar.plugins.bingo;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Startup extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(new BingoListener(new BingoData()), this);
    }

    @Override
    public void onDisable() {

    }

}
