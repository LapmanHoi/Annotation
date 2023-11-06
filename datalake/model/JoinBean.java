package datalake.model;

import datalake.utility.DLTool.DLData;

/**
 * @author Edmond Hoi. lmhoi@mpu.edu.mo
 * @Copyright 2023 March.
 * @Description Data Model JavaBean.
 */
public class JoinBean {
  private String beanName, joinName, sql;
  private String[] columns, filters, properties, sqls, tables;
  private String[][] keys;
  private DLData[] types;

  public String getBeanName() {
    return beanName;
  }

  public void setBeanName(String beanName) {
    this.beanName = beanName;
  }

  public String getJoinName() {
    return joinName;
  }

  public void setJoinName(String joinName) {
    this.joinName = joinName;
  }

  public String getSql() {
    return sql;
  }

  public void setSql(String sql) {
    this.sql = sql;
  }

  public String[] getColumns() {
    return columns;
  }

  public void setColumns(String[] columns) {
    this.columns = columns;
  }

  public String[] getFilters() {
    return filters;
  }

  public void setFilters(String[] filters) {
    this.filters = filters;
  }

  public String[] getProperties() {
    return properties;
  }

  public void setProperties(String[] properties) {
    this.properties = properties;
  }

  public String[] getSqls() {
    return sqls;
  }

  public void setSqls(String[] sqls) {
    this.sqls = sqls;
  }

  public String[] getTables() {
    return tables;
  }

  public void setTables(String[] tables) {
    this.tables = tables;
  }

  public String[][] getKeys() {
    return keys;
  }

  public void setKeys(String[][] keys) {
    this.keys = keys;
  }

  public DLData[] getTypes() {
    return types;
  }

  public void setTypes(DLData[] types) {
    this.types = types;
  }
}