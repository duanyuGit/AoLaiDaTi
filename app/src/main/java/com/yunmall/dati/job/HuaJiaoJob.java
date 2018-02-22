package com.yunmall.dati.job;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;
import com.yunmall.dati.DaTiApplication;
import com.yunmall.dati.DaTiService;
import com.yunmall.dati.util.AccessibilityHelper;
import java.util.List;

/**
 * 百万赢家(花椒)辅助任务类
 */
public class HuaJiaoJob extends BaseAccessbilityJob {

  private static final String TAG = "HuajiaoJob";

  /**
   * 花椒包名
   */
  public static final String HUA_JIAO_PACKAGE_NAME = "com.huajiao";

  @Override public void onCreateJob(DaTiService service) {
    super.onCreateJob(service);
  }

  @Override public void onStopJob() {
  }

  @Override public String getTargetPackageName() {
    return HUA_JIAO_PACKAGE_NAME;
  }

  @Override public void onReceiveJob(AccessibilityEvent event) {
    final int eventType = event.getEventType();
    if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
      if (event.getClassName().equals("com.huajiao.detail.WatchesListActivity")) {
        Toast.makeText(getContext(), "花椒直播", Toast.LENGTH_LONG).show();
      }
      fetchContent();
    } else if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
      fetchContent();
    }
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2) private void fetchContent() {
    DaTiApplication.setZhiBoType(3);
    AccessibilityNodeInfo nodeInfo = getService().getRootInActiveWindow();
    if (nodeInfo == null) {
      Log.w(TAG, "rootWindow为空");
      return;
    }

    AccessibilityNodeInfo nodeInfo1 =
        AccessibilityHelper.findNodeInfosById(nodeInfo, "com.huajiao:id/livewebview");
    if (nodeInfo1 == null) {
      return;
    }
    List<AccessibilityNodeInfo> nodeInfoList =
        AccessibilityHelper.findAllNodeInfosByClassName(nodeInfo1, "android.widget.ListView");

    if (nodeInfoList == null || nodeInfoList.isEmpty()) {
      return;
    }
    AccessibilityNodeInfo listViewNodeInfo = nodeInfoList.get(0);
    AccessibilityNodeInfo listViewParentNodeInfo = listViewNodeInfo.getParent();
    if (listViewParentNodeInfo == null) {
      return;
    }

    AccessibilityNodeInfo titleNode = listViewParentNodeInfo.getChild(0);
    if (titleNode == null) {
      return;
    }
    CharSequence questionTitle = titleNode.getContentDescription();

    if (titleNode != null && listViewNodeInfo != null) {
      if (!TextUtils.isEmpty(questionTitle)) {
        Log.w(TAG, questionTitle.toString());
        String strAnswers = getAnswers(listViewNodeInfo);
        postData(questionTitle.toString(), strAnswers);
      }
    }
  }

  /**
   * 获取所有答案
   */
  @NonNull private String getAnswers(AccessibilityNodeInfo listAnswerNodeInfo) {
    StringBuilder stringBuilder = new StringBuilder("");
    List<AccessibilityNodeInfo> nodeInfoTextView =
        AccessibilityHelper.findNodeInfosIsNotEmpty(listAnswerNodeInfo);
    if (nodeInfoTextView != null && nodeInfoTextView.size() <= 3) {
      for (int i = 0; i < nodeInfoTextView.size(); i++) {
        if (!TextUtils.isEmpty(nodeInfoTextView.get(i).getContentDescription())) {
          stringBuilder.append(nodeInfoTextView.get(i).getContentDescription() + ",");
        }
      }
    }
    return stringBuilder.toString();
  }
}
