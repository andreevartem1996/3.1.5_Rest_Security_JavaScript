package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String showTableUsers(Model model, Principal principal) {
        model.addAttribute("users", userService.getUsersList());
        model.addAttribute("user", userService.findByUsername(principal.getName()));
        model.addAttribute("listRoles", roleService.getRoles());
        return "users";
    }

    @GetMapping("/add")
    public String createNewUser(Model model, Principal principal) {
        model.addAttribute("user", userService.findByUsername(principal.getName()));
        model.addAttribute("roles", roleService.getRoles());
        return "add";
    }

    @PostMapping
    public String addNewUser(@ModelAttribute("user") User user) {
        userService.addUser(user);
        return "redirect:/admin";
    }

    @PostMapping(value = "/delete")
    public String deleteUser(@RequestParam("userId") int userId) {
        userService.deleteUser(userId);
        return "redirect:/admin";
    }

    @PostMapping("/update")
    public String updateUserSubmit(@ModelAttribute User user) {
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/show_user")
    public String getInformationUser(@RequestParam("userId") int userId, Model model) {
        model.addAttribute("user", userService.getUserById(userId));
        return "user";
    }
}
