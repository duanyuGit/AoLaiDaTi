package com.yunmall.dati.job;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;
import com.yunmall.dati.DaTiApplication;
import com.yunmall.dati.DaTiService;
import com.yunmall.dati.util.AccessibilityHelper;
import java.util.List;

import static com.yunmall.dati.util.AccessibilityHelper.findNodeInfosById;

/**
 * 冲顶大会辅助任务类
 */
public class ChongDingJob extends BaseAccessbilityJob {

  private static final String TAG = "ChongDingJob";

  /** 冲顶包名 */
  public static final String CHONG_DING_PACKAGE_NAME = "com.chongdingdahui.app";

  @Override public void onCreateJob(DaTiService service) {
    super.onCreateJob(service);
  }

  @Override public void onStopJob() {
  }

  @Override public String getTargetPackageName() {
    return CHONG_DING_PACKAGE_NAME;
  }



  @Override public void onReceiveJob(AccessibilityEvent event) {
    final int eventType = event.getEventType();
    if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
      if (event.getClassName().equals("com.chongdingdahui.app.ui.LiveActivity")) {
        Toast.makeText(getContext(), "冲顶直播", Toast.LENGTH_LONG).show();
      }
      fetchContent();
    } else if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
      fetchContent();
    }
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN) private void fetchContent() {
    fetchTodayContent();
    DaTiApplication.setZhiBoType(2);
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2) private void fetchTodayContent() {

    AccessibilityNodeInfo nodeInfo = getService().getRootInActiveWindow();
    if (nodeInfo == null) {
      Log.w(TAG, "rootWindow为空");
      return;
    }

    AccessibilityNodeInfo answerNodeInfo =
        findNodeInfosById(nodeInfo, "com.chongdingdahui.app:id/answer0");
    if (answerNodeInfo == null) {
      return;
    }

    AccessibilityNodeInfo nodeInfoQuiz =
        AccessibilityHelper.findNodeInfosById(nodeInfo, "com.chongdingdahui.app:id/layoutQuiz");
    if (nodeInfoQuiz != null) {
      List<AccessibilityNodeInfo> nodeInfoList =
          AccessibilityHelper.findNodeInfosIsNotEmpty(nodeInfoQuiz);
      if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
        AccessibilityNodeInfo nodeTitle = nodeInfoList.get(nodeInfoList.size() - 1);
        if (nodeTitle != null) {
          postData(nodeTitle.getText().toString(), "");
        }
      }
    }
  }
}
