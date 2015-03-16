package daoTest;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import util.MybatisFactory;
import dao.AccountMapper;

public class jdbcTest {
	  private Connection connect = null;
	  private Statement statement = null;
	  private ResultSet rs = null;
	@Test
	public void read() throws SQLException, ClassNotFoundException{
		try {
		      // This will load the MySQL driver, each DB has its own driver
		      Class.forName("com.mysql.jdbc.Driver");
		      // Setup the connection with the DB
		      connect = (Connection) DriverManager.getConnection(
		    		  "jdbc:mysql://localhost/todo?user=todo&password=1520");

		      // Statements allow to issue SQL queries to the database
		      statement = (Statement) connect.createStatement();
		      // Result set get the result of the SQL query
		      rs = statement
		          .executeQuery("select * from account where name='dddd'");
		      while(rs.next()){
		    	  String s=rs.getString("passwd");
		    	  System.out.println(s);
		      }
		      
		    } catch (SQLException e) {
		      throw e;
		    } finally {
		    	statement.close();
		    	connect.close();
		    }

	}
	@Test
	public void add() throws SQLException, ClassNotFoundException{
		try {
		      // This will load the MySQL driver, each DB has its own driver
		      Class.forName("com.mysql.jdbc.Driver");
		      // Setup the connection with the DB
		      connect = (Connection) DriverManager.getConnection(
		    		  "jdbc:mysql://localhost/todo?user=todo&password=1520&"
		    		  + "useUnicode=true&characterEncoding=UTF-8");

		      // Statements allow to issue SQL queries to the database
		      statement = (Statement) connect.createStatement();
		      // Result set get the result of the SQL query
		      int n = statement
		          .executeUpdate("insert into account (passwd) values ('密码')");
		      
		    } catch (SQLException e) {
		      throw e;
		    } finally {
		    	statement.close();
		    	connect.close();
		    }

	}
}
