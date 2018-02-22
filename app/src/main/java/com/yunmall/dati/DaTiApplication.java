package com.yunmall.dati;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import com.baidu.mobstat.StatService;
import com.mcxiaoke.packer.helper.PackerNg;
import com.yunmall.dati.modal.DaAn;
import com.yunmall.dati.modal.DaAnResult;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.converter.SerializableDiskConverter;
import com.zhouyou.http.cache.model.CacheMode;
import floatwindow.xishuang.float_lib.FloatActionController;
import java.util.ArrayList;

import static android.text.TextUtils.isEmpty;

public class DaTiApplication extends Application {

  private static Handler mHandler;
  public static DaTiApplication daTiApplication;
  /**
   * 搜狐接口对应的直播平台类型
   * 1.百万英雄 2.冲顶大会 3.百万赢家 4.芝士超人
   */
  private static int mZhiBoType = 1;
  private static String mSoHuUCAnswer;
  public static String mSoHuSogou;
  private static String mPlatFormChineseName;

  @Override public void onCreate() {
    super.onCreate();
    mHandler = new Handler(Looper.getMainLooper());
    daTiApplication = this;
    initHttp();
    setAppChannel();
  }

  private void initHttp() {
    EasyHttp.init(this);//默认初始化

    //以下设置的所有参数是全局参数,同样的参数可以在请求的时候再设置一遍,那么对于该请求来讲,请求中的参数会覆盖全局参数
    EasyHttp.getInstance()

        //可以全局统一设置全局URL
        .setBaseUrl(Config.BASE_AO_LAI_URL)//设置全局URL  url只能是域名 或者域名+端口号

        // 打开该调试开关并设置TAG,不需要就不要加入该行
        // 最后的true表示是否打印内部异常，一般打开方便调试错误
        .debug("EasyHttp", true)

        //如果使用默认的60秒,以下三行也不需要设置
        .setReadTimeOut(60 * 1000).setWriteTimeOut(60 * 100).setConnectTimeout(60 * 100)

        //可以全局统一设置超时重连次数,默认为3次,那么最差的情况会请求4次(一次原始请求,三次重连请求),
        //不需要可以设置为0
        .setRetryCount(3)//网络不好自动重试3次
        //可以全局统一设置超时重试间隔时间,默认为500ms,不需要可以设置为0
        .setRetryDelay(500)//每次延时500ms重试
        //可以全局统一设置超时重试间隔叠加时间,默认为0ms不叠加
        .setRetryIncreaseDelay(500)//每次延时叠加500ms

        //可以全局统一设置缓存模式,默认是不使用缓存,可以不传,具体请看CacheMode
        .setCacheMode(CacheMode.NO_CACHE)
        //可以全局统一设置缓存时间,默认永不过期
        .setCacheTime(-1)//-1表示永久缓存,单位:秒 ，Okhttp和自定义RxCache缓存都起作用
        //全局设置自定义缓存保存转换器，主要针对自定义RxCache缓存
        .setCacheDiskConverter(new SerializableDiskConverter())//默认缓存使用序列化转化
        //全局设置自定义缓存大小，默认50M
        .setCacheMaxSize(100 * 1024 * 1024)//设置缓存大小为100M
        //设置缓存版本，如果缓存有变化，修改版本后，缓存就不会被加载。特别是用于版本重大升级时缓存不能使用的情况
        .setCacheVersion(1)//缓存版本为1
        //.setHttpCache(new Cache())//设置Okhttp缓存，在缓存模式为DEFAULT才起作用

        //可以设置https的证书,以下几种方案根据需要自己设置
        .setCertificates();                          //方法一：信任所有证书,不安全有风险
    //.setCertificates(new SafeTrustManager())            //方法二：自定义信任规则，校验服务端证书
    //配置https的域名匹配规则，不需要就不要加入，使用不当会导致https握手失败
    //.setHostnameVerifier(new SafeHostnameVerifier())
    //.addConverterFactory(GsonConverterFactory.create(gson))//本框架没有采用Retrofit的Gson转化，所以不用配置
    //.addCommonHeaders(headers)//设置全局公共头
    //.addCommonParams(params) //设置全局公共参数
    //.addNetworkInterceptor(new NoCacheInterceptor())//设置网络拦截器
    //.setCallFactory()//局设置Retrofit对象Factory
    //.setCookieStore(EasyHttp.getCookieJar());//设置cookie
    //.setOkproxy()//设置全局代理
    //.setOkconnectionPool()//设置请求连接池
    //.setCallbackExecutor()//全局设置Retrofit callbackExecutor
    //可以添加全局拦截器，不需要就不要加入，错误写法直接导致任何回调不执行
    //.addInterceptor(new GzipRequestInterceptor())//开启post数据进行gzip后发送给服务器
    //.addInterceptor(new CustomSignInterceptor());//添加参数签名拦截器
  }

