package com.spring;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class IndexController {
	@GetMapping("/")
	public String init() {
		return "redirect:/loginPage";
	}
}
