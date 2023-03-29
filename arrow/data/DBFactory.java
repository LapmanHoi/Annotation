package arrow.data;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import arrow.utility.ArrowLang;
import arrow.utility.ArrowTool;

/**
 * @author Edmond Hoi. lmhoi@mpu.edu.mo
 * @Copyright 2023 March.
 * @Description: Database Factory.
 */
public abstract class DBFactory {
  protected PreparedStatement pStmt = null;

  protected Connection conn = null;

  protected ResultSet rs = null;

  public DBFactory() throws Exception {
    makeConnection();
  }

  public abstract void makeConnection() throws Exception;

  public int prepareSQL(String sql) {
    try {
      pStmt = conn.prepareStatement(sql);
    } catch (SQLException sqle) {
      sqle.printStackTrace();
      return -1;
    }
    return 0;
  }

  public void addBatch() throws Exception {
    pStmt.addBatch();
  }

  public void commitSQL() throws Exception {
    ArrowTool.writeLog(this.getClass()).debug("COMMIT SQL TRANSACTION.");
    conn.commit();
  }

  public String getCurrentDBTime() {
    return (new Timestamp(System.currentTimeMillis())).toString();
  }

  public void setAutoCommit(boolean state) throws Exception {
    ArrowTool.writeLog(this.getClass()).debug("SET AUTOCOMMIT MODE FROM " + conn.getAutoCommit() + " TO " + state + ".");
    conn.setAutoCommit(state);
  }

  public void setBinaryStream(int location, InputStream is, int length) throws Exception {
    pStmt.setBinaryStream(location, is, length);
  }

  public void setBlob(int location, InputStream is, int length) throws Exception {
    pStmt.setBlob(location, is, length);
  }

  public void setDate(int location, Date dParm) throws Exception {
    pStmt.setDate(location, dParm);
  }

  public void setDouble(int location, double dParm) throws Exception {
    pStmt.setDouble(location, dParm);
  }

  public void setInt(int location, int iParm) throws Exception {
    pStmt.setInt(location, iParm);
  }

  public void setBytes(int location, byte[] bParm) throws Exception {
    pStmt.setBytes(location, bParm);
  }

  public void setString(int location, String sParm) throws Exception {
    pStmt.setString(location, sParm);
  }

  public void setNString(int location, String sParm) throws Exception {
    pStmt.setNString(location, sParm);
  }

  public void setTimestamp(int location, Timestamp sTime) throws Exception {
    pStmt.setTimestamp(location, sTime);
  }

  public String setParam(String sql, String param) {
    sql = sql.replaceFirst(ArrowLang.ARROW, param.toUpperCase());
    return sql;
  }

  public int[] executeBatch() throws Exception {
    ArrowTool.writeLog(this.getClass()).debug(pStmt.toString());
    return pStmt.executeBatch();
  }

  public ResultSet executeQuery() throws Exception {
    ArrowTool.writeLog(this.getClass()).debug(pStmt.toString());
    rs = pStmt.executeQuery();
    return rs;
  }

  public void execute() throws Exception {
    ArrowTool.writeLog(this.getClass()).debug(pStmt.toString());
    pStmt.execute();
  }

  public int executeUpdate() throws Exception {
    ArrowTool.writeLog(this.getClass()).debug(pStmt.toString());
    return pStmt.executeUpdate();
  }

  public String[] queryColumnHeader() throws Exception {
    rs = pStmt.executeQuery();
    int col = pStmt.getMetaData().getColumnCount();
    String[] sHeader = new String[col];
    for (int i = 1; i <= col; ++i) {
      sHeader[i - 1] = pStmt.getMetaData().getColumnName(i);
    }
    return sHeader;
  }

  public InputStream isExecuteQuery() throws Exception {
    ArrowTool.writeLog(this.getClass()).debug(pStmt.toString());
    InputStream is = null;
    rs = pStmt.executeQuery();
    while (rs.next()) {
      is = rs.getBinaryStream(1);
    }
    return is;
  }

  public String sExecuteQuery() throws Exception {
    ArrowTool.writeLog(this.getClass()).debug(pStmt.toString());
    rs = pStmt.executeQuery();
    rs.last();
    String sRow = ArrowLang.EMPTY;
    rs.beforeFirst();
    while (rs.next()) {
      sRow = rs.getString(1);
    }
    return (sRow == null ? ArrowLang.EMPTY : sRow);
  }

  public String[] saExecuteQuery() throws Exception {
    ArrowTool.writeLog(this.getClass()).debug(pStmt.toString());
    rs = pStmt.executeQuery();
    int colSize = pStmt.getMetaData().getColumnCount();
    String[] sRow = new String[colSize];
    for (int i = 0; i < sRow.length; ++i) {
      sRow[i] = ArrowLang.EMPTY;
    }
    while (rs.next()) {
      for (int i = 1; i <= colSize; ++i) {
        sRow[i - 1] = rs.getString(i);
      }
    }
    return sRow;
  }

  public List<List<String>> lExecuteQuery() throws Exception {
    ArrowTool.writeLog(this.getClass()).debug(pStmt.toString());
    List<List<String>> lResult = new ArrayList<List<String>>();
    rs = pStmt.executeQuery();
    int colSize = pStmt.getMetaData().getColumnCount();
    while (rs.next()) {
      List<String> lRow = new ArrayList<String>();
      for (int i = 1; i <= colSize; ++i) {
        lRow.add(rs.getString(i));
      }
      lResult.add(lRow);
    }
    return lResult;
  }

  public void closeStmt() throws Exception {
    if (rs != null) {
      rs.close();
    }
    if (pStmt != null) {
      pStmt.close();
    }
  }

  public void closeConn() throws Exception {
    try {
      if (conn != null) {
        conn.close();
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}