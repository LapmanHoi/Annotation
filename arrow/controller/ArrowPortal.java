package arrow.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import arrow.data.DBConn;
import arrow.txn.ArrowTxn;
import arrow.utility.ArrowException;
import arrow.utility.ArrowLang;
import arrow.utility.ArrowTool;

/**
 * @author Edmond Hoi. lmhoi@mpu.edu.mo
 * @Copyright 2023 March.
 * @Description: Main Controller.
 */
@MultipartConfig
@WebServlet("/ArrowPortal")
public class ArrowPortal extends HttpServlet {
  private static final long serialVersionUID = 888L;
  private static final String[] FILE_TYPE = { ".html", ".jsp" };
  private static String classpath;
  private static long counter = 0;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    ArrowTool.writeLog(this.getClass()).warn("Client is using ``GET'' protocol!");
    doPost(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
      request.setCharacterEncoding(ArrowLang.UTF8);
      // Create Transaction Instance
      ArrowTool.writeLog(this.getClass()).info("This is the " + (++counter) + "th visit.");
      HttpSession session = request.getSession(true);
      String lang = request.getParameter("Lang");
      lang = (lang != null) ? lang : (String) session.getAttribute("Lang");
      lang = (lang != null) ? lang : "0";
      session.setAttribute("Lang", lang);
      ArrowLang.switchLang(Integer.parseInt(lang));
      String txnName = request.getParameter("TxnName");
      ArrowTool.writeLog(this.getClass()).info("*** Transaction: " + txnName + " from: " + request.getRemoteAddr().toString() + " ***");
      if (txnName.length() > 20 || txnName.indexOf("'") > 0) {
        ArrowException.showMessage(request, response, ArrowLang.MESG505);
      }
      if (classpath == null || classpath.length() == 0) {
        classpath = getServletConfig().getInitParameter("PackagePath") + ArrowLang.PERIOD;
      }
      ArrowTxn txn = (ArrowTxn) Class.forName(classpath + txnName).getDeclaredConstructor().newInstance();
      // Process Transaction
      DBConn dbObject = new DBConn();
      txn.process(request, dbObject);
      dbObject.closeConn();
      if (txnName.indexOf("Logout") >= 0) {
        if (session != null) {
          session.setMaxInactiveInterval(1);
        }
      } else {
        if (txnName.indexOf("Login") >= 0) {
          session.setAttribute("LoginInfo", txn.getLoginInfo());
        } else {
          session.setAttribute("TxnInfo", txn.getTxnInfo());
        }
      }
      if (txn.getResultPage().endsWith(FILE_TYPE[0]) || txn.getResultPage().endsWith(FILE_TYPE[1])) {
        response.sendRedirect(txn.getResultPage());
      } else {
        RequestDispatcher rd = request.getRequestDispatcher("ArrowPortal?TxnName=" + txn.getResultPage());
        rd.forward(request, response);
      }
    } catch (ClassNotFoundException cnfe) {
      ArrowException.showMessage(request, response, ArrowLang.MESG505);
      cnfe.printStackTrace();
    } catch (Exception err) {
      ArrowException.showMessage(request, response, err);
    }
  }
}