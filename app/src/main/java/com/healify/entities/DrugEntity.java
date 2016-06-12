package com.healify.entities;

public class DrugEntity {
    private String name;
    private int dose;
    private String unit;

    public DrugEntity(String name, int dose, String unit) {
        this.name = name;
        this.dose = dose;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public int getDose() {
        return dose;
    }

    public String getUnit() {
        return unit;
    }
}
