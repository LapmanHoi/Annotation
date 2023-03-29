package arrow.txn;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import arrow.data.DBConn;
import arrow.model.ArrowBean;

/**
 * @author Edmond Hoi. lmhoi@mpu.edu.mo
 * @Copyright 2023 March.
 * @Description: Arrow Transaction.
 */
public abstract class ArrowTxn {
  /**
   * Page to flow after the transaction process
   */
  private String resultPage;

  /**
   * JavaBean to hold login information
   */
  private ArrowBean loginInfo;

  /**
   * JavaBean to hold transaction information
   */
  private List<ArrowBean> txnInfo;

  /**
   * To implement the business logic.
   *
   * @param request  HTTP request from client
   * @param dbObject database connection object
   * @return List of Javabeans to hold the transactional data
   * @throws Exception
   */
  public abstract void process(HttpServletRequest request, DBConn dbObject) throws Exception;

  public String getResultPage() {
    return resultPage;
  }

  public void setResultPage(String resultPage) {
    this.resultPage = resultPage;
  }

  public ArrowBean getLoginInfo() {
    return loginInfo;
  }

  public void setLoginInfo(ArrowBean loginInfo) {
    this.loginInfo = loginInfo;
  }

  public List<ArrowBean> getTxnInfo() {
    return txnInfo;
  }

  public void setTxnInfo(List<ArrowBean> txnInfo) {
    this.txnInfo = txnInfo;
  }
}