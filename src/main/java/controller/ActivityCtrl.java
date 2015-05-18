package controller;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import util.CalendarUtil;
import application.Account;
import application.AccountFactory;
import application.ActivityDTO;
import application.ActivityService;
import application.gson.GsonFactory;
import domain.Activity;
import domain.ActivityFactory;
import domain.Appointment;
import domain.BasicActivity;
import domain.RequestFilter;
import domain.Task;

@RestController
public class ActivityCtrl {

	@RequestMapping(value="/api/activity/newappt/{parent}", method=RequestMethod.GET)
	public String addappt(@PathVariable int parent){
		Gson gson=new GsonFactory().getActivity();
		return gson.toJson(ActivityFactory.newAppointment(getUid(), parent));
	}
	@RequestMapping(value="/api/activity/newtask/{parent}", method=RequestMethod.GET)
	public String addtask(@PathVariable int parent){
		Gson gson=new GsonFactory().getActivity();
		return gson.toJson(ActivityFactory.newTask(getUid(), parent));
	}
	@RequestMapping(value="/api/activity/update/{aidString}", method=RequestMethod.POST)
	public String edit(@RequestBody String json,@PathVariable String aidString){
		
		System.out.println(CalendarUtil.calendar2string(Calendar.getInstance())+" edit activity");
		System.out.println(json);
		Gson gson=new GsonFactory().getActivity();
		Activity act=gson.fromJson(json, Activity.class);
		if(act!=null){
			System.out.println(act.getName());
		act.updateActivity();
		return "\"Successful\"";}
		else return "\"Invalid Json. Should be start of aid and uid\"";
	}
	@RequestMapping(value="/api/activity/remove/{aidString}", method=RequestMethod.GET)
	public String remove(@PathVariable String aidString){
		int aid=Integer.parseInt(aidString);
		System.out.println("remove: "+aid);
		Activity act=ActivityFactory.getById(aid, getUid());
		if(act!=null){
			if(act.removeSelf()==0)
		return "\"Successful\"";}
		return "\"Unsuccessful\"";
	}
	@RequestMapping(value="/api/activity/detail/{aidString}", method=RequestMethod.GET)
	public ActivityDTO getActivity(@PathVariable String aidString){
		if(aidString.equals("new")){
			return ActivityDTO.getNewInstance();
		}
		ActivityDTO adto=(new ActivityService(getUid())).getActivity(aidString,getUid(),true);
		return adto;
	}
	private int getUid() {
		return (new AccountFactory()).getUid();
	}
	@ModelAttribute
	public void header(HttpServletResponse response){
		response.setHeader("Cache-Control", "no-cache");
	}
	@RequestMapping(value="/api/activity/all", method=RequestMethod.GET)
	public List<ActivityDTO> list(){
		List<ActivityDTO> nameList = (new ActivityService(getUid())).getAll(getUid());
		return nameList;
	}
	@RequestMapping(value="/api/activity/path/{parentString}", method=RequestMethod.GET)
	public String path(@PathVariable String parentString){
		List<Activity> nameList = (new ActivityService(getUid())).getPath(parentString,getUid());
		Gson gson=new GsonFactory().getActivity();
		return gson.toJson(nameList);
	}
	@RequestMapping(value="/api/account/lastupdate", method=RequestMethod.GET)
	public String lastupdate(){
		Account ac=Account.read(getUid());
		if(ac==null) return "\"Unknown User "+getUid()+"\"";
		return "\""+CalendarUtil.calendar2string(ac.getLastUpdate())+"\"";
	}
	@RequestMapping(value="/api/activity/fetch/{aidString}", method=RequestMethod.POST)
	public String getActivity1_2(@PathVariable String aidString,@RequestBody String json){
		if(Integer.parseInt(aidString)==-1){
			return null;
		}
		Activity act=ActivityFactory.getById(Integer.parseInt(aidString), getUid());
		Gson rson=new GsonFactory().getRequestFilter();
		RequestFilter rf=rson.fromJson(json, RequestFilter.class);
		System.out.println(json);
		System.out.println(rf.allowPend());
		act.fetchChild(rf);
		Gson gson=new GsonFactory().getActivity();
		return gson.toJson(act);
	}

}
