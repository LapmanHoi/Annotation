package datalake.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import datalake.model.TreeBean;
import datalake.utility.DLLang;
import datalake.utility.DLTool;

/**
 * @author Edmond Hoi. lmhoi@mpu.edu.mo
 * @Copyright 2023 March.
 * @Description: DL Tree.
 */
public class DLTree {
  private List<TreeBean> treeBeanList;
  private List<String> lRecord;
  private String[] fields;

  public void buildTree() throws Exception {
    treeBeanList = new ArrayList<TreeBean>();
    if (verify()) {
      readTreeNodes();
    }
  }

  public JSONObject toJSON() throws Exception {
    JSONObject json = toTreeObj(0, new JSONObject());
    DLTool.writeLog(this.getClass()).info(json.toString());
    return json;
  }

  private void readTreeNodes() throws Exception {
    int levelId = 1;
    for (int i = 0; i < fields.length; ++i) {
      String field = fields[i];
      TreeBean bean = new TreeBean();
      bean.setColId(i + 1);
      for (char ch : field.toCharArray()) {
        if (ch == DLLang.LEFT.charAt(0)) {
          levelId++;
        }
      }
      bean.setLevelId(levelId);
      for (char ch : field.toCharArray()) {
        if (ch == DLLang.RIGHT.charAt(0)) {
          levelId--;
        }
      }
      field = field.replaceAll(DLLang.BRACKETS, DLLang.EMPTY);
      bean.setColName(field);
      treeBeanList.add(bean);
    }
    Collections.sort(treeBeanList, new TreeSort());
  }

  @SuppressWarnings("unchecked")
  private JSONArray toTreeArr(int id, JSONArray jArray) {
    TreeBean bean = treeBeanList.get(id);
    JSONObject jObject = new JSONObject();
    jObject.put(bean.getColName(), lRecord.get(bean.getColId() - 1));
    if (id + 1 == treeBeanList.size()) {
      jArray.add(jObject);
      return jArray;
    } else {
      TreeBean nextBean = treeBeanList.get(id + 1);
      if (nextBean.getLevelId() > bean.getLevelId()) {
        jObject.put(nextBean.getColName(), toTreeArr(id + 1, new JSONArray()));
        jArray.add(jObject);
        return jArray;
      } else {
        jArray.add(toTreeObj(id + 1, jObject));
        return jArray;
      }
    }
  }

  @SuppressWarnings("unchecked")
  private JSONObject toTreeObj(int id, JSONObject jObject) {
    TreeBean bean = treeBeanList.get(id);
    jObject.put(bean.getColName(), lRecord.get(bean.getColId() - 1));
    if (id + 1 == treeBeanList.size()) {
      return jObject;
    } else {
      TreeBean nextBean = treeBeanList.get(id + 1);
      if (nextBean.getLevelId() > bean.getLevelId()) {
        jObject.put(nextBean.getColName(), toTreeArr(id + 1, new JSONArray()));
        return jObject;
      } else {
        return toTreeObj(id + 1, jObject);
      }
    }
  }

  private boolean verify() throws Exception {
    String path = DLLang.EMPTY;
    for (String field : fields) {
      path += field;
    }
    System.out.println(path);
    int counter = 0;
    for (int i = 0; i < path.length(); ++i) {
      if (path.charAt(i) == DLLang.LEFT.charAt(0)) {
        counter++;
      } else if (path.charAt(i) == DLLang.RIGHT.charAt(0)) {
        counter--;
      } else {
      }
    }
    if (counter > 0) {
      DLTool.writeLog(this.getClass()).error(DLLang.MESG510);
      return false;
    } else if (counter < 0) {
      DLTool.writeLog(this.getClass()).error(DLLang.MESG509);
      return false;
    } else {
      return true;
    }
  }

  public String[] getFields() {
    return this.fields;
  }

  public void setFields(String[] fields) {
    this.fields = fields;
  }

  public List<String> getlRecord() {
    return lRecord;
  }

  public void setlRecord(List<String> lRecord) {
    this.lRecord = lRecord;
  }
}

class TreeSort implements Comparator<TreeBean> {
  @Override
  public int compare(TreeBean b1, TreeBean b2) {
    return b1.getLevelId() - b2.getLevelId();
  }
}