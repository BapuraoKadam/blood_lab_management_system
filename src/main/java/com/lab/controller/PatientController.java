package com.lab.controller;

import com.lab.entity.Patient;
import com.lab.repository.PatientRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PatientController {

    @Autowired
    private PatientRepository repo;

    // Show Register Page
    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("patient", new Patient());
        return "register";
    }

    // Save Patient
    @PostMapping("/register")
    public String registerPatient(@ModelAttribute Patient patient) {
        repo.save(patient);
        return "login";
    }

    // Show Login Page
    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    // Login Logic
    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        Model model,
                        HttpSession session) {

        Patient p = repo.findByEmail(email);

        if (p != null && p.getPassword().equals(password)) {

            // ✅ Store patient in session
            session.setAttribute("loggedInUser", p);

            return "dashboard";
        } else {
            model.addAttribute("error", "Invalid Email or Password");
            return "login";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "login";
    }
}