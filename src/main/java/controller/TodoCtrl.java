package controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import application.Account;
import dao.AccountDao;
import domain.Todo;

@RestController
public class TodoCtrl {
	@RequestMapping(value="/api/todo", method=RequestMethod.GET)
	public Todo list(){
		Todo todo=Todo.get();
		return todo;
	}
	@RequestMapping(value="/api/todo", method=RequestMethod.POST)
	public Todo add(@RequestParam("action") String act,@RequestParam("name") String name){
		System.out.println("post received");
		Todo todo=Todo.get();
		switch(act){
		case "add":
			todo.addAct(name);
			break;
		case "rm":
			todo.rmAct(name);
		}
		return todo;
	}
	@RequestMapping(value="/api/account", method=RequestMethod.GET)
	public Account get(){
		Account todo=(new AccountDao()).getByName("shuhao");
		return todo;
	}

}
