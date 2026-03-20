package com.lab.controller;

import com.lab.entity.Test;
import com.lab.repository.TestRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TestController {

    @Autowired
    private TestRepository repo;

    // Admin: Show Add Test Page
    @GetMapping("/admin/add-test")
    public String showAddTest(Model model) {
        model.addAttribute("test", new Test());
        return "add-test";
    }

    // Admin: Save Test
    @PostMapping("/admin/add-test")
    public String saveTest(@ModelAttribute Test test) {
        repo.save(test);
        return "redirect:/admin/add-test";
    }

    // Patient: View Tests
    @GetMapping("/tests")
    public String viewTests(@RequestParam(required = false) String keyword,
                            Model model,
                            HttpSession session) {

        // ✅ ADD HERE
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }

        List<Test> tests;

        if (keyword != null && !keyword.isEmpty()) {
            tests = repo.findByTestNameContainingIgnoreCase(keyword);
        } else {
            tests = repo.findAll();
        }

        model.addAttribute("tests", tests);

        return "tests";
    }
}