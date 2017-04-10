package main.BYL.importData;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.mysql.jdbc.PreparedStatement;

public class ConnectSrcDb
{
  private static Connection connection = null;
  
  public static synchronized Connection getSrcConnection()
  {
    try
    {
      if ((connection == null) || (connection.isClosed())) {
        connection = _getConnection();
      }
    }
    catch (SQLException e)
    {
      LogUtil.error(e.getMessage());
    }
    return connection;
  }
  
  private static Connection _getConnection()
  {
    try
    {
      String driver = "com.mysql.jdbc.Driver";
      String url = "jdbc:mysql://192.168.1.253:3306/echart3";
      String username = "echart";
      String password = "echart";
      Properties p = new Properties();
      InputStream is = ConnectSrcDb.class.getClassLoader().getResourceAsStream("db.properties");
      p.load(is);
      driver = p.getProperty("driver", driver);
      url = p.getProperty("src.url", url);
      username = p.getProperty("src.username", username);
      password = p.getProperty("src.password", password);
      Properties pr = new Properties();
      pr.put("user", username);
      pr.put("password", password);
      pr.put("characterEncoding", "GB2312");
      pr.put("useUnicode", "TRUE");
      Class.forName(driver).newInstance();
      LogUtil.info("Դ���ݿ����ӳɹ���");
      return DriverManager.getConnection(url, pr);
    }
    catch (Exception se)
    {
      se.printStackTrace();
      LogUtil.error("��ȡԴ���ݿ�����ʧ��!");
    }
    return null;
  }
  
  public static void close(Connection conn,PreparedStatement pstmt,ResultSet rs)
		    
  {
    try {
		if ((!conn.isClosed()) && (conn != null)) {
		  conn.close();
		}
		 if(null != pstmt )
		    {
		    	pstmt.close();
		    }
		    if(null != rs)
		    {
		    	rs.close();
		    }
	} catch (SQLException e) {
		e.printStackTrace();
	}
   
  }
}
