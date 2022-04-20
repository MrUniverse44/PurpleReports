package dev.mruniverse.purplereports.commons.database;

import dev.mruniverse.purplereports.commons.data.ReportData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportDatabase {
    private final Map<Integer, ReportData> reportDataMap;

    private final Map<String, List<ReportData>> userListMap;

    public ReportDatabase() {
        reportDataMap = new HashMap<>();
        userListMap = new HashMap<>();
    }

    public <T> void add(Database<T> database, int max, int id, ReportData data) {
        if (reportDataMap.size() >= max) {
            database.saveReports(new HashMap<>(reportDataMap));

            reportDataMap.clear();
            userListMap.clear();
        }

        reportDataMap.put(id, data);

        List<ReportData> reports = userListMap.computeIfAbsent(
                data.getReported(),
                T -> new ArrayList<>()
        );

        reports.add(data);

        userListMap.put(data.getReported(), reports);
    }

    public List<ReportData> getReportsFrom(String user) {
        return userListMap.computeIfAbsent(
                user,
                T -> new ArrayList<>()
        );
    }

    public boolean exists(int id) {
        return reportDataMap.containsKey(id);
    }

    public Map<Integer, ReportData> getMap() {
        return reportDataMap;
    }
}
