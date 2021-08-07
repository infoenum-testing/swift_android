package com.swiftdating.app.model;

public class DateModel {
    public int getDate() {
        return date;
    }

    public int getMonth() {
        return month;
    }
    public int getYear() {
        return year;
    }

    public boolean isChecked() {
        return isChecked;
    }

    private int date;
    private int month;
    private int year;

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    boolean isChecked;

    public DateModel(int date, int month, boolean isChecked,int year) {
        this.date = date;
        this.month = month;
        this.year = year;
        this.isChecked = isChecked;
    }
}
