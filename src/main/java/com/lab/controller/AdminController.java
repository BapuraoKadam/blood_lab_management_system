package com.lab.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.lab.repository.BookingRepository;

@Controller
public class AdminController {

    @Autowired
    private BookingRepository bookingRepo;

    // ================= LOGIN PAGE =================
    @GetMapping("/admin/login")
    public String adminLogin() {
        return "admin-login";
    }

    // ================= LOGIN CHECK =================
    @PostMapping("/admin/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        if (username.equals("admin") && password.equals("admin123")) {

            // ✅ STORE ADMIN SESSION
            session.setAttribute("admin", username);

            return "redirect:/admin/dashboard"; // ✅ redirect (important)
        } else {
            model.addAttribute("error", "Invalid Credentials");
            return "admin-login";
        }
    }

    // ================= DASHBOARD =================
    @GetMapping("/admin/dashboard")
    public String dashboard(HttpSession session, Model model) {

        // 🔒 SESSION CHECK
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        long total = bookingRepo.count();

        long completed = bookingRepo.findAll().stream()
                .filter(b -> b.getStatus().equals("Report Ready"))
                .count();

        long pending = total - completed;

        model.addAttribute("total", total);
        model.addAttribute("completed", completed);
        model.addAttribute("pending", pending);

        return "admin-dashboard";
    }

    // ================= ANALYTICS =================
    @GetMapping("/admin/analytics")
    public String analytics(HttpSession session, Model model) {

        // 🔒 SESSION CHECK
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        long total = bookingRepo.count();

        long completed = bookingRepo.findAll().stream()
                .filter(b -> b.getStatus().equals("Report Ready"))
                .count();

        long pending = total - completed;

        model.addAttribute("total", total);
        model.addAttribute("completed", completed);
        model.addAttribute("pending", pending);

        return "analytics";
    }

    // ================= LOGOUT =================
    @GetMapping("/admin/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login"; // ✅ correct
    }
}