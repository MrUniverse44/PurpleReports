package dev.mruniverse.purplereports.bungee;

import dev.mruniverse.purplereports.commons.PurpleReport;
import dev.mruniverse.slimelib.SlimePlatform;
import dev.mruniverse.slimelib.input.bungeecord.BungeeInputManager;
import dev.mruniverse.slimelib.logs.platforms.SlimeLoggerBungee;
import net.md_5.bungee.api.plugin.Plugin;

public class PurpleReports extends Plugin {
    private PurpleReport<Plugin> main;

    @Override
    public void onEnable() {
        main = new PurpleReport<>(
                new SlimeLoggerBungee(this),
                SlimePlatform.BUNGEECORD,
                this,
                new BungeeInputManager(this),
                getDataFolder()
        );
    }

    @Override
    public void onDisable() {
        main.getLoader().shutdown();
    }
}
