package com.yunmall.dati.job;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
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
 * 今日头条辅助任务类
 */
public class TodayNewsJob extends BaseAccessbilityJob {

  private static final String TAG = "TodayNewsJob";

  /**
   * 头条包名
   */
  public static final String TODAY_PACKAGE_NAME = "com.ss.android.article.news";
  private String mLastAnswer = "";

  @Override public void onCreateJob(DaTiService service) {
    super.onCreateJob(service);
    Log.e(TAG, "onCreateJob: ");
  }

  @Override public void onStopJob() {
    Log.e(TAG, "onStopJob: ");
  }

  @Override public String getTargetPackageName() {
    return TODAY_PACKAGE_NAME;
  }


  @Override public void onReceiveJob(AccessibilityEvent event) {
    final int eventType = event.getEventType();
    if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
      if ("com.ixigua.feature.fantasy.FantasyActivity".equals(event.getClassName())) {
        Toast.makeText(getContext(), "打开了西瓜视频直播页面", Toast.LENGTH_LONG).show();
      }
      fetchContent(event);
    } else if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
      fetchContent(event);
    }
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN) private void fetchContent(AccessibilityEvent event) {

    if ("android.support.v7.widget.RecyclerView".equals(event.getClassName())) {
      fetchTodayContent(Config.TODAY_NEWS_QUESTION_ID, Config.TODAY_NEWS_ANSWER_ID);
    }
    DaTiApplication.setZhiBoType(1);
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
        String answer = "";
        List<AccessibilityNodeInfo> answerNodeInfoList =
            nodeInfo.findAccessibilityNodeInfosByViewId(answerId);
        if (answerNodeInfoList != null && !answerNodeInfoList.isEmpty()) {
          answer = constructStringAnswers(answerNodeInfoList);
          autoPerformClick(nodeInfo, answerNodeInfoList);
        }
        postData(questionTitle.toString(), answer);
      }
    }
  }

  /**
   * 当显示"时间到"时，自动选择答案
   */
  private void autoPerformClick(AccessibilityNodeInfo nodeInfo,
      List<AccessibilityNodeInfo> answerNodeInfoList) {
    if (answerNodeInfoList != null && !answerNodeInfoList.isEmpty()) {
      AccessibilityNodeInfo timeNodeInfo = AccessibilityHelper.findNodeInfosByText(nodeInfo, "时间到");
      if (timeNodeInfo != null) {
        String strUCAnswer = DaTiApplication.getUCAnswer();
        String strSogoAnswer = DaTiApplication.mSoHuSogou;

        String strAnswer = TextUtils.isEmpty(strUCAnswer) ? strSogoAnswer : strUCAnswer;
        if (!TextUtils.isEmpty(strAnswer)) {
          if (!strAnswer.equals(mLastAnswer)) {
            mLastAnswer = strAnswer;
            Toast.makeText(getContext(), "已为您选择答案:" + strAnswer, Toast.LENGTH_LONG).show();
          }
          for (AccessibilityNodeInfo answerNode : answerNodeInfoList) {
            if (strAnswer.contains(answerNode.getText().toString())) {
              AccessibilityHelper.performClick(answerNode);
            }
          }
        }
      }
    }
  }

  /**
   * 获取返回的所有的答案，用逗号分割
   * @param answerNodeInfoList
   * @return
   */
  @NonNull private String constructStringAnswers(List<AccessibilityNodeInfo> answerNodeInfoList) {
    String answer;

    StringBuilder stringBuilder = new StringBuilder("");
    for (int i = 0; i < answerNodeInfoList.size(); i++) {
      AccessibilityNodeInfo answerNode = answerNodeInfoList.get(i);
      stringBuilder.append(answerNode.getText() + ",");
    }
    answer = stringBuilder.toString().substring(0, stringBuilder.toString().lastIndexOf(","));
    return answer;
  }


}
