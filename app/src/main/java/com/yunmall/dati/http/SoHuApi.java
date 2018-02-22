package com.yunmall.dati.http;

import android.util.Log;
import com.google.gson.Gson;
import com.yunmall.dati.BuildConfig;
import com.yunmall.dati.Config;
import com.yunmall.dati.DaTiApplication;
import com.yunmall.dati.modal.DaAnResult;
import com.yunmall.dati.modal.DaTiType;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import static android.content.ContentValues.TAG;

/**
 * Created by wangsanjun on 18/1/26.
 * 获取搜狐答案接口
 */

public class SoHuApi {

  private SoHuApi() {
  }

  public static SoHuApi getInstance() {
    return SoHuProviderHolder.sInstance;
  }

  // 静态内部类
  private static class SoHuProviderHolder {
    private static final SoHuApi sInstance = new SoHuApi();
  }

  /**
   * 获取搜狐答案
   *
   * @param appType 1.百万英雄 2.冲顶大会 3.百万赢家 4.芝士超人
   */
  public void getDataFromSoHu(int appType) {
    String url = Config.SOHU_BASE_URL + Config.SOHU_GET_QUESTION_URL;
    if (BuildConfig.DEBUG) {
      Log.e(TAG, "sohu_url: " + url);
      Log.e(TAG, "appType: " + appType);
    }
    DaTiType datiType = new DaTiType(appType);
    String strType = new Gson().toJson(datiType);

    EasyHttp.post(url)
        .upJson(strType)
        .headers("cookie", "Hm_lvt_32b54343ac4b0930095e3ad0ae71c49e=1517289158")
        .headers("cookie", "Hm_lpvt_32b54343ac4b0930095e3ad0ae71c49e=1517470944")
        .headers("authority", "ss.sohu.com")
        .headers("user-agent",
            "Mozilla/5.0 (Linux; Android 6.0.1; SM-G9250 Build/MMB29K; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36 androidphone sohuinfonews2_2_11")
        .execute(new SimpleCallBack<String>() {
          @Override public void onError(ApiException e) {
          }

          @Override public void onSuccess(String response) {
            final DaAnResult daAnResult = new Gson().fromJson(response, DaAnResult.class);
            DaTiApplication.setDaAnResult(daAnResult);
          }
        });
  }
}
