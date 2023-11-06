package datalake.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import datalake.data.DBConn;
import datalake.trans.DLTrans;
import datalake.utility.DLException;
import datalake.utility.DLLang;
import datalake.utility.DLTool;

/**
 * @author Edmond Hoi. lmhoi@mpu.edu.mo
 * @Copyright 2023 March.
 * @Description: Main Controller.
 */
@MultipartConfig
@WebServlet("/DLPortal")
public class DLPortal extends HttpServlet {
  private static final long serialVersionUID = 888L;
  private static final String[] FILE_TYPE = { ".html", ".jsp" };
  private static String classpath;
  private static long counter = 0;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    DLTool.writeLog(this.getClass()).warn("Client is using ``GET'' protocol!");
    doPost(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
      request.setCharacterEncoding(DLLang.UTF8);
      // Create Transaction Instance
      DLTool.writeLog(this.getClass()).info("This is the " + (++counter) + "th visit.");
      HttpSession session = request.getSession(true);
      String lang = request.getParameter("Lang");
      lang = (lang != null) ? lang : (String) session.getAttribute("Lang");
      lang = (lang != null) ? lang : "0";
      session.setAttribute("Lang", lang);
      DLLang.switchLang(Integer.parseInt(lang));
      String txnName = request.getParameter("TxnName");
      DLTool.writeLog(this.getClass()).info("*** Transaction: " + txnName + " from: " + request.getRemoteAddr().toString() + " ***");
      if (txnName.length() > 20 || txnName.indexOf("'") > 0) {
        DLException.showMessage(request, response, DLLang.MESG505);
      }
      if (classpath == null || classpath.length() == 0) {
        classpath = getServletConfig().getInitParameter("PackagePath") + DLLang.PERIOD;
      }
      DLTrans trans = (DLTrans) Class.forName(classpath + txnName).getDeclaredConstructor().newInstance();
      // Process Transaction
      DBConn dbObject = new DBConn();
      trans.doTrans(request, dbObject);
      dbObject.closeConn();
      if (txnName.indexOf("Logout") >= 0) {
        if (session != null) {
          session.setMaxInactiveInterval(1);
        }
      } else {
        if (txnName.indexOf("Login") >= 0) {
          session.setAttribute("LoginInfo", trans.getLoginInfo());
        } else {
          session.setAttribute("TxnInfo", trans.getTxnInfo());
        }
      }
      if (trans.getResultPage().endsWith(FILE_TYPE[0]) || trans.getResultPage().endsWith(FILE_TYPE[1])) {
        response.sendRedirect(trans.getResultPage());
      } else {
        RequestDispatcher rd = request.getRequestDispatcher("DLPortal?TxnName=" + trans.getResultPage());
        rd.forward(request, response);
      }
    } catch (ClassNotFoundException cnfe) {
      DLException.showMessage(request, response, DLLang.MESG505);
      cnfe.printStackTrace();
    } catch (Exception err) {
      DLException.showMessage(request, response, err);
    }
  }
}