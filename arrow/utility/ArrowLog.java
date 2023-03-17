package arrow.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Edmond Hoi. lmhoi@mpu.edu.mo
 * @Copyright 2023 March.
 * @Description: Log Management.
 */
public class ArrowLog {
  private static Logger logger;

  private String className = this.getClass().getName();

  public ArrowLog(Class<?> className) {
    logger = LogManager.getLogger(className.getName());
    this.className = ArrowLang.LEFT + className.getName() + ArrowLang.RIGHT;
  }

  public boolean debug(String mesg) {
    logger.debug(className + mesg);
    return false;
  }

  public boolean error(String mesg) {
    logger.error(className + mesg);
    return false;
  }

  public boolean info(String mesg) {
    logger.info(className + mesg);
    return false;
  }

  public boolean warn(String mesg) {
    logger.warn(className + mesg);
    return false;
  }
}
