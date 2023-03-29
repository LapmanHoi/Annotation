package arrow.utility;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;

import arrow.utility.ArrowTool.ArrowLocale;

/**
 * @author Edmond Hoi. lmhoi@mpu.edu.mo
 * @Copyright 2023 March.
 * @Description: System Language.
 */
public class ArrowLang {
  private static Hashtable<String, String> ptList, scList, tcList;
  private static ArrowLang instance = new ArrowLang();
  private static ArrowLocale currentLocale;

  public static final String ARROW = "@";
  public static final String BRACKETS = "[\\[\\]]";
  public static final String EMPTY = "";
  public static final String END_OF_LINE = ";";
  public static final String JAVA_FILE = ".java";
  public static final String JOINING_METHOD = "joining";
  public static final String LEFT = "[";
  public static final String MANAGER = "Manager";
  public static final String MAPPING_METHOD = "mapping";
  public static final String NEW_LINE = "\n";
  public static final String PERIOD = ".";
  public static final String ROOT = "src/";
  public static final String SPACE = " ";
  public static final String SLASH = "/";
  public static final String SERVICE = "Service";
  public static final String TEMPLATE = "\\arrow\\service\\";
  public static final String RIGHT = "] ";
  public static final String UTF8 = "UTF-8";
  public static final String[] TAGS = { "<CLASS_NAME/>", "<MANAGER_NAME/>", "<PACKAGE_NAME/>", "<PATH_NAME/>" };
  /**
   * SUCCESS!
   */
  public static final String MESG200 = "SUCCESS!";
  /**
   * NO CONTENT!
   */
  public static final String MESG500 = "NO CONTENT!";
  /**
   * NUMBER OF FIELDS IS NOT MATCH!
   */
  public static final String MESG501 = "THE NUMBER OF FIELDS IS NOT MATCH!";
  /**
   * INVALID FORMAT!
   */
  public static final String MESG502 = "INVALID FORMAT!";
  /**
   * INVALUD DATA!
   */
  public static final String MESG503 = "INVALID DATA!";
  /**
   * UNAUTHORIZED!
   */
  public static final String MESG504 = "UNAUTHORIZED!";
  /**
   * REQUEST NOT FOUND!
   */
  public static final String MESG505 = "REQUEST NOT FOUND!";
  /**
   * REQUEST TIMEOUT!
   */
  public static final String MESG506 = "REQUEST TIMEOUT!";
  /**
   * BAD REQUEST!
   */
  public static final String MESG507 = "BAD REQUEST!";
  /**
   * SERVER ERROR!
   */
  public static final String MESG508 = "INTERNAL SERVER ERROR!";
  /**
   * MISSING LEFT
   */
  public static final String MESG509 = "SYNTAX ERROR: MISSING LEFT!";
  /**
   * MISSING RIGHT
   */
  public static final String MESG510 = "SYNTAX ERROR: MISSING RIGHT!";

  private ArrowLang() {
    ptList = new Hashtable<String, String>();
    scList = new Hashtable<String, String>();
    tcList = new Hashtable<String, String>();
    loadMesg(Locale.SIMPLIFIED_CHINESE, scList);
    loadMesg(Locale.TRADITIONAL_CHINESE, tcList);
    loadMesg(Locale.ENGLISH, ptList);
    currentLocale = ArrowLocale.SimplifiedChinese;
  }

  private void loadMesg(Locale locale, Hashtable<String, String> langList) {
    try {
      ResourceBundle rb = ResourceBundle.getBundle("language", locale);
      Enumeration<String> keys = rb.getKeys();
      while (keys.hasMoreElements()) {
        String key = keys.nextElement();
        langList.put(key, rb.getString(key));
      }
    } catch (Exception ex) {
      ArrowTool.writeLog(ArrowLang.class).error(ex.toString());
    }
  }

  public String getWords(String lang) {
    String words = EMPTY;
    if (currentLocale.equals(ArrowLocale.TraditionalChinese)) {
      words = tcList.get(lang);
    } else if (currentLocale.equals(ArrowLocale.Portuguese)) {
      words = ptList.get(lang);
    } else {
      words = scList.get(lang);
    }
    return (words == null ? EMPTY : words);
  }

  public static ArrowLocale getLocale() {
    return currentLocale;
  }

  public static void switchLang(ArrowLocale locale) {
    currentLocale = locale;
  }

  public static void switchLang(int lang) throws Exception {
    if (lang == 0) {
      switchLang(ArrowLocale.SimplifiedChinese);
    } else if (lang == 1) {
      switchLang(ArrowLocale.TraditionalChinese);
    } else if (lang == 2) {
      switchLang(ArrowLocale.Portuguese);
    } else {
      throw new Exception(MESG505);
    }
  }

  public static ArrowLang getLang() {
    return instance;
  }
}