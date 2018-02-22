package com.yunmall.dati.job;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;
import com.yunmall.dati.BuildConfig;
import com.yunmall.dati.Config;
import com.yunmall.dati.DaTiService;

/**
 * 辅助任务基类
 */
public abstract class BaseAccessbilityJob implements AccessbilityJob {
  private DaTiService service;
  private String mLastTitle;

  @Override public void onCreateJob(DaTiService service) {
    this.service = service;
  }

  public Context getContext() {
    return service.getApplicationContext();
  }

  public Config getConfig() {
    return service.getConfig();
  }

  public DaTiService getService() {
    return service;
  }

  /**
   * 网络上传答题数据
   */
  private void requestData(String strData) {
    //这一步是上传到我自己的服务器，用于网页端展示百度搜索结果，我就省略了。
  }

  /**
   * 上传数据，用于网页端展示百度搜索结果
   *
   * @param strTitleNum 题号
   * @param strTitle 题目
   * @param strAnswer 答案
   */
  public void postData(String strTitleNum, String strTitle, String strAnswer) {
    if (TextUtils.isEmpty(mLastTitle) || !strTitle.equals(mLastTitle)) {
      mLastTitle = strTitle;
      if (BuildConfig.DEBUG) {
        Toast.makeText(getContext(), strTitle + "\n" + strAnswer, Toast.LENGTH_LONG).show();
      }
      String strData = getPostData(strTitleNum, strTitle, strAnswer);
      requestData(strData);
    }
  }

  /**
   * 上传数据，用于网页端展示百度搜索结果
   *
   * @param strTitle 题目
   * @param strAnswer 答案
   */
  public void postData(String strTitle, String strAnswer) {
    String[] strNumAndContent = fetchQuestionNumAndContent(strTitle);
    String strNum = "";
    String strQuestionContent;
    if (strNumAndContent.length >= 2) {
      strNum = strNumAndContent[0];
      strQuestionContent = strNumAndContent[1];
    } else {
      strQuestionContent = strTitle;
    }
    postData(strNum, strQuestionContent, TextUtils.isEmpty(strAnswer) ? "" : strAnswer);
  }

  /**
   * 组装上传数据
   * @param strNum 题号
   * @param questionTitle 题目
   * @param answer 答案
   */
  private String getPostData(String strNum, String questionTitle, String answer) {
    // 组装上传数据，我就省略了。
    return "";
  }

  /**
   * 将题目按照.分割成题号和题目
   */
  protected String[] fetchQuestionNumAndContent(String questionTitle) {
    return questionTitle.split("\\.");
  }
}