  private void setAppChannel() {
    String market = getChannelName();
    StatService.setAppChannel(market);
  }

  @NonNull private String getChannelName() {
    String market = PackerNg.getMarket(this);
    if (isEmpty(market)) {
      market = "guanfang";
    }
    return market;
  }

  public static Handler getHandler() {
    return DaTiApplication.mHandler;
  }

  public static void setZhiBoType(int zhiBoType) {
    if (mZhiBoType != zhiBoType) {
      mZhiBoType = zhiBoType;
    }
  }

  public static int getZhiBoType() {
    return mZhiBoType;
  }

  /**
   * 将获取的搜狐答案设置到悬浮窗
   * @param pDaAnResult
   */
  public static void setDaAnResult(DaAnResult pDaAnResult) {
    if (pDaAnResult != null
        && pDaAnResult.getData() != null
        && pDaAnResult.getData().getResults() != null
        && !pDaAnResult.getData().getResults().isEmpty()) {
      ArrayList<DaAn> daAnArrayList = pDaAnResult.getData().getResults();
      String sogou = "";
      String baidu = "";
      String uc = "";
      String maiche = "";

      String[] answers = new String[] { sogou, baidu, uc, maiche };
      for (int i = 0; i < daAnArrayList.size(); i++) {
        if (!isEmptyAnswer(daAnArrayList.get(i))) {
          answers[i] = (isEmpty(daAnArrayList.get(i).getOption()) ? ""
              : (daAnArrayList.get(i).getOption() + ".")) + daAnArrayList.get(i).getOptionContent();
        }
      }
      FloatActionController.getInstance()
          .setAnswers(mPlatFormChineseName, "搜狗旺仔：" + answers[0],
              "百度蛋哥：" + answers[1], "UC：" + answers[3]);
      mSoHuSogou = answers[0];
      mSoHuUCAnswer = answers[3];
    }
  }

  /**
   * 获取UC的答案，用于"西瓜视频"自动点击
   * @return
   */
  public static String getUCAnswer() {
    return mSoHuUCAnswer;
  }

  private static boolean isEmptyAnswer(DaAn daAn) {
    String option = daAn.getOption();
    String answer = daAn.getOptionContent();
    if (isEmpty(option) && isEmpty(answer)) {
      return true;
    }
    return false;
  }



  /** 首个activity启动调用 */
  public static void activityStartMain(Activity activity) {

  }

  /** 每个activity生命周期里的onCreate */
  public static void activityCreateStatistics(Activity activity) {

  }

  /** 每个activity生命周期里的onResume */
  public static void activityResumeStatistics(Activity activity) {
    StatService.onResume(activity);
  }

  /** 每个activity生命周期里的onPause */
  public static void activityPauseStatistics(Activity activity) {
    StatService.onPause(activity);
  }

  /** 事件统计 */
  public static void eventStatistics(Context context, String event) {
    StatService.onEvent(context, "答题点击事件", event);
  }

  /** 事件统计 */
  public static void eventStatistics(Context context, String event, String tag) {

  }

  /**
   * 设置悬浮窗显示的中文平台名
   * @param platformChineseName
   */
  public static void setPlatformChineseName(String platformChineseName) {
    mPlatFormChineseName = platformChineseName;
  }

}
