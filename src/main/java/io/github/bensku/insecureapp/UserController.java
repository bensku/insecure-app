package io.github.bensku.insecureapp;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private DataSource db;
	
    @GetMapping("/register")
    public String registerForm(Model model) {
        return "register";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        return "login";
    }
    
    @PostMapping("/register")
    public String register(Model model, @RequestParam String name, @RequestParam String password) throws SQLException {
    	try (Connection conn = db.getConnection()) {
    		conn.prepareStatement("INSERT INTO users (name, password) VALUES ("
    				+ name + ", " + passwordEncoder.encode(password) + ")").executeUpdate();
    	}
    	return "redirect:/";
    }
}
