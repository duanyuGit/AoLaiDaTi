package com.yunmall.dati.modal;

import java.io.Serializable;

/**
 * Created by wangsanjun on 18/1/9.
 */

public class BaseObject implements Serializable {

  /**
   * 题号
   */
  private String id;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
