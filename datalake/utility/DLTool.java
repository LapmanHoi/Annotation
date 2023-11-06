package datalake.utility;

import java.sql.Timestamp;

/**
 * @author Edmond Hoi. lmhoi@mpu.edu.mo
 * @Copyright 2023 March.
 * @Description: Utility and Tool.
 */
public class DLTool {
  public static enum DLData {
    Binary, Blob, Double, Integer, String, Timestamp
  };

  public static enum DLLocale {
    SimplifiedChinese, TraditionalChinese, Portuguese
  };

  public static Timestamp getCurrentTime() {
    return new Timestamp(System.currentTimeMillis());
  }

  public static DLLog writeLog(Class<?> className) {
    return new DLLog(className);
  }
}