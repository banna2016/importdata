package main.BYL.importData;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectDesDb
{
  public static synchronized Connection getDesConnection()
  {
    return _getConnection();
  }
  
  private static Connection _getConnection()
  {
    try
    {
      String driver = "";
      String url = "";
      String username = "";
      String password = "";
      Properties p = new Properties();
      InputStream is = ConnectDesDb.class.getClassLoader().getResourceAsStream("db.properties");
      p.load(is);
      driver = p.getProperty("driver", "");
      url = p.getProperty("des.url", "");
      username = p.getProperty("des.username", "");
      password = p.getProperty("des.password", "");
      Properties pr = new Properties();
      pr.put("user", username);
      pr.put("password", password);
      pr.put("characterEncoding", "UTF-8");
      pr.put("useUnicode", "TRUE");
      Class.forName(driver).newInstance();
      return DriverManager.getConnection(url, pr);
    }
    catch (Exception se)
    {
      se.printStackTrace();
      LogUtil.error("获取目标数据库连接失败!");
    }
    return null;
  }
  
  public static void closeDesConnection(Connection conn)
    throws SQLException
  {
    if ((!conn.isClosed()) && (conn != null)) {
      conn.close();
    }
  }
}
