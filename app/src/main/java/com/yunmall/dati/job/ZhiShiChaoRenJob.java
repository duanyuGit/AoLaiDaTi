package com.yunmall.dati.job;

import android.view.accessibility.AccessibilityEvent;
import com.yunmall.dati.Config;
import com.yunmall.dati.DaTiService;

/**
 * 芝士超人辅助任务类
 */
public class ZhiShiChaoRenJob extends YingKeJob {

  /**
   * 芝士包名
   **/
  public static final String ZHI_SHI_PACKAGE_NAME = "com.inke.trivia";

  @Override public void onCreateJob(DaTiService service) {
    super.onCreateJob(service);
  }

  @Override public void onStopJob() {
  }

  @Override public String getTargetPackageName() {
    return ZHI_SHI_PACKAGE_NAME;
  }

  protected String getQuestionId() {
    return Config.ZHI_SHI_CHAO_REN_KE_QUESTION_ID;
  }

  protected String getAnswerId() {
    return Config.ZHI_SHI_CHAO_REN_ANSWER_ID;
  }

  @Override public void onReceiveJob(AccessibilityEvent event) {
    super.onReceiveJob(event);
  }
}
