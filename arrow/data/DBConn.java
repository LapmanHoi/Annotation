package arrow.data;

import java.sql.DriverManager;
import java.util.ResourceBundle;

import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * @author Edmond Hoi. lmhoi@mpu.edu.mo
 * @Copyright 2023 March.
 * @Description: Database Connection Object.
 */
public class DBConn extends DBFactory {
  private boolean connType = true;

  private String dbName, dbHost, dsName, driverName, userName, password;

  public DBConn() throws Exception {
    super();
  }

  @Override
  public void makeConnection() throws Exception {
    getDataSource();
    connectDB();
  }

  private void getDataSource() throws Exception {
    ResourceBundle rb = ResourceBundle.getBundle("datasource");
    connType = rb.getString("ConnType").equals("POOL") ? true : false;
    dbHost = rb.getString("DBHost");
    dbName = rb.getString("DBName");
    if (connType) {
      dsName = rb.getString("DSName");
    } else {
      driverName = rb.getString("DriverName");
      userName = rb.getString("DBUser");
      password = rb.getString("DBPswd");
    }
  }

  private void connectDB() throws Exception {
    if (connType) {
      InitialContext ic = new InitialContext();
      DataSource jdbcURL = (DataSource) ic.lookup(dsName + dbName);
      conn = jdbcURL.getConnection();
    } else {
      Class.forName(driverName).getDeclaredConstructor().newInstance();
      conn = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":3306/" + dbName + "?user=" + userName + "&password=" + password + "&useUnicode=yes&characterEncoding=UTF-8&serverTimezone=UTC");
    }
  }
}