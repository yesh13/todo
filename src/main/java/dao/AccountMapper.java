package dao;

import org.apache.ibatis.annotations.Param;

import application.Account;

public interface AccountMapper {

	public Account getByName(String name);

	public int addAccount(@Param("name") String name,@Param("passwd") String passwd);
}
