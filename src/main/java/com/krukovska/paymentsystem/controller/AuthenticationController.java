package com.krukovska.paymentsystem.controller;

import com.krukovska.paymentsystem.persistence.model.User;
import com.krukovska.paymentsystem.service.PasswordService;
import com.krukovska.paymentsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import static com.krukovska.paymentsystem.util.LabelConstants.ERROR_LABEL;
import static com.krukovska.paymentsystem.util.LabelConstants.LOGIN_LABEL;

@Controller
public class AuthenticationController {

    private final UserService userService;
    private final PasswordService passwordService;

    @Autowired
    public AuthenticationController(UserService userService, PasswordService passwordService) {
        this.userService = userService;
        this.passwordService = passwordService;
    }

    @GetMapping("/")
    public String getMainPage() {
        return "index";
    }


    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute(ERROR_LABEL, null);
        return LOGIN_LABEL;
    }

    @PostMapping("/login-processing")
    public String loginUser(@ModelAttribute User user, Model model) {

        var foundUser = userService.findUserByEmail(user.getEmail());

        if (foundUser == null) {
            model.addAttribute(ERROR_LABEL, "User doesn't exists");
            return LOGIN_LABEL;
        }

        if (!passwordService.compareRawAndEncodedPassword(user.getPassword(), (foundUser.getPassword()))) {
            model.addAttribute(ERROR_LABEL, "Wrong password");
            return LOGIN_LABEL;
        }

        return "redirect:/";
    }

}
