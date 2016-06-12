package com.healify.entities;

public class CheckupEntity {
    private String name;
    private int result;

    public CheckupEntity(String name, int result) {
        this.name = name;
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public int getResult() {
        return result;
    }
}
