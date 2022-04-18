package dev.mruniverse.purplereports.handler.platforms;

import dev.mruniverse.purplereports.handler.PlayerHandler;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotPlayerHandler implements PlayerHandler {

    private final JavaPlugin plugin;

    public <T> SpigotPlayerHandler(T plugin) {
        this.plugin = (JavaPlugin) plugin;
    }

    @Override
    public void announce(String message) {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }

    @Override
    public void announceOnly(String message, String permission) {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (player.hasPermission(permission)) {
                player.sendMessage(message);
            }
        }
    }

    @Override
    public int getPlayersSize() {
        return plugin.getServer().getOnlinePlayers().size();
    }

    @Override
    public int getMaxPlayers() {
        return plugin.getServer().getMaxPlayers();
    }
}
