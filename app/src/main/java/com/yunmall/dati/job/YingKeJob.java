package com.yunmall.dati.job;

import android.annotation.TargetApi;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;
import com.yunmall.dati.Config;
import com.yunmall.dati.DaTiApplication;
import com.yunmall.dati.DaTiService;
import com.yunmall.dati.util.AccessibilityHelper;
import java.util.List;

/**
 * 映客辅助任务类
 */
public class YingKeJob extends BaseAccessbilityJob {

  private static final String TAG = "YingKeJob";

  /** 映客包名 */
  public static final String YING_KE_PACKAGE_NAME = "com.meelive.ingkee";

  @Override public void onCreateJob(DaTiService service) {
    super.onCreateJob(service);
  }

  @Override public void onStopJob() {
  }

  @Override public String getTargetPackageName() {
    return YING_KE_PACKAGE_NAME;
  }

  protected String getQuestionId() {
    return Config.YING_KE_QUESTION_ID;
  }

  protected String getAnswerId() {
    return Config.YING_KE_NEWS_ANSWER_ID;
  }

  @Override public void onReceiveJob(AccessibilityEvent event) {
    final int eventType = event.getEventType();
    if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
      if ((getTargetPackageName() + ".business.room.ui.activity.RoomActivity").equals(
          event.getClassName())) {
        Toast.makeText(getContext(), "打开了映客直播页面", Toast.LENGTH_SHORT).show();
      }
      openNews(event);
    } else if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
      openNews(event);
    }
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN) private void openNews(AccessibilityEvent event) {
    fetchTodayContent(getQuestionId(), getAnswerId());
    DaTiApplication.setZhiBoType(4);
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
  private void fetchTodayContent(String questionId, String answerId) {

    AccessibilityNodeInfo nodeInfo = getService().getRootInActiveWindow();
    if (nodeInfo == null) {
      Log.w(TAG, "rootWindow为空");
      return;
    }

    AccessibilityNodeInfo titleNodeInfo =
        AccessibilityHelper.findNodeInfosById(nodeInfo, questionId);
    if (titleNodeInfo != null) {
      CharSequence questionTitle = titleNodeInfo.getText();
      if (!TextUtils.isEmpty(questionTitle)) {
        Log.w(TAG, questionTitle.toString());
        List<AccessibilityNodeInfo> answerNodeInfoList =
            nodeInfo.findAccessibilityNodeInfosByViewId(answerId);
        if (answerNodeInfoList != null && !answerNodeInfoList.isEmpty()) {
          StringBuilder stringBuilder = new StringBuilder("");
          for (AccessibilityNodeInfo answerNode : answerNodeInfoList) {
            stringBuilder.append(answerNode.getText() + ",");
          }

          AccessibilityNodeInfo numberNodeInfo = AccessibilityHelper.findNodeInfosById(nodeInfo,
              getTargetPackageName() + ":id/tv_index");
          CharSequence numberQuestion = numberNodeInfo.getText();
          postData(numberQuestion.toString(), questionTitle.toString(), stringBuilder.toString());
        }
      }
    }
  }
}
