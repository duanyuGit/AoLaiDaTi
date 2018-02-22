package com.yunmall.dati.job;

import android.view.accessibility.AccessibilityEvent;
import com.yunmall.dati.DaTiService;

public class XiguaJob extends TodayNewsJob {

  /**
   * 西瓜视频包名
   */
  public static final String TODAY_PACKAGE_NAME = "com.ss.android.article.video";

  @Override public void onCreateJob(DaTiService service) {
    super.onCreateJob(service);
  }

  @Override public void onStopJob() {
  }

  @Override public String getTargetPackageName() {
    return TODAY_PACKAGE_NAME;
  }

  @Override public void onReceiveJob(AccessibilityEvent event) {
    super.onReceiveJob(event);
  }

}
