package dev.mruniverse.purplereports.handler.platforms;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.mruniverse.purplereports.handler.PlayerHandler;
import net.kyori.adventure.text.Component;

public class VelocityPlayerHandler implements PlayerHandler {
    private final ProxyServer plugin;

    public <T> VelocityPlayerHandler(T plugin) {
        this.plugin = (ProxyServer) plugin;
    }

    @Override
    public void announce(String message) {
        Component component = Component.text(message);
        for (Player player : plugin.getAllPlayers()) {
            player.sendMessage(component);
        }
    }

    @Override
    public void announceOnly(String message, String permission) {
        Component component = Component.text(message);
        for (Player player : plugin.getAllPlayers()) {
            if (player.hasPermission(permission)) {
                player.sendMessage(component);
            }
        }
    }

    @Override
    public int getPlayersSize() {
        return plugin.getPlayerCount();
    }

    @Override
    public int getMaxPlayers() {
        return plugin.getConfiguration().getShowMaxPlayers();
    }
}