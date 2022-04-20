package dev.mruniverse.purplereports.commons.database;

import dev.mruniverse.purplereports.commons.PurpleReport;
import dev.mruniverse.purplereports.commons.SlimeFile;
import dev.mruniverse.purplereports.commons.data.ReportData;
import dev.mruniverse.slimelib.control.Control;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

@SuppressWarnings("unused")
public class Database<T> {

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ignored) {}
    }

    private final PurpleReport<T> plugin;

    private Connection connection;
    private PreparedStatement reportData;
    private PreparedStatement reportId;
    private PreparedStatement submitReport;
    private PreparedStatement removeReport;
    private PreparedStatement createReport;

    public Database(PurpleReport<T> plugin) {
        this.plugin = plugin;
    }

    public void initialize() throws SQLException {
        final Control configuration = plugin.getLoader().getFiles().getControl(SlimeFile.MYSQL);

        init(
                configuration.getString("mysql.host"),
                configuration.getInt("mysql.port"),
                configuration.getString("mysql.data"),
                configuration.getString("mysql.user"),
                configuration.getString("mysql.pass")
        );
    }

    private void init(String hostname, int port, String database, String user, String password) throws SQLException {
        final InputStream schemaIn = Database.class.getResourceAsStream("/schema.sql");

        if (schemaIn == null) {
            throw new IllegalStateException("schema.sql not found");
        }

        final String schema;

        try {
            schema = new String(readAllBytes(schemaIn), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        final Connection connection = DriverManager.getConnection(
                String.format("jdbc:mysql://%s:%d/%s?autoReconnect=true&useSSL=false", hostname, port, database),
                user, password
        );

        for (String query : schema.split(";")) {
            if (query.trim().isEmpty()) {
                continue;
            }
            try (final Statement statement = connection.createStatement()) {
                statement.execute(query);
            }
        }

        this.connection = connection;
        this.reportData = connection.prepareStatement("select * from `reports` where `reportId` = ?");
        this.reportId = connection.prepareStatement("select * from `reports` where `reportedPlayer` = ? and `reporter` = ? and `reason` = ?");
        this.removeReport = connection.prepareStatement("delete from `reports` where `reportId` = ?");
        this.submitReport = connection.prepareStatement("replace into `reports` (reportId, reportedPlayer, reporter, reason) values (?,?,?,?)");
        this.createReport = connection.prepareStatement("insert into `reports` (reportedPlayer, reporter, reason) values (?,?,?)");
    }

    public synchronized int getReportId(String reportedUser, String author, String reason) {
        try {
            reportId.clearParameters();
            reportId.setString(1, reportedUser);
            reportId.setString(2, author);
            reportId.setString(3, reason);

            try (ResultSet rs = reportData.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("reportId");
                }
            }
        } catch (SQLException e) {
            plugin.getLogs().error("Failed to fetch report data for user " + reportedUser, e);
        }
        return 999;
    }

    public synchronized ReportData getReportData(int id) {
        try {
            reportData.clearParameters();
            reportData.setInt(1, id);

            try (ResultSet rs = reportData.executeQuery()) {
                if (rs.next()) {
                    return new ReportData(
                            rs.getInt(id),
                            rs.getString("reporter"),
                            rs.getString("reason"),
                            rs.getString("reportedPlayer")
                    );
                }
            }
        } catch (SQLException e) {
            plugin.getLogs().error("Failed to fetch report data for id " + id, e);
        }
        return null;
    }

    private synchronized void saveReportData(final ReportData data) {
        try {
            submitReport.clearParameters();
            submitReport.setInt(1, data.getId());
            submitReport.setString(2, data.getReported());
            submitReport.setString(3, data.getAuthor());
            submitReport.setString(4, data.getReason());
            submitReport.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogs().error("Failed to save report data for id " + data.getId());
            e.printStackTrace();
        }
    }

    public void createReport(String author, String reportedUser, String reason) {
        try {
            createReport.clearParameters();
            createReport.setString(1, reportedUser);
            createReport.setString(2, author);
            createReport.setString(3, reason);

            createReport.executeUpdate();

            int id = getReportId(reportedUser, author, reason);
            int max = plugin.getLoader().getFiles().getControl(SlimeFile.SETTINGS).getInt("settings.reports.max-reports-to-clear-cache", 35);

            plugin.getReportData().add(
                    plugin.getDatabase(),
                    max,
                    id,
                    new ReportData(
                            id,
                            author,
                            reason,
                            reportedUser
                    )
            );
        } catch (SQLException e) {
            plugin.getLogs().error("Failed to fetch report data for player " + reportedUser, e);
        }
    }

    public void saveReports(Map<Integer, ReportData> reportDataMap) {
        for (ReportData data : reportDataMap.values()) {
            saveReportData(data);
        }
    }

    public void close() {
        closeSilent(submitReport);
        closeSilent(reportData);
        closeSilent(removeReport);
        closeSilent(connection);
    }

    private void closeSilent(AutoCloseable closeable) {
        try {
            if (closeable != null) { closeable.close(); }
        } catch (Exception ignored) {}
    }

    private static byte[] readAllBytes(InputStream in) throws IOException {
        ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int readCount;
        while ((readCount = in.read(buffer)) != -1) {
            byteArrayOutput.write(buffer, 0, readCount);
        }
        return byteArrayOutput.toByteArray();
    }
}