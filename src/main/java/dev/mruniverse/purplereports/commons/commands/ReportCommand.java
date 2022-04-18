package dev.mruniverse.purplereports.commons.commands;

import dev.mruniverse.purplereports.commons.PurpleReport;
import dev.mruniverse.purplereports.commons.SlimeFile;
import dev.mruniverse.slimelib.commands.command.Command;
import dev.mruniverse.slimelib.commands.command.SlimeCommand;
import dev.mruniverse.slimelib.commands.sender.Sender;
import dev.mruniverse.slimelib.control.Control;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Command(
        description = "This command will be used to report users.",
        usage = "/<command> <player> <reason>"
)
public class ReportCommand<T> implements SlimeCommand {

    private final PurpleReport<T> plugin;
    private final Control commandSettings;

    public ReportCommand(PurpleReport<T> plugin, Control commandSettings) {
        this.commandSettings = commandSettings;
        this.plugin = plugin;
    }

    @Override
    public String getCommand() {
        return commandSettings.getString("commands.report.main-command", "report");
    }

    @Override
    public List<String> getAliases() {
        return commandSettings.getStringList("commands.report.aliases");
    }

    @Override
    public void execute(Sender sender, @NotNull String commandLabel, String[] args) {
        Control control = plugin.getLoader().getFiles().getControl(SlimeFile.REPORT);

        if (!sender.isPlayer()) {
            for (String message : control.getStringList("console-error")) {
                sender.sendColoredMessage(
                        message
                );
            }
            return;
        }

        if (args.length == 0) {
            for (String message : control.getStringList("usage")) {
                sender.sendColoredMessage(
                        message
                );
            }
        }

        if (args.length == 1) {
            if (!control.getStatus("allow-use-without-reason")) {
                for (String message : control.getStringList("error-no-reason")) {
                    sender.sendColoredMessage(
                            message
                    );
                }
                return;
            }
            //TODO: REPORT WITHOUT REASON
            for (String message : control.getStringList("reported-successfully-without-reason")) {
                sender.sendColoredMessage(
                        message
                );
            }
            for (String message : control.getStringList("report-notification-without-reason")) {
                plugin.getPlayerHandler().announceOnly(
                        message,
                        "purplereports.admin.report-notification"
                );

            }
            return;
        }
        //TODO: REPORT WITH REASON
        for (String message : control.getStringList("reported-successfully-with-reason")) {
            sender.sendColoredMessage(
                    message
            );
        }
        for (String message : control.getStringList("report-notification-with-reason")) {
            plugin.getPlayerHandler().announceOnly(
                    message,
                    "purplereports.admin.report-notification"
            );

        }
    }


}
