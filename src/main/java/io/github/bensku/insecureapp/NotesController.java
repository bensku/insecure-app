package io.github.bensku.insecureapp;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class NotesController {

	private String note;
	
	@GetMapping("/")
	public String index(Principal login, Model model, @RequestParam(required = false) String newNote) {
		if (newNote != null) {
			note = newNote;
		}
		model.addAttribute("note", note);
		model.addAttribute("user", login.getName());
		return "index";
	}
}
