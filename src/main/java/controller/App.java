package controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
public class App {
	@RequestMapping("/")
	public String main(){
		return "main";
	}
	@RequestMapping("/about")
	public String about(){
		return "about";
	}
}
