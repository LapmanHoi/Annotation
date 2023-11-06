package datalake.utility;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;

import datalake.utility.DLTool.DLLocale;

/**
 * @author Edmond Hoi. lmhoi@mpu.edu.mo
 * @Copyright 2023 March.
 * @Description: System Language.
 */
public class DLLang {
  private static Hashtable<String, String> ptList, scList, tcList;
  private static DLLang instance = new DLLang();
  private static DLLocale currentLocale;

  public static final String AT = "@";
  public static final String BRACKETS = "[\\[\\]]";
  public static final String EMPTY = "";
  public static final String END_OF_LINE = ";";
  public static final String JOINING_METHOD = "joining";
  public static final String LEFT = "[";
  public static final String MAPPING_METHOD = "mapping";
  public static final String NEW_LINE = "\n";
  public static final String PERIOD = ".";
  public static final String SPACE = " ";
  public static final String RIGHT = "] ";
  public static final String UTF8 = "UTF-8";

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

  private DLLang() {
    ptList = new Hashtable<String, String>();
    scList = new Hashtable<String, String>();
    tcList = new Hashtable<String, String>();
    loadMesg(Locale.SIMPLIFIED_CHINESE, scList);
    loadMesg(Locale.TRADITIONAL_CHINESE, tcList);
    loadMesg(Locale.ENGLISH, ptList);
    currentLocale = DLLocale.SimplifiedChinese;
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
      DLTool.writeLog(DLLang.class).error(ex.toString());
    }
  }

  public String getWords(String lang) {
    String words = EMPTY;
    if (currentLocale.equals(DLLocale.TraditionalChinese)) {
      words = tcList.get(lang);
    } else if (currentLocale.equals(DLLocale.Portuguese)) {
      words = ptList.get(lang);
    } else {
      words = scList.get(lang);
    }
    return (words == null ? EMPTY : words);
  }

  public static DLLocale getLocale() {
    return currentLocale;
  }

  public static void switchLang(DLLocale locale) {
    currentLocale = locale;
  }

  public static void switchLang(int lang) throws Exception {
    if (lang == 0) {
      switchLang(DLLocale.SimplifiedChinese);
    } else if (lang == 1) {
      switchLang(DLLocale.TraditionalChinese);
    } else if (lang == 2) {
      switchLang(DLLocale.Portuguese);
    } else {
      throw new Exception(MESG505);
    }
  }

  public static DLLang getLang() {
    return instance;
  }
}