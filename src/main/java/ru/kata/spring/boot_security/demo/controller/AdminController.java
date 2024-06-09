package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String showTableUsers(Model model, Principal principal) {
        model.addAttribute("users", userService.getUsersList());
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("listRoles", roleService.getRoles());
        return "users";
    }

    @GetMapping("/add")
    public String createNewUser(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getRoles());
        return "add";
    }

    @PostMapping
    public String addNewUser(@ModelAttribute("user") User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roleService.getSetOfRoles(user.getRoles()));
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
        user.setRoles(roleService.getSetOfRoles(user.getRoles()));
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/show_user")
    public String getInformationUser(@RequestParam("userId") int userId, Model model) {
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "user";
    }
}
