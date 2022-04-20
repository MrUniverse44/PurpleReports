package dev.mruniverse.purplereports.commons.commands;

import dev.mruniverse.purplereports.commons.PurpleReport;
import dev.mruniverse.purplereports.commons.SlimeFile;
import dev.mruniverse.purplereports.commons.data.ReportData;
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

            for (ReportData data : plugin.getReportData().getReportsFrom(args[0])) {
                if (data.getAuthor().equalsIgnoreCase(sender.getName())) {
                    cancelReport(control, sender, args[0]);
                }
            }

            if (!control.getStatus("allow-use-without-reason")) {
                for (String message : control.getStringList("error-no-reason")) {
                    sender.sendColoredMessage(
                            replace(
                                    message,
                                    args[0],
                                    sender.getName()
                            )
                    );
                }
                return;
            }

            String reason = control.getString("default-reason", "Cheating");

            plugin.getDatabase().createReport(
                    sender.getName(),
                    args[0],
                    reason
            );

            for (String message : control.getStringList("reported-successfully-without-reason")) {
                sender.sendColoredMessage(
                        replace(
                                message,
                                args[0],
                                sender.getName(),
                                reason
                        )
                );
            }
            for (String message : control.getColoredStringList("report-notification-without-reason")) {
                plugin.getPlayerHandler().announceOnly(
                        replace(
                                message,
                                args[0],
                                sender.getName(),
                                reason
                        ),
                        "purplereports.admin.report-notification"
                );

            }
            return;
        }

        String[] reasonArray = removeFirstArgument(args);

        String reason = convert(reasonArray);

        plugin.getDatabase().createReport(
                sender.getName(),
                args[0],
                reason
        );

        for (String message : control.getStringList("reported-successfully-with-reason")) {
            sender.sendColoredMessage(
                    replace(
                            message,
                            args[0],
                            sender.getName(),
                            reason
                    )
            );
        }
        for (String message : control.getColoredStringList("report-notification-with-reason")) {
            plugin.getPlayerHandler().announceOnly(
                    replace(
                            message,
                            args[0],
                            sender.getName(),
                            reason
                    ),
                    "purplereports.admin.report-notification"
            );

        }
    }

    public void cancelReport(Control control, Sender sender, String reportedUser) {
        for (String message : control.getStringList("error-already-reported")) {
            sender.sendColoredMessage(
                    replace(
                            message,
                            reportedUser,
                            sender.getName()
                    )
            );
        }
    }

    public String replace(String message, String reported, String author, String reason) {
        return message.replace("%user%", reported)
                .replace("%author%", author)
                .replace("%reason%", reason);
    }

    public String replace(String message, String reported, String author) {
        return message.replace("%user%", reported)
                .replace("%author%", author);
    }

    public String convert(String[] args) {
        StringBuilder builder = new StringBuilder();

        for (String argument : args) {
            builder.append(argument);
        }

        return builder.toString();
    }

    private String[] removeFirstArgument(String[] args) {
        String[] newArgs = new String[args.length - 1];

        System.arraycopy(args, 1, newArgs, 0, newArgs.length);

        return newArgs;
    }

}
