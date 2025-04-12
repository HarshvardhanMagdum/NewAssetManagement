package com.hexa.entity;

import java.time.LocalDate;

public class Reservation {
    private int reservationId;
    private Asset asset;
    private Employee employee;
    private LocalDate reservationDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;

    public Reservation() {}

    public Reservation(int reservationId, Asset asset, Employee employee, LocalDate reservationDate, LocalDate startDate, LocalDate endDate, String status) {
        this.reservationId = reservationId;
        this.asset = asset;
        this.employee = employee;
        this.reservationDate = reservationDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    // Getters and Setters
    public int getReservationId() { return reservationId; }
    public void setReservationId(int reservationId) { this.reservationId = reservationId; }
    public Asset getAsset() { return asset; }
    public void setAsset(Asset asset) { this.asset = asset; }
    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
    public LocalDate getReservationDate() { return reservationDate; }
    public void setReservationDate(LocalDate reservationDate) { this.reservationDate = reservationDate; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

