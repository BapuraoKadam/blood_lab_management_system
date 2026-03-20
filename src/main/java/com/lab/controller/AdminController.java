package com.lab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.lab.repository.BookingRepository;

@Controller
public class AdminController {
	
	 @Autowired
	 private BookingRepository bookingRepo;

	
    // Show login page
    @GetMapping("/admin/login")
    public String adminLogin() {
        return "admin-login";
    }

    // Login check
    @PostMapping("/admin/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model) {

        if (username.equals("admin") && password.equals("admin123")) {
            return "admin-dashboard";
        } else {
            model.addAttribute("error", "Invalid Credentials");
            return "admin-login";
        }
    }
    
   
    @GetMapping("/admin/analytics")
    public String analytics(Model model) {

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
    
}