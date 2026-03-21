package com.lab.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index"; // loads index.html from templates
    }
    @GetMapping("/contact")
    public String contactPage() {
        return "contact";
    }
    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }
    @PostMapping("/contact")
    public String sendMessage(@RequestParam String name,
                              @RequestParam String email,
                              @RequestParam String message) {

        System.out.println(name + " " + email + " " + message);
        return "contact";
    }
}