package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;

@Controller
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(value = "/admin")
    public String showTableUsers(Model model) {
        model.addAttribute("users", userService.getUsersList());
        return "users";
    }

    @GetMapping("/admin/add")
    public String newUser(@ModelAttribute("new_user") User user, Model model) {
        model.addAttribute("roles", roleService.getRoles());
        return "add";
    }

    @PostMapping(value = "/admin")
    public String addNewUser(@ModelAttribute("new_user") User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roleService.getSetOfRoles(user.getRoles()));
        userService.addUser(user);
        return "redirect:/admin";
    }

    @PostMapping(value = "/admin/delete")
    public String deleteUser(@RequestParam("userId") int userId) {
        userService.deleteUser(userId);
        return "redirect:/admin";
    }

    @GetMapping("/admin/update")
    public String updateUserForm(@RequestParam("userId") int userId, Model model) {
        model.addAttribute("user", userService.getUserById(userId));
        model.addAttribute("roles", roleService.getRoles());
        return "update";
    }

    @PostMapping("/admin/update")
    public String updateUserSubmit(@ModelAttribute User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roleService.getSetOfRoles(user.getRoles()));
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/show_user")
    public String info(@RequestParam("userId") int userId, Model model) {
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/user")
    public String getUserPage(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("listRoles", roleService.getRoles());
        return "user";
    }
}
