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

@Controller
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

    @GetMapping(value = "/admin")
    public String showTableUsers(Model model) {
        model.addAttribute("users", userService.getUsersList());
        return "users";
    }

    @GetMapping("/admin/add")
    public String createNewUser(@ModelAttribute("new_user") User user, Model model) {
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
    public String getInformationUser(@RequestParam("userId") int userId, Model model) {
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "user";
    }
}
