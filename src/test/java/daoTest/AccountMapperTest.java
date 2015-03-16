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
		  //mapper.addAccount("叶舒豪","aad");
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
		  org.junit.Assert.assertEquals("get", mapper.getByName("dddd").getPasswd(), "叶舒豪");
		}
		finally {
		  session.close();
		}
	}
	
}
