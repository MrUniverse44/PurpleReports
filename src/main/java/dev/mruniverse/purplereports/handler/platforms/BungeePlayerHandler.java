package dev.mruniverse.purplereports.handler.platforms;

import dev.mruniverse.purplereports.handler.PlayerHandler;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeePlayerHandler implements PlayerHandler {
    private final Plugin plugin;

    public <T> BungeePlayerHandler(T plugin) {
        this.plugin = (Plugin) plugin;
    }

    @Override
    public void announce(String message) {
        TextComponent component = new TextComponent(message);
        for (ProxiedPlayer player : plugin.getProxy().getPlayers()) {
            player.sendMessage(component);
        }
    }

    @Override
    public void announceOnly(String message, String permission) {
        TextComponent component = new TextComponent(message);
        for (ProxiedPlayer player : plugin.getProxy().getPlayers()) {
            if (player.hasPermission(permission)) {
                player.sendMessage(component);
            }
        }
    }

    @Override
    public int getPlayersSize() {
        return plugin.getProxy().getOnlineCount();
    }

    @Override
    public int getMaxPlayers() {
        return plugin.getProxy().getConfig().getPlayerLimit();
    }
}
