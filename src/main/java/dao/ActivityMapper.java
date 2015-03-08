package dao;

import java.util.List;

import domain.Activity;
import domain.ActivityId;
import domain.Location;
import domain.Note;

import org.apache.ibatis.annotations.Param;

public interface ActivityMapper {

	public Activity getById(@Param("aid") int aid,@Param("uid") int uid);

	public int addActivity(@Param("uid") int uid,@Param("name") String name,@Param("parent") int parent,
			@Param("location") String location,@Param("note") String note);

	public int updateActivity(@Param("aid") int aid,@Param("uid") int uid,@Param("name") String name,
			@Param("parent") int parent,@Param("location") String location,@Param("note") String note);
	public List<Activity> getRealChildById(@Param("aid") int aid,@Param("uid") int uid);

}
