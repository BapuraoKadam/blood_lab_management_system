package com.lab.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String patientName;
    private String testName;

    private LocalDate bookingDate;     // ✅ today (auto)
    private LocalDate preferredDate;   // ✅ selected by user

    private String status;
    private String mobile;
    private String address;

    // ===== Getters & Setters =====

    public Long getId() {
        return id;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getTestName() {
        return testName;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public LocalDate getPreferredDate() {
        return preferredDate;
    }

    public String getStatus() {
        return status;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public void setPreferredDate(LocalDate preferredDate) {
        this.preferredDate = preferredDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Booking [id=" + id +
                ", patientName=" + patientName +
                ", testName=" + testName +
                ", bookingDate=" + bookingDate +
                ", preferredDate=" + preferredDate +
                ", status=" + status +
                ", mobile=" + mobile +
                ", address=" + address + "]";
    }
}