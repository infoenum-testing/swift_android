package com.swiftdating.app.model.requestmodel;

public class ReportRequestModel {

    private int reportedfor;
    private String reason;

    public ReportRequestModel(int reportedfor, String reason) {
        this.reportedfor = reportedfor;
        this.reason = reason;
    }

    public int getReportedfor() {
        return reportedfor;
    }

    public String getReason() {
        return reason;
    }
}
