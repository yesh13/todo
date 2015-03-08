package daoTest;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import util.MybatisFactory;
import dao.AccountMapper;

public class AccountMapperTest {
	@Test
	public void add(){

		SqlSession session = MybatisFactory.get().openSession();
		AccountMapper mapper;
		try {
		  mapper = session.getMapper(AccountMapper.class);
//		  return mapper.insert(uid,act.getName(),act.getParent().toString(),
//				  act.getLocation().toString(),act.getNote().toString());
		  mapper.addAccount("namea","mima");
		}
		finally {
			session.commit();
		  session.close();
		}
	}
	@Test
	public void get(){

		SqlSession session = MybatisFactory.get().openSession();
		AccountMapper mapper;
		try {
		  mapper = session.getMapper(AccountMapper.class);
		  org.junit.Assert.assertEquals("get", mapper.getByName("shuhao").getPasswd(), "hello");
		}
		finally {
		  session.close();
		}
	}
	
}
