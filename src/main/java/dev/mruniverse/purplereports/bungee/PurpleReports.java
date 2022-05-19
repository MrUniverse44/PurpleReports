package dev.mruniverse.purplereports.bungee;

import dev.mruniverse.purplereports.commons.PurpleReport;
import dev.mruniverse.slimelib.SlimePlatform;
import net.md_5.bungee.api.plugin.Plugin;

public class PurpleReports extends Plugin {
    private PurpleReport<Plugin> main;

    @Override
    public void onEnable() {
        main = new PurpleReport<>(
                SlimePlatform.BUNGEECORD,
                this,
                getDataFolder()
        );
    }

    @Override
    public void onDisable() {
        main.getLoader().shutdown();
    }
}
