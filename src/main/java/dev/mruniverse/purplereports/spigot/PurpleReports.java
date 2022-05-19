package dev.mruniverse.purplereports.spigot;

import dev.mruniverse.purplereports.commons.PurpleReport;
import dev.mruniverse.slimelib.SlimePlatform;
import org.bukkit.plugin.java.JavaPlugin;

public final class PurpleReports extends JavaPlugin {
    private PurpleReport<JavaPlugin> main;

    @Override
    public void onEnable() {
        main = new PurpleReport<>(
                SlimePlatform.SPIGOT,
                this,
                getDataFolder()
        );
    }

    @Override
    public void onDisable() {
        main.getLoader().shutdown();
    }
}
