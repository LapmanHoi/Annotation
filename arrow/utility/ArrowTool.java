package arrow.utility;

import java.sql.Timestamp;

/**
 * @author Edmond Hoi. lmhoi@mpu.edu.mo
 * @Copyright 2023 March.
 * @Description: Utility and Tool.
 */
public class ArrowTool {
  public static enum ArrowData {
    Binary, Blob, Double, Integer, String, Timestamp
  };

  public static enum ArrowLocale {
    SimplifiedChinese, TraditionalChinese, Portuguese
  };

  public static enum ArrowJoinMethod {
    INNER("="), FULL("+"), LEFT("<"), MINUS("-"), RIGHT(">"), UNION("*");

    public String value;

    private ArrowJoinMethod(String value) {
      this.value = value;
    }
  }

  public static Timestamp getCurrentTime() {
    return new Timestamp(System.currentTimeMillis());
  }

  public static ArrowLog writeLog(Class<?> className) {
    return new ArrowLog(className);
  }
}