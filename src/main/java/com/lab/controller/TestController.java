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

    // 🔐 COMMON METHOD
    private boolean isAdminLoggedIn(HttpSession session) {
        return session.getAttribute("admin") != null;
    }

    // ================= ADD TEST PAGE =================
    @GetMapping("/admin/add-test")
    public String showAddTest(Model model, HttpSession session) {

        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        model.addAttribute("test", new Test());
        return "add-test";
    }

    // ================= SAVE TEST =================
    @PostMapping("/admin/add-test")
    public String saveTest(@RequestParam String testName,
                           @RequestParam double price,
                           @RequestParam String description,
                           HttpSession session,
                           Model model) {

        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        Test t = new Test();
        t.setTestName(testName);
        t.setPrice(price);
        t.setDescription(description);

        repo.save(t);

        model.addAttribute("msg", "Test added successfully!");
        return "add-test";
    }

    // ================= EDIT PAGE =================
    @GetMapping("/admin/edit-test/{id}")
    public String showEditPage(@PathVariable Long id,
                              Model model,
                              HttpSession session) {

        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        Test test = repo.findById(id).orElse(null);
        model.addAttribute("test", test);

        return "edit-test";
    }

    // ================= UPDATE TEST =================
    @PostMapping("/admin/update-test")
    public String updateTest(@RequestParam Long id,
                            @RequestParam String testName,
                            @RequestParam double price,
                            @RequestParam String description,
                            HttpSession session) {

        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        Test existing = repo.findById(id).orElse(null);

        if (existing == null) {
            return "redirect:/admin/tests";
        }

        existing.setTestName(testName);
        existing.setPrice(price);
        existing.setDescription(description);

        repo.save(existing);

        return "redirect:/admin/tests";
    }

    // ================= VIEW ALL TESTS =================
    @GetMapping("/admin/tests")
    public String adminTests(Model model, HttpSession session) {

        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        model.addAttribute("tests", repo.findAll());
        return "admin-tests";
    }

    // ================= DELETE TEST =================
    @PostMapping("/admin/delete-test/{id}")
    public String deleteTest(@PathVariable Long id, HttpSession session) {

        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        repo.deleteById(id);

        return "redirect:/admin/tests";
    }

    // ================= PATIENT VIEW =================
    @GetMapping("/tests")
    public String viewTests(@RequestParam(required = false) String keyword,
                           Model model,
                           HttpSession session) {

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