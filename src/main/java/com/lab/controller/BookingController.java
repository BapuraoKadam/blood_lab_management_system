package com.lab.controller;

import com.lab.entity.Booking;
import com.lab.entity.Patient;
import com.lab.entity.Test;
import com.lab.repository.BookingRepository;
import com.lab.repository.TestRepository;
import com.lab.service.NotificationService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class BookingController {

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private TestRepository testRepo;
    
    @Autowired
    private NotificationService notificationService;

    // 🔐 COMMON METHOD
    private boolean isAdminLoggedIn(HttpSession session) {
        return session.getAttribute("admin") != null;
    }

    // ================= PATIENT: BOOK PAGE =================
    @GetMapping("/book/{id}")
    public String bookPage(@PathVariable Long id,
                           Model model,
                           HttpSession session) {

        // 🔒 Patient check
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }

        Test test = testRepo.findById(id).orElse(null);
        model.addAttribute("test", test);
        return "booking";
    }

    // ================= PATIENT: SAVE BOOKING =================
    @PostMapping("/book")
    public String saveBooking(@RequestParam String testName,
                              @RequestParam String mobile,
                              @RequestParam String address,
                              @RequestParam String preferredDate,  // ✅ GET FROM FORM
                              HttpSession session) {

        // 🔒 Patient check
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }

        Patient p = (Patient) session.getAttribute("loggedInUser");

        Booking b = new Booking();
        b.setPatientName(p.getName());
        b.setTestName(testName);
        b.setMobile(mobile);
        b.setAddress(address);

        // ✅ Convert String → LocalDate
        b.setPreferredDate(LocalDate.parse(preferredDate));

        // ✅ Set today's date separately
        b.setBookingDate(LocalDate.now());

        b.setStatus("Booked");

        bookingRepo.save(b);

        // 🔔 Notification
        notificationService.createNotification(
            "New booking: " + p.getName() + " booked " + testName
        );

        return "success";
    }

    // ================= ADMIN: VIEW BOOKINGS =================
    @GetMapping("/admin/bookings")
    public String viewBookings(Model model, HttpSession session) {

        // 🔒 ADMIN CHECK (FIXED)
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        model.addAttribute("bookings", bookingRepo.findAll());
        return "admin-bookings";
    }

    // ================= PATIENT: MY BOOKINGS =================
    @GetMapping("/my-bookings")
    public String myBookings(Model model, HttpSession session) {

        // 🔒 Patient check
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }

        Patient p = (Patient) session.getAttribute("loggedInUser");

        model.addAttribute("bookings",
                bookingRepo.findByPatientName(p.getName()));

        return "my-bookings";
    }
}