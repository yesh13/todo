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

import application.AccountFactory;
import application.ActivityDTO;
import application.ActivityService;
import domain.ActivityFilter;
import domain.DateActivityFilter;

@RestController
public class ActivityCtrl {

	@RequestMapping(value="/api/activity", method=RequestMethod.POST)
	public String add(@RequestBody ActivityDTO dto){
		System.out.println("act add post received");
		int ret=(new ActivityService()).addActivity(dto,getUid());
		return String.valueOf(ret);
	}
	@RequestMapping(value="/api/activity/{aidString}", method=RequestMethod.POST)
	public String edit(@RequestBody ActivityDTO dto,@PathVariable String aidString){
		dto.setAid(aidString);
		int ret=(new ActivityService()).editActivity(dto,getUid());
		return String.valueOf(ret);
	}
	@RequestMapping(value="/api/activity/detail/{aidString}", method=RequestMethod.GET)
	public ActivityDTO getActivity(@PathVariable String aidString){
		ActivityDTO adto=(new ActivityService()).getActivity(aidString,getUid(),true);
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
		List<ActivityDTO> nameList = (new ActivityService()).getAll(getUid());
		return nameList;
	}
	//parentString is 0 to show top level activities
	@RequestMapping(value="/api/activity/list/{parentString}", method=RequestMethod.GET)
	public List<ActivityDTO> list(@PathVariable String parentString){
		if(parentString.equals("-1")){
			List<ActivityDTO> nameList = (new ActivityService()).getAll(getUid());
			return nameList;
		}
		List<ActivityDTO> nameList = (new ActivityService()).getChild(parentString,getUid(),null);
		return nameList;
	}
	@RequestMapping(value="/api/activity/leaves/{parentString}", method=RequestMethod.GET)
	public List<ActivityDTO> list1(@PathVariable String parentString,@RequestParam(required=false) String early,@RequestParam(required=false) int span){
		List<ActivityFilter> filters=new ArrayList<ActivityFilter>();
		Calendar ec=util.CalendarUtil.string2calendar(early);
		Calendar lc=util.CalendarUtil.string2calendar(early);
		lc.add(Calendar.HOUR, span);
			ActivityFilter filter=new DateActivityFilter(ec,lc);
			filters.add(filter);
		List<ActivityDTO> nameList = (new ActivityService()).getLeaves(parentString,getUid(),filters);
		return nameList;
	}
	@RequestMapping(value="/api/activity/path/{parentString}", method=RequestMethod.GET)
	public List<ActivityDTO> path(@PathVariable String parentString){
		List<ActivityDTO> nameList = (new ActivityService()).getPath(parentString,getUid());
		return nameList;
	}

}
