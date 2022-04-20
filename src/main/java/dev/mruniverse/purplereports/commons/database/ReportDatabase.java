package dev.mruniverse.purplereports.commons.database;

import dev.mruniverse.purplereports.commons.data.ReportData;

import java.util.HashMap;
import java.util.Map;

public class ReportDatabase {
    private final Map<Integer, ReportData> reportDataMap;

    public ReportDatabase() {
        reportDataMap = new HashMap<>();
    }

    public void add(int id, ReportData data) {
        reportDataMap.put(id, data);
    }

    public boolean exists(int id) {
        return reportDataMap.containsKey(id);
    }

    public Map<Integer, ReportData> getMap() {
        return reportDataMap;
    }
}
