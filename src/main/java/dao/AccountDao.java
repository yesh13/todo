package dao;

import org.apache.ibatis.session.SqlSession;

import util.MybatisFactory;
import application.Account;

public class AccountDao {
public Account getByName(String name){
	SqlSession session = MybatisFactory.get().openSession();
	AccountMapper mapper;
	try {
	  mapper = session.getMapper(AccountMapper.class);
	  return mapper.getByName(name);
	}
	finally {
		session.commit();
	  session.close();
	}
}
}
