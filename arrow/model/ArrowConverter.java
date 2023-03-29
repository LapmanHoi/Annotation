package arrow.model;

import java.util.List;

/**
 * @author Edmond Hoi. lmhoi@mpu.edu.mo
 * @Copyright 2023 March.
 * @Description: Arrow Converter.
 */
public interface ArrowConverter {

  /**
   * Convert the current mapping JavaBean to JSON format
   * 
   * @param bean represents the current mapping table
   * @return a string in JSON format
   * @throws Exception
   */
  public String beanToJSON(ArrowBean bean) throws Exception;

  /**
   * Convert a list of current mapping JavaBean to JSON format
   * 
   * @param lBean list of JavaBean
   * @return a string in JSON format
   * @throws Exception
   */
  public String beansToJSON(List<ArrowBean> lBean) throws Exception;

  /**
   * Convert a list of JavaBean (joining tables) to JSON format
   * 
   * @param lBean    list of JavaBean
   * @param joinName name for joining other tables
   * @return a string in JSON format
   * @throws Exception
   */
  public String beansToJSON(List<ArrowBean> lBean, String joinName) throws Exception;

  /**
   * Convert the list of String to JavaBean
   * 
   * @param lTable list of String
   * @return List of JavaBean
   * @throws Exception
   */
  public List<ArrowBean> toBean(List<List<String>> lTable) throws Exception;

  /**
   * Convert a list of results (joining tables) to JSON Tree format structure
   * 
   * @param lBean    list of result
   * @param joinName name for joining other tables
   * @return a string in JSON format
   * @throws Exception
   */
  public String toJSONTree(List<List<String>> lResult, String joinName) throws Exception;
}