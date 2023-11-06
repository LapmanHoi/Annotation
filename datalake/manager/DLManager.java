package datalake.manager;

import java.io.FileWriter;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import datalake.data.DBConn;
import datalake.model.DLBean;
import datalake.model.DLConverter;
import datalake.model.JoinBean;
import datalake.utility.DLLang;
import datalake.utility.DLTool;
import datalake.utility.DLTool.DLData;

/**
 * @author Edmond Hoi. lmhoi@mpu.edu.mo
 * @Copyright 2023 March.
 * @Description: Data Model Management.
 */
public abstract class DLManager implements DLCRUD, DLConverter {
  private String CREATE_RECORD, READ_RECORD, UPDATE_RECORD, DELETE_RECORD, LIST_RECORD;
  private List<JoinBean> joinTables = new ArrayList<JoinBean>();
  protected DLMapper metadata;
  protected DLView viewData;
  private int pkNum;

  /**
   * Manager Constructor
   */
  public DLManager() {
    try {
      Method method = this.getClass().getMethod(DLLang.MAPPING_METHOD);
      metadata = method.getAnnotation(DLMapper.class);
      mapping();
      method = this.getClass().getMethod(DLLang.JOINING_METHOD);
      viewData = method.getAnnotation(DLView.class);
      joining();
      verification();
      generateBean();
      generateService();
      generateSQL();
    } catch (Exception ex) {
      DLTool.writeLog(this.getClass()).error(ex.toString());
    }
  }

  /**
   * Joining the current table with other tables
   */
  public abstract void joining();

  /**
   * Object Relation Mapping function to map a JavaBean with a table
   */
  public abstract void mapping();

