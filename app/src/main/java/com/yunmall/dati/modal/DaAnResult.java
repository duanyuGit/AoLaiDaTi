package com.yunmall.dati.modal;

/**
 * Created by wangsanjun on 18/1/24.
 */

public class DaAnResult extends BaseObject {
  private DaAns data;
  private int errorCode;
  private String message;
  private String platform;

  public DaAns getData() {
    return data;
  }

  public void setData(DaAns data) {
    this.data = data;
  }

  public int getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(int errorCode) {
    this.errorCode = errorCode;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }
}
