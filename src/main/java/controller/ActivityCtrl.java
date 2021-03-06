package controller;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import util.CalendarUtil;
import application.Account;
import application.AccountFactory;
import application.ActivityDTO;
import application.ActivityService;
import domain.ActivityFilter;
import domain.DateActivityFilter;
import domain.LeavesComparator;
import domain.OnlyUnscheduledFilter;

@RestController
public class ActivityCtrl {

	@RequestMapping(value="/api/activity", method=RequestMethod.POST)
	public String add(@RequestBody ActivityDTO dto){
		System.out.println("act add post received "+ dto.getAid()+" "+dto.getName());
		int ret=(new ActivityService(getUid())).addActivity(dto,getUid());
		return String.valueOf(ret);
	}
	@RequestMapping(value="/api/activity/{aidString}", method=RequestMethod.POST)
	public String edit(@RequestBody ActivityDTO dto,@PathVariable String aidString){
		System.out.println("act edit post received "+ dto.getAid()+" "+dto.getName());
		dto.setAid(aidString);
		int ret=(new ActivityService(getUid())).editActivity(dto,getUid());
		return String.valueOf(ret);
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
			filters.add(new OnlyUnscheduledFilter(true));
		}
		if(unscheduled!=null&&unscheduled.equals("1")){
			filters.add(new OnlyUnscheduledFilter(false));
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

}
