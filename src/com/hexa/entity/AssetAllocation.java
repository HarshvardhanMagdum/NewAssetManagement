package com.hexa.entity;


import java.time.LocalDate;

public class AssetAllocation {
    private int allocationId;
    private Asset asset;
    private Employee employee;
    private LocalDate allocationDate;
    private LocalDate returnDate;

    public AssetAllocation() {}

    public AssetAllocation(int allocationId, Asset asset, Employee employee, LocalDate allocationDate, LocalDate returnDate) {
        this.allocationId = allocationId;
        this.asset = asset;
        this.employee = employee;
        this.allocationDate = allocationDate;
        this.returnDate = returnDate;
    }

    // Getters and Setters
    public int getAllocationId() { return allocationId; }
    public void setAllocationId(int allocationId) { this.allocationId = allocationId; }
    public Asset getAsset() { return asset; }
    public void setAsset(Asset asset) { this.asset = asset; }
    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
    public LocalDate getAllocationDate() { return allocationDate; }
    public void setAllocationDate(LocalDate allocationDate) { this.allocationDate = allocationDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
}

