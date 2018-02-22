package com.yunmall.dati.modal;

/**
 * Created by wangsanjun on 18/1/24.
 * 搜狐返回的答案对象
 */

public class DaAn extends BaseObject {

  private String index;
  private String option;
  private String optionContent;
  private int pluginType;
  private String title;

  public String getIndex() {
    return index;
  }

  public void setIndex(String index) {
    this.index = index;
  }

  public String getOption() {
    return option;
  }

  public void setOption(String option) {
    this.option = option;
  }

  public String getOptionContent() {
    return optionContent;
  }

  public void setOptionContent(String optionContent) {
    this.optionContent = optionContent;
  }

  public int getPluginType() {
    return pluginType;
  }

  public void setPluginType(int pluginType) {
    this.pluginType = pluginType;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}

