package com.yunmall.dati.modal;

/**
 * Created by wangsanjun on 18/1/9.
 */

public class Question extends BaseObject{
  /**
   * 问题
   */
  private String question;
  /**
   * 答案
   */
  private String answer;


  private String platform;

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }
}
