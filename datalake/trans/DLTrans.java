package datalake.trans;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import datalake.data.DBConn;
import datalake.model.DLBean;

/**
 * @author Edmond Hoi. lmhoi@mpu.edu.mo
 * @Copyright 2023 March.
 * @Description: DL Transaction.
 */
public abstract class DLTrans {
  /**
   * Page to flow after the transaction process
   */
  private String resultPage;

  /**
   * JavaBean to hold login information
   */
  private DLBean loginInfo;

  /**
   * JavaBean to hold transaction information
   */
  private List<DLBean> txnInfo;

  /**
   * To implement the business logic.
   *
   * @param request  HTTP request from client
   * @param dbObject database connection object
   * @return List of Javabeans to hold the transactional data
   * @throws Exception
   */
  public abstract void doTrans(HttpServletRequest request, DBConn dbObject) throws Exception;

  public String getResultPage() {
    return resultPage;
  }

  public void setResultPage(String resultPage) {
    this.resultPage = resultPage;
  }

  public DLBean getLoginInfo() {
    return loginInfo;
  }

  public void setLoginInfo(DLBean loginInfo) {
    this.loginInfo = loginInfo;
  }

  public List<DLBean> getTxnInfo() {
    return txnInfo;
  }

  public void setTxnInfo(List<DLBean> txnInfo) {
    this.txnInfo = txnInfo;
  }
}