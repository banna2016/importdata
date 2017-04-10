package main.BYL.importData;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import com.mysql.jdbc.StringUtils;

public class App
{
  static String maxIssueId = null;
  static String lineCount = null;
  static String srcNumberTbName = null;
  static String descNumberTbName = null;
  static String descMissTbName = null;
  
  private static void initParam()
  {
    Properties p = new Properties();
    InputStream is = App.class.getClassLoader().getResourceAsStream("db.properties");
    try
    {
      p.load(is);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    lineCount = p.getProperty("lineCount");
    srcNumberTbName = p.getProperty("srcNumberTbName");
    descNumberTbName = p.getProperty("descNumberTbName");
    maxIssueId = p.getProperty("maxIssueId");
  }
  
  public static void main(String[] args)
  {
    initParam();
	importData();
  }
  
  private static void importData()
  {
    Data2Db data2Db = new Data2Db();
    List<SrcDataBean> beans = data2Db.getRecordByIssueId(maxIssueId);
    data2Db.insertBaseData(beans);
    
    
  }
  
  public static String getNextIssueNumber(String issueNumber)
  {
    String nextIssueNumber = null;
    String issueCode = issueNumber.substring(issueNumber.length() - 2, issueNumber.length());
    if (issueCode.equals(lineCount))
    {
    	if(issueNumber.length()==8)
    	{
    		nextIssueNumber = getNextDay(issueNumber.substring(0, 6)) + "01";//9位开奖号码是连接001,8位开奖号码是连接01
    	}
    	else
	    	if(issueNumber.length()==9)
	    	{
	    		nextIssueNumber = getNextDay(issueNumber.substring(0, 6)) + "001";//9位开奖号码是连接001,8位开奖号码是连接01
	    	}
      
    }
    else
    {
      int codeInt = Integer.parseInt(issueCode) + 1;
      if (codeInt < 10) 
      {
        nextIssueNumber = issueNumber.substring(0, issueNumber.length() - 2) + "0" + codeInt;
      } 
      else 
      {
        nextIssueNumber = issueNumber.substring(0, issueNumber.length() - 2) + codeInt;
      }
    }
    return nextIssueNumber;
  }
  
  public static String getNextDay(String day)
  {
    SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
    Calendar calendar = new GregorianCalendar();
    String dateString = null;
    try
    {
      Date date = formatter.parse(day);
      calendar.setTime(date);
      calendar.add(5, 1);
      date = calendar.getTime();
      dateString = formatter.format(date);
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    return dateString;
  }
  
  public static String translate(int temp)
  {
    String rtn = null;
    if (temp < 10) {
      rtn = temp+"";
    } else if (temp == 10) {
      rtn = "A";
    } else if (temp == 11) {
      rtn = "J";
    } else if (temp == 12) {
      rtn = "Q";
    }
    return rtn;
  }
}
