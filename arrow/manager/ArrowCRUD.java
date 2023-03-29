package arrow.manager;

import java.util.List;

import arrow.data.DBConn;
import arrow.model.ArrowBean;

/**
 * @author Edmond Hoi. lmhoi@mpu.edu.mo
 * @Copyright 2023 March.
 * @Description: CRUD functions.
 */
public interface ArrowCRUD {
  /**
   * (CRUD Transaction) to create a table record.
   *
   * @param dbObject database connection object
   * @param bean     JavaBean to hold the record information
   * @return 0=error
   * @throws Exception
   */
  public int create(DBConn dbObject, ArrowBean bean) throws Exception;

  /**
   * (CRUD Transaction) to read a table record.
   *
   * @param dbObject database connection object
   * @param bean     JavaBean to hold the input record information
   * @return JavaBean to hold the output record information
   * @throws Exception
   */
  public ArrowBean read(DBConn dbObject, ArrowBean bean) throws Exception;

  /**
   * (CRUD Transaction) to update a table record.
   *
   * @param dbObject database connection object
   * @param bean     JavaBean to hold the record information
   * @return 0=error
   * @throws Exception
   */
  public int update(DBConn dbObject, ArrowBean bean) throws Exception;

  /**
   * (CRUD Transaction) to delete a table record.
   *
   * @param dbObject database connection object
   * @param bean     JavaBean to hold the record information
   * @return 0=error
   * @throws Exception
   */
  public int delete(DBConn dbObject, ArrowBean bean) throws Exception;

  /**
   * Batch insert a number of records
   * 
   * @param dbObject database connection object
   * @param lBean    List of JavaBean
   * @return 0=error
   * @throws Exception
   */
  public int[] batchCreate(DBConn dbObject, List<? extends ArrowBean> lBean) throws Exception;

  /**
   * Batch update a number of records
   * 
   * @param dbObject database connection object
   * @param lBean    List of JavaBean
   * @return 0=error
   * @throws Exception
   */
  public int[] batchUpdate(DBConn dbObject, List<? extends ArrowBean> lBean) throws Exception;

  /**
   * Batch delete a number of records
   * 
   * @param dbObject database connection object
   * @param lBean    List of JavaBean
   * @return 0=error
   * @throws Exception
   */
  public int[] batchDelete(DBConn dbObject, List<? extends ArrowBean> lBean) throws Exception;

  /**
   * To list all records of a table.
   *
   * @param dbObject database connection object
   * @param bean     JavaBean to hold the record information
   * @return all table records
   * @throws Exception
   */
  public List<ArrowBean> list(DBConn dbObject) throws Exception;

  /**
   * Query the join table by a join name
   * 
   * @param dbObject database connection object
   * @param joinName name for joining other tables
   * @return all joining table records
   * @throws Exception record not found
   */
  public List<ArrowBean> queryJoin(DBConn dbObject, String joinName) throws Exception;

  /**
   * Query the join table by a join name
   * 
   * @param dbObject database connection object
   * @param joinName name for joining other tables
   * @return all joining table records
   * @throws Exception record not found
   */
  public List<List<String>> queryView(DBConn dbObject, String joinName) throws Exception;

  /**
   * Update the join table by a join name
   * 
   * @param dbObject database connection object
   * @param joinName name for joining other tables
   * @return all joining table records
   * @throws Exception record not found
   */
  public int[] updateJoin(DBConn dbObject, String joinName) throws Exception;
}