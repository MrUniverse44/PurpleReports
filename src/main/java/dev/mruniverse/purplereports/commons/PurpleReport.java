package dev.mruniverse.purplereports.commons;

import dev.mruniverse.purplereports.commons.commands.ReportCommand;
import dev.mruniverse.purplereports.handler.PlayerHandler;
import dev.mruniverse.slimelib.SlimePlatform;
import dev.mruniverse.slimelib.SlimePlugin;
import dev.mruniverse.slimelib.input.InputManager;
import dev.mruniverse.slimelib.loader.BaseSlimeLoader;
import dev.mruniverse.slimelib.loader.DefaultSlimeLoader;
import dev.mruniverse.slimelib.logs.SlimeLogs;

import java.io.File;

public class PurpleReport<T> implements SlimePlugin<T> {

    private final BaseSlimeLoader<T> slimeLoader;

    private final PlayerHandler playerHandler;

    private final SlimePlatform platform;

    private final SlimeLogs logs;

    private final File folder;

    private final T plugin;

    public PurpleReport(SlimeLogs logs, SlimePlatform platform, T plugin, InputManager inputManager, File dataFolder) {
        this.playerHandler = PlayerHandler.fromPlatform(platform, plugin);
        this.slimeLoader   = new DefaultSlimeLoader<>(this, inputManager);
        this.folder        = dataFolder;
        this.platform      = platform;
        this.plugin        = plugin;
        this.logs          = logs;

        getLoader().setFiles(SlimeFile.class);

        getLoader().init();

        getLoader().getCommands().register(new ReportCommand<>(
                this,
                getLoader().getFiles().getControl(SlimeFile.COMMANDS)
        ));
    }


    @Override
    public SlimePlatform getServerType() {
        return platform;
    }

    public PlayerHandler getPlayerHandler() {
        return playerHandler;
    }

    @Override
    public SlimeLogs getLogs() {
        return logs;
    }

    @Override
    public BaseSlimeLoader<T> getLoader() {
        return slimeLoader;
    }

    @Override
    public T getPlugin() {
        return plugin;
    }

    @Override
    public void reload() {
        slimeLoader.reload();
    }

    @Override
    public File getDataFolder() {
        return folder;
    }
}
