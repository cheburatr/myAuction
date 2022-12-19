package com.gleb_dev.my_auction.controller;

import com.gleb_dev.my_auction.request.LoginRequest;
import com.gleb_dev.my_auction.request.SignUpRequest;
import com.gleb_dev.my_auction.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

// Controller, which is responsible for authentication and registration
@Controller
public class AuthController {

    private UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model){
        return "login";
    }


    @GetMapping("/signup")
    public String showSignUpForm(Model model){
        model.addAttribute("signUpRequest", new SignUpRequest());

        return "signup";
    }

    @PostMapping("/signup")
    public String processSignUp(@Valid @ModelAttribute SignUpRequest signUpRequest, BindingResult bindingResult){

        // Checking the correctness of the data entered
        if(bindingResult.hasErrors()){
            return "signup";
        }

        userService.createUser(signUpRequest);

        return "redirect:/login";
    }
}
