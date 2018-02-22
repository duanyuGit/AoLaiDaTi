package com.yunmall.dati.modal;

import java.util.ArrayList;

/**
 * Created by wangsanjun on 18/1/24.
 * 所有平台的答案
 */

public class DaAns extends BaseObject{
  private ArrayList<DaAn> results;
  private String title;

  public ArrayList<DaAn> getResults() {
    return results;
  }

  public void setResults(ArrayList<DaAn> results) {
    this.results = results;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
