package com.yunmall.dati.util;

import com.yunmall.dati.DaTiApplication;
import com.yunmall.dati.http.SoHuApi;

/**
 * Created by wangsanjun on 18/1/26.
 * 搜狐接口轮询帮助类
 */

public class AnswersHelper {

  private static final int DYNAMIC_TIMER_MESSAGE = 1000;
  private static AnswersHelper mInstance;
  private boolean isPaused = true;

  private Runnable mMsgRunnable = new Runnable() {
    @Override public void run() {
      if (!isPaused) {
        SoHuApi.getInstance().getDataFromSoHu(DaTiApplication.getZhiBoType());
        DaTiApplication.getHandler().postDelayed(mMsgRunnable, DYNAMIC_TIMER_MESSAGE);
      }
    }
  };

  private void startDynamicTimer() {
    if (isPaused) {
      DaTiApplication.getHandler().post(mMsgRunnable);
    }
    isPaused = false;
  }

  private void cancelDynamicTimer() {
    isPaused = true;
    DaTiApplication.getHandler().removeCallbacks(mMsgRunnable);
  }

  public static AnswersHelper getInstance() {
    if (mInstance == null) {
      mInstance = new AnswersHelper();
    }
    return mInstance;
  }

  public static void stopDynamicPoll() {
    getInstance().cancelDynamicTimer();
  }

  public static void startDynamicPoll() {
    getInstance().startDynamicTimer();
  }
}
