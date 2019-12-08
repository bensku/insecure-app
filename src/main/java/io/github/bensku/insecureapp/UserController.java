package io.github.bensku.insecureapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
    @GetMapping("/register")
    public String registerForm(Model model) {
        return "register";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        return "login";
    }
}
