package com.example.parcial2.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/")
    public String inicio() {
        return "redirect:/consultas";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/error/403")
    public String accesoDenegado() {
        return "error/403";
    }
}
