package controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
public class App {
	@RequestMapping("/")
	public String main(){
		return "main";
	}
	@RequestMapping("/add")
	public String add(){
		return "add";
	}
	@RequestMapping("/todo")
	public String todo(){
		return "todo";
	}
	@RequestMapping("/about")
	public String about(){
		return "about";
	}
}
