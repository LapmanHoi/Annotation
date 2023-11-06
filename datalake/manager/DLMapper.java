package datalake.manager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import datalake.utility.DLTool.DLData;

/**
 * @author Edmond Hoi. lmhoi@mpu.edu.mo
 * @Copyright 2023 March.
 * @Description: Data Mapping.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DLMapper {

  /**
   * @return JavaBean name
   */
  public String beanName();

  /**
   * @return Table column names
   */
  public String[] columns();

  /**
   * @return JavaBean property names
   */
  public String[] properties();

  /**
   * @return Table name
   */
  public String tableName();

  /**
   * @return Data types
   */
  public DLData[] types();
}