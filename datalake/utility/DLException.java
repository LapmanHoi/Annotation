package datalake.utility;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Edmond Hoi. lmhoi@mpu.edu.mo
 * @Copyright 2023 March.
 * @Description: Exception Handler.
 */
public class DLException {
  public static void showMessage(HttpServletRequest request, HttpServletResponse response, Exception exception) {
    exception.printStackTrace();
    DLTool.writeLog(DLException.class).error(exception.getMessage());
    HttpSession session = request.getSession(true);
    session.setAttribute("msg", exception.getMessage());
    try {
      request.getRequestDispatcher("errorPage.jsp").forward(request, response);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    } catch (ServletException se) {
      se.printStackTrace();
    }
  }

  public static void showMessage(HttpServletRequest request, HttpServletResponse response, String msg) {
    System.err.println(msg);
    DLTool.writeLog(DLException.class).error(msg);
    HttpSession session = request.getSession(true);
    session.setAttribute("msg", msg);
    try {
      request.getRequestDispatcher("errorPage.jsp").forward(request, response);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    } catch (ServletException se) {
      se.printStackTrace();
    }
  }
}