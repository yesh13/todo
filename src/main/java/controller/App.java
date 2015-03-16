package controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;
import org.springframework.mobile.device.Device;

@Controller
public class App {
	@RequestMapping("/")
	public ModelAndView main(Device device){
		ModelAndView model;
		if(device.isNormal()) {
			model=new ModelAndView("main");
			model.addObject("msg", "hello desk");
		}
		else{
			model=new ModelAndView("main-mobile");
			model=model.addObject("msg", "hello mobile");
		}
		return model;
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
	@RequestMapping("/test")
	public String test(){
		return "test";
	}
}
