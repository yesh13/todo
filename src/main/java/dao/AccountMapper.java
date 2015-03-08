package dao;

import application.Account;
import org.apache.ibatis.annotations.Param;

public interface AccountMapper {

	public Account getByName(String name);

	public int addAccount(@Param("name") String name,@Param("passwd") String passwd);
}
