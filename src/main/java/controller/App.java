package controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.mobile.device.Device;

import application.Account;
import application.AccountFactory;

@Controller
public class App {
	@ModelAttribute
	public void userModel(Model model){
		Account user=(new AccountFactory()).getUser();
		model.addAttribute("account", (new AccountFactory()).getUser());
	}
	@RequestMapping("/main/")
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
	@RequestMapping("/")
	public String defaulta(){
		return "redirect:/main/";
	}
	@RequestMapping("/private")
	public String access_private(){
		return "redirect:/main/";
	}
	@RequestMapping("/about")
	public String about(){
		return "about";
	}
	@RequestMapping("/test")
	public String test(){
		return "test";
	}
	@RequestMapping(value = "/admin**", method = RequestMethod.GET)
	public ModelAndView adminPage() {
 
		ModelAndView model = new ModelAndView();
		model.addObject("title", "Spring Security Custom Login Form");
		model.addObject("message", "This is protected page!");
		model.setViewName("admin");
 
		return model;
 
	}
 
	//Spring Security see this :
	@RequestMapping(value = "/signin", method = RequestMethod.GET)
	public ModelAndView login(
		@RequestParam(value = "error", required = false) String error,
		@RequestParam(value = "signout", required = false) String signout) {
 
		ModelAndView model = new ModelAndView();
		if (error != null) {
			model.addObject("error", "Invalid username and password!");
		}
 
		if (signout != null) {
			model.addObject("msg", "You've been signed out successfully.");
		}
		model.setViewName("signin");
 
		return model;
 
	}
	@RequestMapping(value = "/register")
	public ModelAndView register(
		@RequestParam(value = "error", required = false) String error,
		@RequestParam(value = "signout", required = false) String signout) {
 
		ModelAndView model = new ModelAndView();
		if (error != null) {
			model.addObject("error", "Invalid username and password!");
		}
 
		if (signout != null) {
			model.addObject("msg", "You've been signed out successfully.");
		}
		model.setViewName("register");
 
		return model;
 
	}
}
