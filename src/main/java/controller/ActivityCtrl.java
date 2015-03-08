package controller;


import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import domain.Activity;
import domain.Todo;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import application.ActivityDTO;
import application.ActivityService;

@RestController
public class ActivityCtrl {
	//parentString is 0 to show top level activities
	@RequestMapping(value="/api/list/{parentString}", method=RequestMethod.GET)
	public List<ActivityDTO> list(@PathVariable String parentString){
		List<ActivityDTO> nameList = (new ActivityService()).getChild(parentString);
		return nameList;
	}
	@RequestMapping(value="/api/activity/{aidString}", method=RequestMethod.GET)
	public ActivityDTO getActivity(@PathVariable String aidString){
		ActivityDTO adto=(new ActivityService()).getActivity(aidString);
		return adto;
	}
	@RequestMapping(value="/api/activity", method=RequestMethod.POST)
	public String add(@RequestParam("name") String name,
			@RequestParam("location") String location,
			@RequestParam("note") String note){
		System.out.println("act add post received");
		ActivityDTO dto=new ActivityDTO();
		dto.setName(name);
		dto.setLocation(location);
		dto.setNote(note);
		int ret=(new ActivityService()).insertActivity(dto);
			ret=(new ActivityService()).editActivity(dto);
		return String.valueOf(ret);
	}
	@RequestMapping(value="/api/activity/{aidString}", method=RequestMethod.POST)
	public String add(@PathVariable String aidString, @RequestParam("name") String name,
			@RequestParam("location") String location,
			@RequestParam("note") String note){
		ActivityDTO dto=new ActivityDTO();
		dto.setAid(aidString);
		dto.setName(name);
		dto.setLocation(location);
		dto.setNote(note);
		int ret=(new ActivityService()).editActivity(dto);
		return String.valueOf(ret);
	}

}
