package com.hexa.entity;

import java.time.LocalDate;

public class MaintenanceRecord {
    private int maintenanceId;
    private Asset asset; // Class-level relationship
    private LocalDate maintenanceDate;
    private String description;
    private double cost;

    public MaintenanceRecord() {}

    public MaintenanceRecord(int maintenanceId, Asset asset, LocalDate maintenanceDate, String description, double cost) {
        this.maintenanceId = maintenanceId;
        this.asset = asset;
        this.maintenanceDate = maintenanceDate;
        this.description = description;
        this.cost = cost;
    }

    // Getters and Setters
    public int getMaintenanceId() { return maintenanceId; }
    public void setMaintenanceId(int maintenanceId) { this.maintenanceId = maintenanceId; }
    public Asset getAsset() { return asset; }
    public void setAsset(Asset asset) { this.asset = asset; }
    public LocalDate getMaintenanceDate() { return maintenanceDate; }
    public void setMaintenanceDate(LocalDate maintenanceDate) { this.maintenanceDate = maintenanceDate; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }
}