package dev.mruniverse.purplereports.commons.data;

public class ReportData {
    private final String reported;

    private final String author;

    private final String reason;

    private final int id;

    public ReportData(int id, String author, String reason, String reported) {
        this.reported = reported;
        this.author = author;
        this.reason = reason;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getReported() {
        return reported;
    }

    public String getReason() {
        return reason;
    }
}
