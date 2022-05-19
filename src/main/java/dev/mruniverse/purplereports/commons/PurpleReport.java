package dev.mruniverse.purplereports.commons;

import dev.mruniverse.purplereports.commons.commands.ReportCommand;
import dev.mruniverse.purplereports.commons.commands.ReportsCommand;
import dev.mruniverse.purplereports.commons.commands.WatchReportsCommand;
import dev.mruniverse.purplereports.commons.database.Database;
import dev.mruniverse.purplereports.commons.database.ReportDatabase;
import dev.mruniverse.purplereports.handler.PlayerHandler;
import dev.mruniverse.slimelib.SlimePlatform;
import dev.mruniverse.slimelib.SlimePlugin;
import dev.mruniverse.slimelib.SlimePluginInformation;
import dev.mruniverse.slimelib.control.Control;
import dev.mruniverse.slimelib.input.InputManager;
import dev.mruniverse.slimelib.loader.BaseSlimeLoader;
import dev.mruniverse.slimelib.loader.DefaultSlimeLoader;
import dev.mruniverse.slimelib.logs.SlimeLog;
import dev.mruniverse.slimelib.logs.SlimeLogs;

import java.io.File;

public class PurpleReport<T> implements SlimePlugin<T> {

    private final SlimePluginInformation slimePluginInformation;

    private final BaseSlimeLoader<T> slimeLoader;

    private final PlayerHandler playerHandler;

    private final ReportDatabase reportData;

    private final SlimePlatform platform;

    private final Database<T> database;

    private final SlimeLogs logs;

    private final File folder;

    private final T plugin;

    public PurpleReport(SlimePlatform platform, T plugin, File dataFolder) {
        this.slimePluginInformation = new SlimePluginInformation(platform, plugin);
        this.playerHandler = PlayerHandler.fromPlatform(platform, plugin);
        this.slimeLoader   = new DefaultSlimeLoader<>(this, InputManager.createInputManager(platform, plugin));
        this.reportData    = new ReportDatabase();
        this.folder        = dataFolder;
        this.platform      = platform;
        this.plugin        = plugin;
        this.logs          = SlimeLog.createLogs(platform, this);

        getLoader().setFiles(SlimeFile.class);

        getLoader().init();

        this.database      = new Database<>(this);

        Control commandSettings = getLoader().getFiles().getControl(SlimeFile.COMMANDS);

        getLoader().getCommands().register(new ReportCommand<>(
                this,
                commandSettings
        ));

        getLoader().getCommands().register(new ReportsCommand<>(
                this,
                commandSettings
        ));

        getLoader().getCommands().register(new WatchReportsCommand<>(
                this,
                commandSettings
        ));
    }

    public ReportDatabase getReportData() {
        return reportData;
    }

    public Database<T> getDatabase() {
        return database;
    }

    @Override
    public SlimePluginInformation getPluginInformation() {
        return slimePluginInformation;
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
