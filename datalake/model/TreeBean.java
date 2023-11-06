package datalake.model;

/**
 * @author Edmond Hoi. lmhoi@mpu.edu.mo
 * @Copyright 2023 March.
 * @Description Data Model JavaBean.
 */
public class TreeBean {
  private int colId, frequency, levelId;
  private String colName;

  public int getColId() {
    return colId;
  }

  public void setColId(int colId) {
    this.colId = colId;
  }

  public int getFrequency() {
    return frequency;
  }

  public void setFrequency(int frequency) {
    this.frequency = frequency;
  }

  public int getLevelId() {
    return levelId;
  }

  public void setLevelId(int levelId) {
    this.levelId = levelId;
  }

  public String getColName() {
    return colName;
  }

  public void setColName(String colName) {
    this.colName = colName;
  }
}