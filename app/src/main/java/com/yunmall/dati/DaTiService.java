package com.yunmall.dati;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;
import com.yunmall.dati.job.AccessbilityJob;
import com.yunmall.dati.job.ChongDingJob;
import com.yunmall.dati.job.HuaJiaoJob;
import com.yunmall.dati.job.TodayNewsJob;
import com.yunmall.dati.job.XiguaJob;
import com.yunmall.dati.job.YingKeJob;
import com.yunmall.dati.job.ZhiShiChaoRenJob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * 答题辅助服务
 */
public class DaTiService extends AccessibilityService {

  private static final String TAG = "DaTiService";

  private static final Class[] ACCESSBILITY_JOBS = {
      TodayNewsJob.class, XiguaJob.class, YingKeJob.class, ZhiShiChaoRenJob.class,
      ChongDingJob.class, HuaJiaoJob.class

  };


  private static final String[] LIVE_PLATFORM_CHINESE_NAME = {
      "百万英雄(今日头条)", "百万英雄(西瓜)", "芝士超人(映客)", "芝士超人", "冲顶大会", "百万赢家(花椒)"
  };

  private static DaTiService service;

  private List<AccessbilityJob> mAccessbilityJobs;
  private HashMap<String, AccessbilityJob> mPkgAccessbilityJobMap;
  private HashMap<String, String> mPkgLiveMap;

  @Override public void onCreate() {
    super.onCreate();

    mAccessbilityJobs = new ArrayList<>();
    mPkgAccessbilityJobMap = new HashMap<>();
    mPkgLiveMap = new HashMap<>();

    //初始化辅助插件工作
    for (int i = 0; i < ACCESSBILITY_JOBS.length; i++) {
      Class clazz = ACCESSBILITY_JOBS[i];
      try {
        Object object = clazz.newInstance();
        if (object instanceof AccessbilityJob) {
          AccessbilityJob job = (AccessbilityJob) object;
          job.onCreateJob(this);
          mAccessbilityJobs.add(job);
          mPkgAccessbilityJobMap.put(job.getTargetPackageName(), job);
          mPkgLiveMap.put(job.getTargetPackageName(), LIVE_PLATFORM_CHINESE_NAME[i]);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @Override public void onDestroy() {
    super.onDestroy();
    Log.d(TAG, "dati service destory");
    if (mPkgAccessbilityJobMap != null) {
      mPkgAccessbilityJobMap.clear();
    }
    if (mAccessbilityJobs != null && !mAccessbilityJobs.isEmpty()) {
      for (AccessbilityJob job : mAccessbilityJobs) {
        job.onStopJob();
      }
      mAccessbilityJobs.clear();
    }

    service = null;
    mAccessbilityJobs = null;
    mPkgAccessbilityJobMap = null;
    //发送广播，已经断开辅助服务
    Intent intent = new Intent(Config.ACTION_DA_TI_SERVICE_DISCONNECT);
    sendBroadcast(intent);
  }

  @Override public void onInterrupt() {
    Log.d(TAG, "dati service interrupt");
    Toast.makeText(this, "中断答题助手服务", Toast.LENGTH_SHORT).show();
  }

  @Override protected void onServiceConnected() {
    super.onServiceConnected();
    service = this;
    //发送广播，已经连接上了
    Intent intent = new Intent(Config.ACTION_DA_TI_SERVICE_CONNECT);
    sendBroadcast(intent);
    Toast.makeText(this, "已连接答题助手服务", Toast.LENGTH_SHORT).show();
  }

  @Override public void onAccessibilityEvent(AccessibilityEvent event) {
    String pkn = String.valueOf(event.getPackageName());
    if (mAccessbilityJobs != null && !mAccessbilityJobs.isEmpty()) {
      for (AccessbilityJob job : mAccessbilityJobs) {
        if (pkn.equals(job.getTargetPackageName())) {
          job.onReceiveJob(event);
          DaTiApplication.setPlatformChineseName(mPkgLiveMap.get(job.getTargetPackageName()));
        }
      }
    }
  }

  public Config getConfig() {
    return Config.getConfig(this);
  }


  /**
   * 判断当前服务是否正在运行
   */
  @TargetApi(Build.VERSION_CODES.JELLY_BEAN) public static boolean isRunning() {
    if (service == null) {
      return false;
    }
    AccessibilityManager accessibilityManager =
        (AccessibilityManager) service.getSystemService(Context.ACCESSIBILITY_SERVICE);
    AccessibilityServiceInfo info = service.getServiceInfo();
    if (info == null) {
      return false;
    }
    List<AccessibilityServiceInfo> list = accessibilityManager.getEnabledAccessibilityServiceList(
        AccessibilityServiceInfo.FEEDBACK_GENERIC);
    Iterator<AccessibilityServiceInfo> iterator = list.iterator();

    boolean isConnect = false;
    while (iterator.hasNext()) {
      AccessibilityServiceInfo i = iterator.next();
      if (i.getId().equals(info.getId())) {
        isConnect = true;
        break;
      }
    }
    if (!isConnect) {
      return false;
    }
    return true;
  }
}
