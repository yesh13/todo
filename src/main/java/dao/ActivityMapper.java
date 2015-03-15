package dao;

import java.util.List;
import org.apache.ibatis.type.DateTypeHandler;

import domain.Activity;
import domain.ActivityId;
import domain.Location;
import domain.Note;

import org.apache.ibatis.annotations.Param;

public interface ActivityMapper {

	public Activity getById(@Param("aid") int aid,@Param("uid") int uid);

	public int addActivity(@Param("uid") int uid,@Param("act") Activity act);

	public int updateActivity(@Param("uid") int uid,@Param("act") Activity act);
	public List<Activity> getRealChildById(@Param("uid") int uid,@Param("aid") int aid);

}
