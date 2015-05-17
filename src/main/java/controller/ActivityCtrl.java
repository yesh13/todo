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
import domain.ActivityFilter;
import domain.Appointment;
import domain.BasicActivity;
import domain.DateActivityFilter;
import domain.LeavesComparator;
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
	@RequestMapping(value="/api/activity/aid/{aidString}", method=RequestMethod.POST)
	public String edit(@RequestBody String json,@PathVariable String aidString){
		Session session = util.hibernate.HibernateFactory.getInstance()
				.buildSessionFactory().openSession();
		Transaction tx = null;
		Gson gson=new GsonFactory().getActivity();
		Activity act=gson.fromJson(json, Activity.class);
		if(act!=null){
		try {
			tx = session.beginTransaction();
			session.update(act);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return "Successful";}
		else return "Invalid Json. Should be start of aid and uid";
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
	//parentString is 0 to show top level activities
	@RequestMapping(value="/api/activity/list/{parentString}", method=RequestMethod.GET)
	public List<ActivityDTO> list(@PathVariable String parentString){
		if(parentString.equals("-1")){
			List<ActivityDTO> nameList = (new ActivityService(getUid())).getAll(getUid());
			return nameList;
		}
		List<ActivityDTO> nameList = (new ActivityService(getUid())).getChild(parentString,getUid(),null);
		return nameList;
	}
	@RequestMapping(value="/api/activity/leaves/{parentString}", method=RequestMethod.GET)
	public List<ActivityDTO> list1(@PathVariable String parentString,@RequestParam(required=false) String t1,@RequestParam(required=false) String t2,@RequestParam(required=false) String unscheduled){
		List<ActivityFilter> filters=new ArrayList<ActivityFilter>();
		if(t1!=null&&t2!=null){
			Calendar ec=util.CalendarUtil.string2calendar(t1);
			Calendar lc=util.CalendarUtil.string2calendar(t2);
			ActivityFilter filter=new DateActivityFilter(ec,lc);
			filters.add(filter);
		}
		if(unscheduled!=null&&unscheduled.equals("1")){
		}
		List<ActivityDTO> nameList = (new ActivityService(getUid())).getLeaves(parentString,getUid(),filters);
		return nameList;
	}
	@RequestMapping(value="/api/activity/path/{parentString}", method=RequestMethod.GET)
	public List<ActivityDTO> path(@PathVariable String parentString){
		List<ActivityDTO> nameList = (new ActivityService(getUid())).getPath(parentString,getUid());
		return nameList;
	}
	@RequestMapping(value="/api/account/lastupdate", method=RequestMethod.GET)
	public String lastupdate(){
		Account ac=Account.read(getUid());
		if(ac==null) return "Unknown User "+getUid();
		return "\""+CalendarUtil.calendar2string(ac.getLastUpdate())+"\"";
	}
	@RequestMapping(value="/api/activity/aid/{aidString}", method=RequestMethod.GET)
	public String getActivity1_2(@PathVariable String aidString,@RequestParam(required=false) Long t1,@RequestParam(required=false) Long t2){
		if(Integer.parseInt(aidString)==-1){
			return null;
		}
		Activity act=ActivityFactory.getById(Integer.parseInt(aidString), getUid());
		Calendar c1=Calendar.getInstance();
		Calendar c2=Calendar.getInstance();
		if(t1!=null&&t2!=null){
		c1.setTimeInMillis(t1);
		c2.setTimeInMillis(t2);
		}
		else{
			c1.set(Calendar.YEAR, 1970);
			c2.set(Calendar.YEAR, 2970);
		}
		act.fetchChild(c1,c2);
		Gson gson=new GsonFactory().getActivity();
		return gson.toJson(act);
	}
}
