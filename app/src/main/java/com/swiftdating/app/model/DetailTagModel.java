package com.swiftdating.app.model;

public class DetailTagModel {
    private String name;
    private int image;

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

    public DetailTagModel(String name,int image) {
        this.name = name;
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "{"+ name + '\'' +'}';
    }
}