  @Override
  public int create(DBConn dbObject, DLBean bean) throws Exception {
    dbObject.prepareSQL(CREATE_RECORD);
    for (int i = 1; i <= metadata.properties().length; ++i) {
      if (metadata.types()[i - 1].equals(DLData.Integer)) {
        dbObject.setInt(i, (Integer) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean));
      } else if (metadata.types()[i - 1].equals(DLData.Double)) {
        dbObject.setDouble(i, (Double) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean));
      } else if (metadata.types()[i - 1].equals(DLData.Binary)) {
        dbObject.setBinaryStream(i, (InputStream) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean), ((InputStream) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean)).available());
      } else if (metadata.types()[i - 1].equals(DLData.Blob)) {
        dbObject.setBlob(i, (InputStream) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean), ((InputStream) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean)).available());
      } else if (metadata.types()[i - 1].equals(DLData.Timestamp)) {
        dbObject.setTimestamp(i, (Timestamp) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean));
      } else {
        dbObject.setString(i, (String) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean));
      }
    }
    int rc = dbObject.executeUpdate();
    dbObject.closeStmt();
    return rc;
  }

  @Override
  public int[] batchCreate(DBConn dbObject, List<? extends DLBean> lBean) throws Exception {
    int[] rc = new int[lBean.size()];
    dbObject.setAutoCommit(false);
    dbObject.prepareSQL(CREATE_RECORD);
    for (int j = 0; j < lBean.size(); ++j) {
      DLBean bean = lBean.get(j);
      for (int i = 1; i <= metadata.properties().length; ++i) {
        if (metadata.types()[i - 1].equals(DLData.Integer)) {
          dbObject.setInt(i, (Integer) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean));
        } else if (metadata.types()[i - 1].equals(DLData.Double)) {
          dbObject.setDouble(i, (Double) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean));
        } else if (metadata.types()[i - 1].equals(DLData.Binary)) {
          dbObject.setBinaryStream(i, (InputStream) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean), ((InputStream) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean)).available());
        } else if (metadata.types()[i - 1].equals(DLData.Blob)) {
          dbObject.setBlob(i, (InputStream) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean), ((InputStream) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean)).available());
        } else if (metadata.types()[i - 1].equals(DLData.Timestamp)) {
          dbObject.setTimestamp(i, (Timestamp) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean));
        } else {
          dbObject.setString(i, (String) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean));
        }
      }
      dbObject.addBatch();
    }
    rc = dbObject.executeBatch();
    dbObject.commitSQL();
    dbObject.closeStmt();
    dbObject.setAutoCommit(true);
    return rc;
  }

  @Override
  public int update(DBConn dbObject, DLBean bean) throws Exception {
    dbObject.prepareSQL(UPDATE_RECORD);
    for (int i = 1; i <= metadata.properties().length - pkNum; ++i) {
      if (metadata.types()[i + pkNum - 1].equals(DLData.Integer)) {
        dbObject.setInt(i, (Integer) callGetter(metadata.beanName(), metadata.properties()[i + pkNum - 1], bean));
      } else if (metadata.types()[i + pkNum - 1].equals(DLData.Double)) {
        dbObject.setDouble(i, (Double) callGetter(metadata.beanName(), metadata.properties()[i + pkNum - 1], bean));
      } else if (metadata.types()[i + pkNum - 1].equals(DLData.Binary)) {
        dbObject.setBinaryStream(i, (InputStream) callGetter(metadata.beanName(), metadata.properties()[i + pkNum - 1], bean), ((InputStream) callGetter(metadata.beanName(), metadata.properties()[i + pkNum - 1], bean)).available());
      } else if (metadata.types()[i + pkNum - 1].equals(DLData.Blob)) {
        dbObject.setBlob(i, (InputStream) callGetter(metadata.beanName(), metadata.properties()[i + pkNum - 1], bean), ((InputStream) callGetter(metadata.beanName(), metadata.properties()[i + pkNum - 1], bean)).available());
      } else if (metadata.types()[i + pkNum - 1].equals(DLData.Timestamp)) {
        dbObject.setTimestamp(i, (Timestamp) callGetter(metadata.beanName(), metadata.properties()[i + pkNum - 1], bean));
      } else {
        dbObject.setString(i, (String) callGetter(metadata.beanName(), metadata.properties()[i + pkNum - 1], bean));
      }
    }
    for (int i = 1; i <= pkNum; ++i) {
      if (metadata.types()[i - 1].equals(DLData.Integer)) {
        dbObject.setInt(metadata.properties().length - pkNum + i, (Integer) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean));
      } else if (metadata.types()[i - 1].equals(DLData.Double)) {
        dbObject.setDouble(metadata.properties().length - pkNum + i, (Double) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean));
      } else if (metadata.types()[i - 1].equals(DLData.Binary)) {
        dbObject.setBinaryStream(metadata.properties().length - pkNum + i, (InputStream) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean), ((InputStream) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean)).available());
      } else if (metadata.types()[i - 1].equals(DLData.Blob)) {
        dbObject.setBlob(metadata.properties().length - pkNum + i, (InputStream) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean), ((InputStream) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean)).available());
      } else if (metadata.types()[i - 1].equals(DLData.Double)) {
        dbObject.setTimestamp(metadata.properties().length - pkNum + i, (Timestamp) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean));
      } else {
        dbObject.setString(metadata.properties().length - pkNum + i, (String) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean));
      }
    }
    int rc = dbObject.executeUpdate();
    dbObject.closeStmt();
    return rc;
  }

  @Override
  public int[] batchUpdate(DBConn dbObject, List<? extends DLBean> lBean) throws Exception {
    int[] rc = new int[lBean.size()];
    dbObject.setAutoCommit(false);
    dbObject.prepareSQL(UPDATE_RECORD);
    for (int j = 0; j < lBean.size(); ++j) {
      DLBean bean = lBean.get(j);
      for (int i = 1; i <= metadata.properties().length - pkNum; ++i) {
        if (metadata.types()[i + pkNum - 1].equals(DLData.Integer)) {
          dbObject.setInt(i, (Integer) callGetter(metadata.beanName(), metadata.properties()[i + pkNum - 1], bean));
        } else if (metadata.types()[i + pkNum - 1].equals(DLData.Double)) {
          dbObject.setDouble(i, (Double) callGetter(metadata.beanName(), metadata.properties()[i + pkNum - 1], bean));
        } else if (metadata.types()[i + pkNum - 1].equals(DLData.Binary)) {
          dbObject.setBinaryStream(i, (InputStream) callGetter(metadata.beanName(), metadata.properties()[i + pkNum - 1], bean), ((InputStream) callGetter(metadata.beanName(), metadata.properties()[i + pkNum - 1], bean)).available());
        } else if (metadata.types()[i + pkNum - 1].equals(DLData.Blob)) {
          dbObject.setBlob(i, (InputStream) callGetter(metadata.beanName(), metadata.properties()[i + pkNum - 1], bean), ((InputStream) callGetter(metadata.beanName(), metadata.properties()[i + pkNum - 1], bean)).available());
        } else if (metadata.types()[i + pkNum - 1].equals(DLData.Timestamp)) {
          dbObject.setTimestamp(i, (Timestamp) callGetter(metadata.beanName(), metadata.properties()[i + pkNum - 1], bean));
        } else {
          dbObject.setString(i, (String) callGetter(metadata.beanName(), metadata.properties()[i + pkNum - 1], bean));
        }
      }
      for (int i = 1; i <= pkNum; ++i) {
        if (metadata.types()[i - 1].equals(DLData.Integer)) {
          dbObject.setInt(metadata.properties().length - pkNum + i, (Integer) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean));
        } else if (metadata.types()[i - 1].equals(DLData.Double)) {
          dbObject.setDouble(metadata.properties().length - pkNum + i, (Double) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean));
        } else if (metadata.types()[i - 1].equals(DLData.Binary)) {
          dbObject.setBinaryStream(metadata.properties().length - pkNum + i, (InputStream) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean),
              ((InputStream) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean)).available());
        } else if (metadata.types()[i - 1].equals(DLData.Blob)) {
          dbObject.setBlob(metadata.properties().length - pkNum + i, (InputStream) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean), ((InputStream) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean)).available());
        } else if (metadata.types()[i - 1].equals(DLData.Timestamp)) {
          dbObject.setTimestamp(metadata.properties().length - pkNum + i, (Timestamp) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean));
        } else {
          dbObject.setString(metadata.properties().length - pkNum + i, (String) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean));
        }
      }
      dbObject.addBatch();
    }
    rc = dbObject.executeBatch();
    dbObject.commitSQL();
    dbObject.closeStmt();
    dbObject.setAutoCommit(true);
    return rc;
  }

  @Override
  public int delete(DBConn dbObject, DLBean bean) throws Exception {
    dbObject.prepareSQL(DELETE_RECORD);
    for (int i = 1; i <= pkNum; ++i) {
      if (metadata.types()[i - 1].equals(DLData.Integer)) {
        dbObject.setInt(i, (Integer) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean));
      } else if (metadata.types()[i - 1].equals(DLData.Double)) {
        dbObject.setDouble(i, (Double) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean));
      } else if (metadata.types()[i - 1].equals(DLData.Timestamp)) {
        dbObject.setTimestamp(i, (Timestamp) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean));
      } else {
        dbObject.setString(i, (String) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean));
      }
    }
    int rc = dbObject.executeUpdate();
    dbObject.closeStmt();
    return rc;
  }

  @Override
  public int[] batchDelete(DBConn dbObject, List<? extends DLBean> lBean) throws Exception {
    int[] rc = new int[lBean.size()];
    dbObject.setAutoCommit(false);
    dbObject.prepareSQL(DELETE_RECORD);
    for (int j = 0; j < lBean.size(); ++j) {
      DLBean bean = lBean.get(j);
      for (int i = 1; i <= pkNum; ++i) {
        if (metadata.types()[i - 1].equals(DLData.Integer)) {
          dbObject.setInt(i, (Integer) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean));
        } else if (metadata.types()[i - 1].equals(DLData.Double)) {
          dbObject.setDouble(i, (Double) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean));
        } else if (metadata.types()[i - 1].equals(DLData.Timestamp)) {
          dbObject.setTimestamp(i, (Timestamp) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean));
        } else {
          dbObject.setString(i, (String) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean));
        }
      }
      dbObject.addBatch();
    }
    rc = dbObject.executeBatch();
    dbObject.commitSQL();
    dbObject.closeStmt();
    dbObject.setAutoCommit(true);
    return rc;
  }

  @Override
  public DLBean read(DBConn dbObject, DLBean bean) throws Exception {
    dbObject.prepareSQL(READ_RECORD);
    DLBean resultBean;
    for (int i = 1; i <= pkNum; ++i) {
      if (metadata.types()[i - 1].equals(DLData.Integer)) {
        dbObject.setInt(i, (Integer) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean));
      } else if (metadata.types()[i - 1].equals(DLData.Double)) {
        dbObject.setDouble(i, (Double) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean));
      } else if (metadata.types()[i - 1].equals(DLData.Timestamp)) {
        dbObject.setTimestamp(i, (Timestamp) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean));
      } else {
        dbObject.setString(i, (String) callGetter(metadata.beanName(), metadata.properties()[i - 1], bean));
      }
    }
    List<List<String>> lResult = dbObject.lExecuteQuery();
    dbObject.closeStmt();
    if (lResult.size() == 0) {
      throw new Exception(DLLang.MESG500);
    } else {
      List<String> lRow = lResult.get(0);
      resultBean = (DLBean) Class.forName(metadata.beanName()).getDeclaredConstructor().newInstance();
      resultBean.setModelName(metadata.beanName());
      for (int j = 0; j < metadata.properties().length; ++j) {
        callSetter(metadata.beanName(), metadata.properties()[j], metadata.types()[j], resultBean, lRow.get(j));
      }
    }
    return resultBean;
  }

  @Override
  public List<DLBean> list(DBConn dbObject) throws Exception {
    dbObject.prepareSQL(LIST_RECORD);
    List<DLBean> lBean = new ArrayList<DLBean>();
    List<List<String>> lResult = dbObject.lExecuteQuery();
    dbObject.closeStmt();
    if (lResult.size() == 0) {
      throw new Exception(DLLang.MESG500);
    } else {
      for (int i = 0; i < lResult.size(); ++i) {
        List<String> lRow = lResult.get(i);
        DLBean bean = (DLBean) Class.forName(metadata.beanName()).getDeclaredConstructor().newInstance();
        bean.setModelName(metadata.beanName());
        for (int j = 0; j < metadata.properties().length; ++j) {
          callSetter(metadata.beanName(), metadata.properties()[j], metadata.types()[j], bean, lRow.get(j));
        }
        lBean.add(bean);
      }
    }
    return lBean;
  }

  @Override
  public List<DLBean> queryJoin(DBConn dbObject, String joinName) throws Exception {
    if (joinTables.size() <= 0) {
      throw new Exception(DLLang.MESG500);
    } else {
      JoinBean joinBean = getJoinBean(joinName);
      List<String> columns = Arrays.asList(joinBean.getColumns());
      dbObject.prepareSQL(joinBean.getSql());
      List<DLBean> lBean = new ArrayList<DLBean>();
      List<List<String>> lResult = dbObject.lExecuteQuery();
      dbObject.closeStmt();
      if (lResult.size() == 0) {
        throw new Exception(DLLang.MESG500);
      } else {
        for (int i = 0; i < lResult.size(); ++i) {
          int index = 0;
          List<String> lRow = lResult.get(i);
          DLBean bean = (DLBean) Class.forName(joinBean.getBeanName()).getDeclaredConstructor().newInstance();
          bean.setModelName(joinBean.getBeanName());
          for (int j = 0; j < joinBean.getProperties().length; ++j) {
            callSetter(joinBean.getBeanName(), joinBean.getProperties()[j], joinBean.getTypes()[j], bean, lRow.get(index++));
          }
          for (int j = 0; j < metadata.properties().length; ++j) {
            if (!columns.contains(metadata.columns()[j])) {
              callSetter(joinBean.getBeanName(), metadata.properties()[j], metadata.types()[j], bean, lRow.get(index++));
            }
          }
          lBean.add(bean);
        }
      }
      return lBean;
    }
  }

  @Override
  public List<List<String>> queryView(DBConn dbObject, String joinName) throws Exception {
    if (joinTables.size() <= 0) {
      throw new Exception(DLLang.MESG500);
    } else {
      JoinBean joinBean = getJoinBean(joinName);
      dbObject.prepareSQL(joinBean.getSql());
      List<List<String>> lResult = dbObject.lExecuteQuery();
      dbObject.closeStmt();
      if (lResult.size() == 0) {
        throw new Exception(DLLang.MESG500);
      } else {
        return lResult;
      }
    }
  }

  @Override
  public int[] updateJoin(DBConn dbObject, String joinName) throws Exception {
    DLBean bean = null;
    if (joinTables.size() <= 0) {
      throw new Exception(DLLang.MESG500);
    } else {
      JoinBean joinBean = getJoinBean(joinName);
      int[] rc = new int[joinBean.getSqls().length];
      for (int j = 0; j < joinBean.getSqls().length; ++j) {
        dbObject.prepareSQL(joinBean.getSqls()[j]);
        for (int i = 1; i <= joinBean.getProperties().length - pkNum; ++i) {
          if (joinBean.getTypes()[i + pkNum - 1].equals(DLData.Integer)) {
            dbObject.setInt(i, (Integer) callGetter(joinBean.getBeanName(), joinBean.getProperties()[i + pkNum - 1], bean));
          } else if (joinBean.getTypes()[i + pkNum - 1].equals(DLData.Double)) {
            dbObject.setDouble(i, (Double) callGetter(joinBean.getBeanName(), joinBean.getProperties()[i + pkNum - 1], bean));
          } else if (joinBean.getTypes()[i + pkNum - 1].equals(DLData.Binary)) {
            dbObject.setBinaryStream(i, (InputStream) callGetter(joinBean.getBeanName(), joinBean.getProperties()[i + pkNum - 1], bean), ((InputStream) callGetter(joinBean.getBeanName(), joinBean.getProperties()[i + pkNum - 1], bean)).available());
          } else if (joinBean.getTypes()[i + pkNum - 1].equals(DLData.Blob)) {
            dbObject.setBlob(i, (InputStream) callGetter(joinBean.getBeanName(), joinBean.getProperties()[i + pkNum - 1], bean), ((InputStream) callGetter(joinBean.getBeanName(), joinBean.getProperties()[i + pkNum - 1], bean)).available());
          } else if (joinBean.getTypes()[i + pkNum - 1].equals(DLData.Timestamp)) {
            dbObject.setTimestamp(i, (Timestamp) callGetter(joinBean.getBeanName(), joinBean.getProperties()[i + pkNum - 1], bean));
          } else {
            dbObject.setString(i, (String) callGetter(joinBean.getBeanName(), joinBean.getProperties()[i + pkNum - 1], bean));
          }
        }
        for (int i = 1; i <= pkNum; ++i) {
          if (joinBean.getTypes()[i - 1].equals(DLData.Integer)) {
            dbObject.setInt(joinBean.getProperties().length - pkNum + i, (Integer) callGetter(joinBean.getBeanName(), joinBean.getProperties()[i - 1], bean));
          } else if (joinBean.getTypes()[i - 1].equals(DLData.Double)) {
            dbObject.setDouble(joinBean.getProperties().length - pkNum + i, (Double) callGetter(joinBean.getBeanName(), joinBean.getProperties()[i - 1], bean));
          } else if (joinBean.getTypes()[i - 1].equals(DLData.Binary)) {
            dbObject.setBinaryStream(joinBean.getProperties().length - pkNum + i, (InputStream) callGetter(joinBean.getBeanName(), joinBean.getProperties()[i - 1], bean),
                ((InputStream) callGetter(joinBean.getBeanName(), joinBean.getProperties()[i - 1], bean)).available());
          } else if (joinBean.getTypes()[i - 1].equals(DLData.Blob)) {
            dbObject.setBlob(joinBean.getProperties().length - pkNum + i, (InputStream) callGetter(joinBean.getBeanName(), joinBean.getProperties()[i - 1], bean),
                ((InputStream) callGetter(joinBean.getBeanName(), joinBean.getProperties()[i - 1], bean)).available());
          } else if (joinBean.getTypes()[i - 1].equals(DLData.Double)) {
            dbObject.setTimestamp(joinBean.getProperties().length - pkNum + i, (Timestamp) callGetter(joinBean.getBeanName(), joinBean.getProperties()[i - 1], bean));
          } else {
            dbObject.setString(joinBean.getProperties().length - pkNum + i, (String) callGetter(joinBean.getBeanName(), joinBean.getProperties()[i - 1], bean));
          }
        }
        rc[j] = dbObject.executeUpdate();
        dbObject.closeStmt();
      }
      return rc;
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public String beanToJSON(DLBean bean) throws Exception {
    JSONObject json = new JSONObject();
    for (int i = 0; i < metadata.properties().length; ++i) {
      String properties = metadata.properties()[i];
      if (metadata.types()[i].equals(DLData.Integer)) {
        json.put(properties, (Integer) callGetter(metadata.beanName(), properties, bean));
      } else if (metadata.types()[i].equals(DLData.Double)) {
        json.put(properties, (Double) callGetter(metadata.beanName(), properties, bean));
      } else if (metadata.types()[i].equals(DLData.Binary)) {
        json.put(properties, (InputStream) callGetter(metadata.beanName(), properties, bean));
      } else if (metadata.types()[i].equals(DLData.Blob)) {
        json.put(properties, (InputStream) callGetter(metadata.beanName(), properties, bean));
      } else if (metadata.types()[i].equals(DLData.Timestamp)) {
        json.put(properties, (Timestamp) callGetter(metadata.beanName(), properties, bean));
      } else {
        json.put(properties, (String) callGetter(metadata.beanName(), properties, bean));
      }
    }
    return json.toString();
  }

  @Override
  @SuppressWarnings("unchecked")
  public String beansToJSON(List<DLBean> lBean) throws Exception {
    JSONArray jArray = new JSONArray();
    for (DLBean bean : lBean) {
      jArray.add(beanToJSON(bean));
    }
    return jArray.toString();
  }

  @Override
  @SuppressWarnings("unchecked")
  public String beansToJSON(List<DLBean> lBean, String joinName) throws Exception {
    JoinBean joinBean = getJoinBean(joinName);
    JSONArray jArray = new JSONArray();
    for (DLBean bean : lBean) {
      JSONObject json = new JSONObject();
      for (int i = 0; i < joinBean.getProperties().length; ++i) {
        String properties = joinBean.getProperties()[i];
        if (joinBean.getTypes()[i].equals(DLData.Integer)) {
          json.put(properties, (Integer) callGetter(joinBean.getBeanName(), properties, bean));
        } else if (joinBean.getTypes()[i].equals(DLData.Double)) {
          json.put(properties, (Double) callGetter(joinBean.getBeanName(), properties, bean));
        } else if (joinBean.getTypes()[i].equals(DLData.Binary)) {
          json.put(properties, (InputStream) callGetter(joinBean.getBeanName(), properties, bean));
        } else if (joinBean.getTypes()[i].equals(DLData.Blob)) {
          json.put(properties, (InputStream) callGetter(joinBean.getBeanName(), properties, bean));
        } else if (joinBean.getTypes()[i].equals(DLData.Timestamp)) {
          json.put(properties, (Timestamp) callGetter(joinBean.getBeanName(), properties, bean));
        } else {
          json.put(properties, (String) callGetter(joinBean.getBeanName(), properties, bean));
        }
      }
      for (int i = 0; i < metadata.properties().length; ++i) {
        String properties = metadata.properties()[i];
        if (metadata.types()[i].equals(DLData.Integer)) {
          json.put(properties, (Integer) callGetter(joinBean.getBeanName(), properties, bean));
        } else if (metadata.types()[i].equals(DLData.Double)) {
          json.put(properties, (Double) callGetter(joinBean.getBeanName(), properties, bean));
        } else if (metadata.types()[i].equals(DLData.Binary)) {
          json.put(properties, (InputStream) callGetter(joinBean.getBeanName(), properties, bean));
        } else if (metadata.types()[i].equals(DLData.Blob)) {
          json.put(properties, (InputStream) callGetter(joinBean.getBeanName(), properties, bean));
        } else if (metadata.types()[i].equals(DLData.Timestamp)) {
          json.put(properties, (Timestamp) callGetter(joinBean.getBeanName(), properties, bean));
        } else {
          json.put(properties, (String) callGetter(joinBean.getBeanName(), properties, bean));
        }
      }
      jArray.add(json);
    }
    return jArray.toString();
  }

  @Override
  public List<DLBean> toBean(List<List<String>> lTable) throws Exception {
    List<DLBean> lBean = new ArrayList<DLBean>();
    for (List<String> lRecord : lTable) {
      DLBean bean = (DLBean) Class.forName(metadata.beanName()).getDeclaredConstructor().newInstance();
      for (int i = 0; i < lRecord.size(); ++i) {
        callSetter(metadata.beanName(), metadata.properties()[i], metadata.types()[i], bean, lRecord.get(i));
      }
      lBean.add(bean);
    }
    return lBean;
  }

  @Override
  @SuppressWarnings("unchecked")
  public String toJSONTree(List<List<String>> lResult, String joinName) throws Exception {
    JoinBean joinBean = getJoinBean(joinName);
    JSONArray jArray = new JSONArray();
    DLTree aTree = new DLTree();
    aTree.setFields(joinBean.getColumns());
    aTree.buildTree();
    for (List<String> lRow : lResult) {
      aTree.setlRecord(lRow);
      jArray.add(aTree.toJSON());
    }
    return jArray.toString();
  }

  private Object callGetter(String beanName, String methodName, DLBean bean) throws Exception {
    Class<?> classBean = Class.forName(beanName);
    Method method = classBean.getDeclaredMethod("get" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1));
    Object value = method.invoke(bean);
    return value;
  }

  private void callSetter(String beanName, String methodName, DLData type, DLBean bean, String value) throws Exception {
    Class<?> cBean = Class.forName(beanName);
    Class<?>[] cArg = new Class[1];
    if (type.equals(DLData.Timestamp)) {
      cArg[0] = Class.forName("java.sql." + type.toString());
    } else {
      cArg[0] = Class.forName("java.lang." + type.toString());
    }
    Method method = cBean.getDeclaredMethod("set" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1), cArg);
    if (type.equals(DLData.Integer)) {
      method.invoke(bean, Integer.parseInt(value));
    } else if (type.equals(DLData.Double)) {
      method.invoke(bean, Double.parseDouble(value));
    } else if (type.equals(DLData.Timestamp)) {
      method.invoke(bean, Timestamp.valueOf(value));
    } else {
      method.invoke(bean, value);
    }
  }

  public static void callMethod(String className, String methodName, Class<?>[] methodParms, Object[] value) throws Exception {
    Class<?> classInstance = Class.forName(className);
    Method method = classInstance.getDeclaredMethod(methodName, methodParms);
    method.invoke(classInstance.getDeclaredConstructor().newInstance(), value);
  }

  private JoinBean getJoinBean(String joinName) throws Exception {
    for (JoinBean bean : joinTables) {
      if (bean.getJoinName().equals(joinName)) {
        return bean;
      }
    }
    return new JoinBean();
  }

  protected void generateBean() throws Exception {
    String packageName = metadata.beanName().substring(0, metadata.beanName().lastIndexOf(DLLang.PERIOD));
    String className = metadata.beanName().substring(metadata.beanName().lastIndexOf(DLLang.PERIOD) + 1);
    StringTokenizer st = new StringTokenizer(packageName, DLLang.PERIOD);
    String path = "src/";
    while (st.hasMoreTokens()) {
      path += st.nextToken() + "/";
    }
    FileWriter fw = new FileWriter(path + className + ".java");
    fw.write("package " + packageName + DLLang.END_OF_LINE + DLLang.NEW_LINE + DLLang.NEW_LINE);
    fw.write("import datlake.model.DLBean;" + DLLang.NEW_LINE + DLLang.NEW_LINE);
    fw.write("public class " + className + " extends DLBean {" + DLLang.NEW_LINE);
    for (int i = 0; i < metadata.types().length; ++i) {
      fw.write("  private " + metadata.types()[i] + DLLang.SPACE + metadata.properties()[i] + DLLang.END_OF_LINE + DLLang.NEW_LINE);
    }
    for (int i = 0; i < metadata.types().length; ++i) {
      fw.write(DLLang.NEW_LINE);
      fw.write("  public " + metadata.types()[i] + " get" + metadata.properties()[i].substring(0, 1).toUpperCase() + metadata.properties()[i].substring(1) + "() {" + DLLang.NEW_LINE);
      fw.write("    return " + metadata.properties()[i] + DLLang.END_OF_LINE + DLLang.NEW_LINE);
      fw.write("  }" + DLLang.NEW_LINE + DLLang.NEW_LINE);
      fw.write("  public void set" + metadata.properties()[i].substring(0, 1).toUpperCase() + metadata.properties()[i].substring(1) + "(" + metadata.types()[i] + " " + metadata.properties()[i] + ") {" + DLLang.NEW_LINE);
      fw.write("    this." + metadata.properties()[i] + " = " + metadata.properties()[i] + DLLang.END_OF_LINE + DLLang.NEW_LINE);
      fw.write("  }" + DLLang.NEW_LINE);
    }
    fw.write("}");
    fw.close();
  }

  protected void generateService() throws Exception {
    String packageName = metadata.beanName().substring(0, metadata.beanName().lastIndexOf(DLLang.PERIOD));
  }

  private void generateSQL() throws Exception {
    String dbSchema = ResourceBundle.getBundle("datasource").getString("Schema") + ".";
    // GENERATE CREATE STATEMENT
    StringBuilder sb = new StringBuilder().append("INSERT IGNORE INTO ").append(dbSchema).append(metadata.tableName()).append(" (");
    for (int i = 0; i < metadata.columns().length; ++i) {
      sb.append(metadata.columns()[i]);
      if (i != metadata.columns().length - 1) {
        sb.append(", ");
      }
    }
    sb.append(") VALUES (");
    for (int i = 0; i < metadata.columns().length; ++i) {
      sb.append("?");
      if (i != metadata.columns().length - 1) {
        sb.append(", ");
      }
    }
    sb.append(")");
    CREATE_RECORD = sb.toString();

    // GENERATE READ STATEMENT
    sb = new StringBuilder().append("SELECT ");
    for (int i = 0; i < metadata.columns().length; ++i) {
      sb.append(metadata.columns()[i]);
      if (i != metadata.columns().length - 1) {
        sb.append(", ");
      }
    }
    sb.append(" FROM ").append(dbSchema).append(metadata.tableName()).append(" WHERE ");
    for (int i = 0; i < pkNum; ++i) {
      sb.append(metadata.columns()[i]).append(" = ?");
      if (i != pkNum - 1) {
        sb.append(" AND ");
      }
    }
    READ_RECORD = sb.toString();

    // GENERATE UPDATE STATEMENT
    sb = new StringBuilder().append("UPDATE ").append(dbSchema).append(metadata.tableName()).append(" SET ");
    for (int i = pkNum; i < metadata.columns().length; ++i) {
      sb.append(metadata.columns()[i]).append(" = ?");
      if (i != metadata.columns().length - 1) {
        sb.append(", ");
      }
    }
    sb.append(" WHERE ");
    for (int i = 0; i < pkNum; ++i) {
      sb.append(metadata.columns()[i]).append(" = ?");
      if (i != pkNum - 1) {
        sb.append(" AND ");
      }
    }
    UPDATE_RECORD = sb.toString();

    // GENERATE DELETE STATEMENT
    sb = new StringBuilder().append("DELETE FROM ").append(dbSchema).append(metadata.tableName()).append(" WHERE ");
    for (int i = 0; i < pkNum; ++i) {
      sb.append(metadata.columns()[i]).append(" = ?");
      if (i != pkNum - 1) {
        sb.append(" AND ");
      }
    }
    DELETE_RECORD = sb.toString();

    // GENERATE LIST STATEMENT
    sb = new StringBuilder().append("SELECT ");
    for (int i = 0; i < metadata.columns().length; ++i) {
      sb.append(metadata.columns()[i]);
      if (i != metadata.columns().length - 1) {
        sb.append(", ");
      }
    }
    sb.append(" FROM ").append(dbSchema).append(metadata.tableName()).append(" ORDER BY ").append(metadata.columns()[0]);
    LIST_RECORD = sb.toString();

    // GENERATE JOIN STATEMENT
    for (JoinBean joinBean : joinTables) {
      sb = new StringBuilder().append("SELECT ");
      HashMap<String, String> htJoin = new HashMap<String, String>();
      int index = 1;
      for (String table : joinBean.getTables()) {
        if (htJoin.get(table) == null) {
          htJoin.put(table, "T" + index++);
        }
      }
      for (int i = 0; i < joinBean.getColumns().length; ++i) {
        sb.append(htJoin.get(joinBean.getTables()[i])).append(DLLang.PERIOD).append(joinBean.getColumns()[i].replaceAll(DLLang.BRACKETS, DLLang.EMPTY));
        if (i != joinBean.getColumns().length - 1) {
          sb.append(", ");
        }
      }
      sb.append(" FROM ").append(dbSchema).append(metadata.tableName()).append(" AS ").append(htJoin.get(metadata.tableName()));
      for (int i = 0; i < joinBean.getKeys().length; ++i) {
        String[] tbKey1 = joinBean.getKeys()[i][0].split(Pattern.quote(DLLang.PERIOD));
        String[] tbKey2 = joinBean.getKeys()[i][1].split(Pattern.quote(DLLang.PERIOD));
        sb.append(" LEFT OUTER JOIN ").append(dbSchema).append(tbKey2[0]).append(" AS ").append(htJoin.get(tbKey2[0])).append(" ON ");
        sb.append(htJoin.get(tbKey1[0])).append(DLLang.PERIOD).append(tbKey1[1]).append(" = ");
        sb.append(htJoin.get(tbKey2[0])).append(DLLang.PERIOD).append(tbKey2[1]);
        if (joinBean.getKeys()[i].length > 2) {
          for (int j = 2; j < joinBean.getKeys()[i].length; ++j) {
            sb.append(" AND ");
            tbKey1 = joinBean.getKeys()[i][j++].split(Pattern.quote(DLLang.PERIOD));
            tbKey2 = joinBean.getKeys()[i][j].split(Pattern.quote(DLLang.PERIOD));
            sb.append(htJoin.get(tbKey1[0])).append(DLLang.PERIOD).append(tbKey1[1]).append(" = ");
            sb.append(htJoin.get(tbKey2[0])).append(DLLang.PERIOD).append(tbKey2[1]);
          }
        }
      }
      if (joinBean.getFilters() != null && joinBean.getFilters().length > 0) {
        sb.append(" WHERE ").append(joinBean.getFilters()[0]);
        for (int i = 1; i < joinBean.getFilters().length; ++i) {
          sb.append(" AND ").append(joinBean.getFilters()[i]);
        }
      }
      joinBean.setSql(sb.toString());
    }
  }

  private void verification() throws Exception {
    int numOfFields = metadata.columns().length;
    if (metadata.properties().length != numOfFields || metadata.types().length != numOfFields) {
      throw new Exception(DLLang.MESG501);
    }
    for (JoinBean joinBean : joinTables) {
      numOfFields = joinBean.getColumns().length;
      if (joinBean.getProperties().length != numOfFields || joinBean.getTypes().length != numOfFields) {
        throw new Exception(DLLang.MESG501);
      }
    }
  }

  public List<JoinBean> getJoinTables() {
    return joinTables;
  }

  public void setJoinTables(List<JoinBean> joinTables) {
    this.joinTables = joinTables;
  }

  public int getPkNum() {
    return pkNum;
  }

  public void setPkNum(int pkNum) {
    this.pkNum = pkNum;
  }
}