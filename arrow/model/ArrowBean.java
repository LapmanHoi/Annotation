package arrow.model;

/**
 * @author Edmond Hoi. lmhoi@mpu.edu.mo
 * @Copyright 2023 March.
 * @Description: Data Model JavaBean.
 */
public abstract class ArrowBean {
  public String modelName, txnName;

  public String getModelName() {
    return modelName;
  }

  public void setModelName(String modelName) {
    this.modelName = modelName;
  }

  public String getTxnName() {
    return txnName;
  }

  public void setTxnName(String txnName) {
    this.txnName = txnName;
  }
}