package com.swift.dating.model;

public class FlexModel {

    private String name;

    private boolean isChecked;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public FlexModel(String name) {
        this.name = name;
    }
}
