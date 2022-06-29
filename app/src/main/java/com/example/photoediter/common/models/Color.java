package com.example.photoediter.common.models;

public class Color {
    private String color;
    private int type;

    public Color() {
    }

    public Color(String color, int type) {
        this.color = color;
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
