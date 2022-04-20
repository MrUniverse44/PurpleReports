package dev.mruniverse.purplereports.commons.commands;

import dev.mruniverse.purplereports.commons.PurpleReport;
import dev.mruniverse.slimelib.commands.command.Command;
import dev.mruniverse.slimelib.commands.command.SlimeCommand;
import dev.mruniverse.slimelib.commands.sender.Sender;
import dev.mruniverse.slimelib.control.Control;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Command
public class ReportsCommand<T> implements SlimeCommand {

    private final PurpleReport<T> plugin;
    private final Control commandSettings;

    public ReportsCommand(PurpleReport<T> plugin, Control commandSettings) {
        this.commandSettings = commandSettings;
        this.plugin = plugin;
    }

    @Override
    public String getCommand() {
        return commandSettings.getString("commands.reports.main-command", "reports");
    }

    @Override
    public List<String> getAliases() {
        return commandSettings.getStringList("commands.reports.aliases");
    }

    @Override
    public void execute(@NotNull Sender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (args.length == 0) {
            //TODO
        }
    }
}
