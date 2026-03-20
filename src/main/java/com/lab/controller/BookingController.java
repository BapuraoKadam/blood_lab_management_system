package com.lab.controller;

import com.lab.entity.Booking;
import com.lab.entity.Patient;
import com.lab.entity.Test;
import com.lab.repository.BookingRepository;
import com.lab.repository.TestRepository;

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

    // Show booking page
    @GetMapping("/book/{id}")
    public String bookPage(@PathVariable Long id,
                           Model model,
                           HttpSession session) {

        // ✅ ADD HERE
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }

        Test test = testRepo.findById(id).orElse(null);
        model.addAttribute("test", test);
        return "booking";
    }

    // Save booking
    @PostMapping("/book")
    public String saveBooking(@RequestParam String testName,
                              HttpSession session) {

        // ✅ ADD HERE
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }

        Patient p = (Patient) session.getAttribute("loggedInUser");

        Booking b = new Booking();
        b.setPatientName(p.getName());
        b.setTestName(testName);
        b.setDate(LocalDate.now());
        b.setStatus("Booked");

        bookingRepo.save(b);

        return "success";
    }
    @GetMapping("/admin/bookings")
    public String viewBookings(Model model) {
        model.addAttribute("bookings", bookingRepo.findAll());
        return "admin-bookings";
    }
    
    @GetMapping("/my-bookings")
    public String myBookings(Model model, HttpSession session) {

        // ✅ ADD HERE
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }

        Patient p = (Patient) session.getAttribute("loggedInUser");

        model.addAttribute("bookings",
                bookingRepo.findByPatientName(p.getName()));

        return "my-bookings";
    }
    
}