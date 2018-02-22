package com.yunmall.dati.util;

import android.accessibilityservice.AccessibilityService;
import android.os.Build;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class AccessibilityHelper {

  private AccessibilityHelper() {
  }

  /** 通过id查找 */
  public static AccessibilityNodeInfo findNodeInfosById(AccessibilityNodeInfo nodeInfo,
      String resId) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
      List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(resId);
      if (list != null && !list.isEmpty()) {
        return list.get(0);
      }
    }
    return null;
  }

  /** 通过文本查找 */
  public static AccessibilityNodeInfo findNodeInfosByText(AccessibilityNodeInfo nodeInfo,
      String text) {
    List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(text);
    if (list == null || list.isEmpty()) {
      return null;
    }
    return list.get(0);
  }

  /** 通过关键字查找 */
  public static AccessibilityNodeInfo findNodeInfosByTexts(AccessibilityNodeInfo nodeInfo,
      String... texts) {
    for (String key : texts) {
      AccessibilityNodeInfo info = findNodeInfosByText(nodeInfo, key);
      if (info != null) {
        return info;
      }
    }
    return null;
  }


  /**
   * 通过组件名字查找
   */
  public static List<AccessibilityNodeInfo> findAllNodeInfosByClassName(
      AccessibilityNodeInfo nodeInfo, String name) {
    List<AccessibilityNodeInfo> targetNodes = new ArrayList<>();
    if (nodeInfo == null) {
      return targetNodes;
    }
    return findAllNodeInfosByClassName(nodeInfo, name, targetNodes);
  }

  private static List<AccessibilityNodeInfo> findAllNodeInfosByClassName(
      AccessibilityNodeInfo nodeInfo, String name, List<AccessibilityNodeInfo> targetNodes) {
    if (nodeInfo != null) {
      if (name.equals(nodeInfo.getClassName())) {
        targetNodes.add(nodeInfo);
      } else {
        for (int i = 0; i < nodeInfo.getChildCount(); i++) {
          findAllNodeInfosByClassName(nodeInfo.getChild(i), name, targetNodes);
        }
      }

    }
    return targetNodes;
  }

  /** 通过content-description或者text不为空来查找所有NodeInfo */
  public static List<AccessibilityNodeInfo> findNodeInfosIsNotEmpty(
      AccessibilityNodeInfo nodeInfo) {
    if (nodeInfo == null) {
      return null;
    }
    List<AccessibilityNodeInfo> nodeInfosNotEmpty = new ArrayList<>();
    return findNodeInfosIsNotEmpty(nodeInfo, nodeInfosNotEmpty);
  }

  private static List<AccessibilityNodeInfo> findNodeInfosIsNotEmpty(AccessibilityNodeInfo nodeInfo,
      List<AccessibilityNodeInfo> nodeInfosNotEmpty) {
    if (nodeInfo != null) {
      if ((!TextUtils.isEmpty(nodeInfo.getContentDescription()) || !TextUtils.isEmpty(
          nodeInfo.getText()))) {
        nodeInfosNotEmpty.add(nodeInfo);
      } else {
        for (int i = 0; i < nodeInfo.getChildCount(); i++) {
          findNodeInfosIsNotEmpty(nodeInfo.getChild(i), nodeInfosNotEmpty);
        }
      }
    }
    return nodeInfosNotEmpty;
  }

  /** 找父组件 */
  public static AccessibilityNodeInfo findParentNodeInfosByClassName(AccessibilityNodeInfo nodeInfo,
      String className) {
    if (nodeInfo == null) {
      return null;
    }
    if (TextUtils.isEmpty(className)) {
      return null;
    }
    if (className.equals(nodeInfo.getClassName())) {
      return nodeInfo;
    }
    return findParentNodeInfosByClassName(nodeInfo.getParent(), className);
  }

  private static final Field sSourceNodeField;

  static {
    Field field = null;
    try {
      field = AccessibilityNodeInfo.class.getDeclaredField("mSourceNodeId");
      field.setAccessible(true);
    } catch (Exception e) {
      e.printStackTrace();
    }
    sSourceNodeField = field;
  }

  public static long getSourceNodeId(AccessibilityNodeInfo nodeInfo) {
    if (sSourceNodeField == null) {
      return -1;
    }
    try {
      return sSourceNodeField.getLong(nodeInfo);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return -1;
  }

  public static String getViewIdResourceName(AccessibilityNodeInfo nodeInfo) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
      return nodeInfo.getViewIdResourceName();
    }
    return null;
  }

  /** 返回主界面事件 */
  public static void performHome(AccessibilityService service) {
    if (service == null) {
      return;
    }
    service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
  }

  /** 返回事件 */
  public static void performBack(AccessibilityService service) {
    if (service == null) {
      return;
    }
    service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
  }

  /** 点击事件 */
  public static void performClick(AccessibilityNodeInfo nodeInfo) {
    if (nodeInfo == null) {
      return;
    }
    if (nodeInfo.isClickable()) {
      nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
    } else {
      performClick(nodeInfo.getParent());
    }
  }
}
