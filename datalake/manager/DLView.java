package datalake.manager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import datalake.utility.DLTool.DLData;

/**
 * @author Edmond Hoi. lmhoi@mpu.edu.mo
 * @Copyright 2023 March.
 * @Description: DL View.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DLView {
  /**
   * @return JavaBean name
   */
  public String beanName();

  /**
   * @return JavaBean property names
   */
  public String[] properties();

  /**
   * @return Data types
   */
  public DLData[] types();

  /**
   * @return Table column names
   */
  public String[] viewFields();

  /**
   * @return Table key names
   */
  public String[] viewKeys();

  /**
   * @return The transaction name of this view
   */
  public String viewName();

  /**
   * @return Column belongs to the table
   */
  public String[] viewTables();
}