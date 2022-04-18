package dev.mruniverse.purplereports.spigot;

import dev.mruniverse.purplereports.commons.PurpleReport;
import dev.mruniverse.slimelib.SlimePlatform;
import dev.mruniverse.slimelib.input.spigot.SpigotInputManager;
import dev.mruniverse.slimelib.logs.platforms.SlimeLoggerSpigot;
import org.bukkit.plugin.java.JavaPlugin;

public final class PurpleReports extends JavaPlugin {
    private PurpleReport<JavaPlugin> main;

    @Override
    public void onEnable() {
        main = new PurpleReport<>(
                new SlimeLoggerSpigot(this),
                SlimePlatform.BUNGEECORD,
                this,
                new SpigotInputManager(this),
                getDataFolder()
        );
    }

    @Override
    public void onDisable() {
        main.getLoader().shutdown();
    }
}
