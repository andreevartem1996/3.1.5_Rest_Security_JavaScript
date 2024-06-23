package ru.kata.spring.boot_security.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
public class Controller {

    @GetMapping("/admin")
    public String getAdminPage(){
        return "admin";
    }


    @GetMapping("/user")
    public String getUserPage(){
        return "user";
    }

    @GetMapping("/")
    public String indexPage(){
        return "redirect:/login";
    }
}
