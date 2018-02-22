package com.yunmall.dati.modal;

/**
 * Created by wangsanjun on 18/1/9.
 *
 */

public class DaTiType extends BaseObject{
  private int appType;

  public int getAppType() {
    return appType;
  }

  public void setAppType(int appType) {
    this.appType = appType;
  }

  public DaTiType(int pAppType) {
    appType = pAppType;
  }
}
