package controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fasterxml.jackson.annotation.JsonView;
import domain.Today;

@RestController
public class TodayCtrl {
	@RequestMapping("/api/today")
	public Today list(){
		Today today=new Today();
		return today;
	}
}
