package main.BYL.importData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.StringUtils;

public class Data2Db
{
  public String findMaxIssueIdFromSrcDb()
  {
    Connection srcConn = ConnectSrcDb.getSrcConnection();
    String issueId = null;
    PreparedStatement pstmt = null;
    String sql = "SELECT MAX(ISSUE_ID) FROM " + App.srcNumberTbName;
    try
    {
      pstmt = (PreparedStatement)srcConn.prepareStatement(sql);
      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        issueId = rs.getString(1);
      }
      if ((!StringUtils.isNullOrEmpty(issueId)) && 
        (!judgeIssueNumber(issueId))) {
        issueId = null;
      }
      if ((rs != null) && (!rs.isClosed())) {
        rs.close();
      }
    }
    catch (SQLException e)
    {
      LogUtil.error(e.getMessage());
    }
    return issueId;
  }
  
  public String findMaxIssueIdFromDescDb()
  {
    Connection srcConn = ConnectDesDb.getDesConnection();
    String issueId = null;
    PreparedStatement pstmt = null;
    String sql = "SELECT max(ISSUE_NUMBER) FROM " + App.descNumberTbName;
    try
    {
      pstmt = (PreparedStatement)srcConn.prepareStatement(sql);
      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        issueId = rs.getString(1);
      }
      if ((rs != null) && (!rs.isClosed())) {
        rs.close();
      }
    }
    catch (SQLException e)
    {
      LogUtil.error(e.getMessage());
    }
    return issueId;
  }
  
  public boolean judgeIssueNumber(String issueNumber)
  {
    Pattern pattern = Pattern.compile("[0-9]*");
    Matcher isNum = pattern.matcher(issueNumber);
    if (!isNum.matches()) {
      return false;
    }
    return true;
  }
  
  public List<SrcDataBean> getAllRecord()
  {
    Connection srcConn = ConnectSrcDb.getSrcConnection();
    List<SrcDataBean> srcList = new ArrayList();
    PreparedStatement pstmt = null;
    String sql = "SELECT issue_id,no_1,no_2,no_3 FROM " + App.srcNumberTbName + "";
    try
    {
      pstmt = (PreparedStatement)srcConn.prepareStatement(sql);
      ResultSet rs = pstmt.executeQuery();
      while (rs.next())
      {
        SrcDataBean srcDataBean = new SrcDataBean();
        srcDataBean.setIssueId(rs.getString(1));
        srcDataBean.setNo1(rs.getInt(2));
        srcDataBean.setNo2(rs.getInt(3));
        srcDataBean.setNo3(rs.getInt(4));
        srcList.add(srcDataBean);
      }
      if ((rs != null) && (!rs.isClosed())) {
        rs.close();
      }
    }
    catch (SQLException e)
    {
      LogUtil.error(e.getMessage());
    }
    return srcList;
  }
  
  public List<SrcDataBean> getRecordByIssueId(String issueId)
  {
    Connection srcConn = ConnectSrcDb.getSrcConnection();
    SrcDataBean srcDataBean = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    List<SrcDataBean> beans = new ArrayList<SrcDataBean>();
    String sql = "SELECT ISSUE_NUMBER,NO1,NO2,NO3,NO4,NO5 FROM " + App.srcNumberTbName + " where ISSUE_NUMBER < ? ORDER BY ISSUE_NUMBER DESC ";
    try
    {
      pstmt = (PreparedStatement)srcConn.prepareStatement(sql);
      pstmt.setString(1, issueId);
      rs = pstmt.executeQuery();
      while (rs.next())
      {
        srcDataBean = new SrcDataBean();
        srcDataBean.setIssueId(rs.getString(1));
        srcDataBean.setNo1(rs.getInt(2));
        srcDataBean.setNo2(rs.getInt(3));
        srcDataBean.setNo3(rs.getInt(4));
        srcDataBean.setNo4(rs.getInt(5));
        srcDataBean.setNo5(rs.getInt(6));
        beans.add(srcDataBean);
      }
     
    }
    catch (SQLException e)
    {
      LogUtil.error(e.getMessage());
    }
    finally
    {
    	ConnectSrcDb.close(srcConn, pstmt, rs);
    }
    return beans;
  }
  
  public SrcDataBean getRecordByDesIssueNumber(String issueNumber)
  {
    Connection srcConn = ConnectSrcDb.getSrcConnection();
    SrcDataBean srcDataBean = null;
    PreparedStatement pstmt = null;
    String sql = "SELECT issue_number,no1,no2,no3 FROM " + App.srcNumberTbName + " where issue_number = ?";
    try
    {
      pstmt = (PreparedStatement)srcConn.prepareStatement(sql);
      pstmt.setString(1, issueNumber);
      ResultSet rs = pstmt.executeQuery();
      while (rs.next())
      {
        srcDataBean = new SrcDataBean();
        srcDataBean.setIssueId(rs.getString(1));
        srcDataBean.setNo1(rs.getInt(2));
        srcDataBean.setNo2(rs.getInt(3));
        srcDataBean.setNo3(rs.getInt(4));
      }
      if ((rs != null) && (!rs.isClosed())) {
        rs.close();
      }
    }
    catch (SQLException e)
    {
      LogUtil.error(e.getMessage());
    }
    return srcDataBean;
  }
  
  private SrcDataBean caluExtentInfo(SrcDataBean srcDataBean)
  {
	  	int oneInt = srcDataBean.getNo1();
	    int twoInt = srcDataBean.getNo2();
	    int threeInt = srcDataBean.getNo3();
	    int fourInt = srcDataBean.getNo4();
	    int fiveInt = srcDataBean.getNo5();
	    int threeSpan = 0;
	    int threeSum = 0;
	    int oddNumber = 0;
	    int bigCount = 0;
	    threeSum = oneInt + twoInt + threeInt;
	    int fiveSum = oneInt + twoInt + threeInt + fourInt + fiveInt;
	    int[] three = { oneInt, twoInt, threeInt };
	    int[] five = { oneInt, twoInt, threeInt, fourInt, fiveInt };
	    for (int i = 0; i < five.length; i++)
	    {
	      if (five[i] % 2 != 0) {
	        oddNumber++;
	      }
	      if (five[i] > 6) {
	        bigCount++;
	      }
	    }
	    Arrays.sort(three);
	    threeSpan = three[2] - three[0];
	    Arrays.sort(five);
	    int fiveSpan = five[4] - five[0];
	    srcDataBean.setOddNum(oddNumber);
	    srcDataBean.setBigCount(bigCount);
	    srcDataBean.setThreeSpan(threeSpan);
	    srcDataBean.setFiveSpan(fiveSpan);
	    srcDataBean.setThreeSum(threeSum);
	    srcDataBean.setFiveSum(fiveSum);
	    srcDataBean.setBiggestNum(five[4]);
	    srcDataBean.setBiggerNum(five[3]);
	    srcDataBean.setMiddleNum(five[2]);
	    srcDataBean.setSmallerNum(five[1]);
	    srcDataBean.setSmallestNum(five[0]);
	    StringBuffer noArr = new StringBuffer();
	    for (int num : five) 
	    {
	    	noArr.append(App.translate(num));
		}
	    srcDataBean.setNoArr(noArr.toString());
	    return srcDataBean;
  }
  
  public void insertBaseData(List<SrcDataBean> beans)
  {
	  try 
	  {
		  Connection con = ConnectDesDb.getDesConnection();
		  for (SrcDataBean srcDataBean2 : beans) 
		  {
			  srcDataBean2 = caluExtentInfo(srcDataBean2);//计算基础值
			  insertData(srcDataBean2, con);
		  }
			 
	  } 
	  catch (SQLException e) 
  		{
		  e.printStackTrace();
  		}
	  
  }
  
  
  
  
  private void insertData(SrcDataBean srcDataBean, Connection conn)
    throws SQLException
  {
    String sql = "insert into " + App.descNumberTbName + ""
    		+ "(ISSUE_NUMBER,NO1,NO2,NO3,NO4,NO5,THREE_SUM,THREE_SPAN,"
    		+ "FIVE_SPAN,FIVE_SUM,BIG_COUNT,ODD_COUNT,ORIGIN,CREATE_TIME,SMALLER_NUM"
    		+ ",SMALLEST_NUM,MIDDLE_NUM,BIGGER_NUM,BIGGEST_NUM,NOARR) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    PreparedStatement pstmt = null;
    try
    {
      pstmt = (PreparedStatement)conn.prepareStatement(sql);
      pstmt.setString(1, srcDataBean.getIssueId());
      pstmt.setInt(2, srcDataBean.getNo1());
      pstmt.setInt(3, srcDataBean.getNo2());
      pstmt.setInt(4, srcDataBean.getNo3());
      pstmt.setInt(5, srcDataBean.getNo4());
      pstmt.setInt(6, srcDataBean.getNo5());
      pstmt.setInt(7, srcDataBean.getThreeSum());
      pstmt.setInt(8, srcDataBean.getThreeSpan());
      pstmt.setInt(9, srcDataBean.getFiveSpan());
      pstmt.setInt(10, srcDataBean.getFiveSum());
      pstmt.setInt(11, srcDataBean.getBigCount());
      pstmt.setInt(12, srcDataBean.getOddNum());
      pstmt.setInt(13, 1);
      pstmt.setTimestamp(14, new Timestamp(new Date().getTime()));
      pstmt.setInt(15, srcDataBean.getSmallerNum());
      pstmt.setInt(16, srcDataBean.getSmallestNum());
      pstmt.setInt(17, srcDataBean.getMiddleNum());
      pstmt.setInt(18, srcDataBean.getBiggerNum());
      pstmt.setInt(19, srcDataBean.getBiggestNum());
      pstmt.setString(20, srcDataBean.getNoArr());
     
      pstmt.executeUpdate();
    }
    catch (SQLException e)
    {
      LogUtil.error("插入基础数据表异常!" + e.getCause());
    }
    finally
    {
      if ((!pstmt.isClosed()) && (pstmt != null)) {
        pstmt.close();
      }
    }
  }
  
  private boolean haveDataInIssueId(String issueId, Connection conn)
    throws SQLException
  {
    boolean flag = false;
    int count = 0;
    String sql = "SELECT COUNT(*) FROM " + App.descNumberTbName + " WHERE ISSUE_NUMBER = " + issueId + "";
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try
    {
      pstmt = (PreparedStatement)conn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        count = rs.getInt(1);
      }
      if (count > 0) {
        flag = true;
      }
    }
    catch (SQLException e)
    {
      LogUtil.error("haveDataInIssueId方法异常！" + e.getCause());
    }
    finally
    {
      if ((rs != null) && (!rs.isClosed())) {
        rs.close();
      }
      if ((pstmt != null) && (!pstmt.isClosed())) {
        pstmt.close();
      }
    }
    return flag;
  }
  
  public String findMaxIssueIdFromDesMissTable()
  {
    Connection srcConn = ConnectDesDb.getDesConnection();
    String issueId = null;
    PreparedStatement pstmt = null;
    String sql = "SELECT MAX(ISSUE_NUMBER) FROM " + App.descMissTbName;
    try
    {
      pstmt = (PreparedStatement)srcConn.prepareStatement(sql);
      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        issueId = rs.getString(1);
      }
      if ((!StringUtils.isNullOrEmpty(issueId)) && 
        (!judgeIssueNumber(issueId))) {
        issueId = null;
      }
      if ((rs != null) && (!rs.isClosed())) {
        rs.close();
      }
    }
    catch (SQLException e)
    {
      LogUtil.error(e.getMessage());
    }
    return issueId;
  }
  
  private boolean haveMissDataInIssueId(String issueId, Connection conn)
    throws SQLException
  {
    boolean flag = false;
    int count = 0;
    String sql = "SELECT COUNT(*) FROM " + App.descMissTbName + " WHERE ISSUE_NUMBER = '" + issueId + "'";
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try
    {
      pstmt = (PreparedStatement)conn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        count = rs.getInt(1);
      }
      if (count > 0) {
        flag = true;
      }
    }
    catch (SQLException e)
    {
      LogUtil.error("查询分析表是否存在数据异常!" + e.getCause());
    }
    finally
    {
      if ((rs != null) && (!rs.isClosed())) {
        rs.close();
      }
      if ((pstmt != null) && (!pstmt.isClosed())) {
        pstmt.close();
      }
    }
    return flag;
  }
  
  
}
