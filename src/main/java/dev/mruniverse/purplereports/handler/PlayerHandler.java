package dev.mruniverse.purplereports.handler;

import dev.mruniverse.purplereports.handler.platforms.BungeePlayerHandler;
import dev.mruniverse.purplereports.handler.platforms.SpigotPlayerHandler;
import dev.mruniverse.purplereports.handler.platforms.VelocityPlayerHandler;
import dev.mruniverse.slimelib.SlimePlatform;

public interface PlayerHandler {

    /**
     * Announce only to all players
     * @param message The message to be sent
     */
    void announce(String message);

    /**
     * Announce only to specific players with a permission
     * @param message The message to be sent
     * @param permission the permission required
     */
    void announceOnly(String message, String permission);

    /**
     * Get the amount of players online in the server
     * @return int Players Size
     */
    int getPlayersSize();

    /**
     * Get the amount of max players in the server
     * @return int Max Players Size
     */
    int getMaxPlayers();

    static <T> PlayerHandler fromPlatform(SlimePlatform platform, T plugin) {
        switch (platform) {
            case SPIGOT:
                return new SpigotPlayerHandler(plugin);
            case VELOCITY:
                return new VelocityPlayerHandler(plugin);
            default:
            case BUNGEECORD:
                return new BungeePlayerHandler(plugin);
        }
    }

}
